package top.hellooooo.blog.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Q
 * @date 08/04/2021 08:25
 * @description 此工具类的目的是将MarkDown中图片链接的图片传到七牛云，同时替换URL地址
 * 所以，该工具生效的前提是，将本地的图片上传至服务器
 */
public class QiniuUtil {

    @Setter
    @Value("${qiniu.accesskey}")
    private String accessKey;

    @Setter
    @Value("${qiniu.secretkey}")
    private String secretKey;

    @Setter
    @Value("${qiniu.bucketName}")
    private String bucketName;

    @Setter
    @Value("${qiniu.originDomain}")
    private String originDomain;

    Auth auth;

    ObjectMapper objectMapper = new ObjectMapper();

    Logger logger = LoggerFactory.getLogger(this.getClass());

    Configuration cfg;

    BucketManager bucketManager;

    UploadManager uploadManager;

    private final int MAX_IMAGE_COT = 100;

    public void init(){
        auth = Auth.create(accessKey, secretKey);
        // 华南机房
        cfg = new Configuration(Region.region2());
        bucketManager = new BucketManager(auth, cfg);
        uploadManager = new UploadManager(cfg);
    }

    /**
     * 将指定URL的文件上传至七牛云
     * 相同文件不会覆盖上传
     * @param originUrl
     */
    public DefaultPutRet singleFileUpload(String originUrl) throws IOException {
        if (!originUrl.startsWith("http")) {
            throw new IllegalArgumentException("QiniuUtil#singleFileUpload: Not the supported protocol");
        }
        // 准备从网络下载图片
        URL url;
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        url = new URL(originUrl);
        httpURLConnection = (HttpURLConnection) url.openConnection();
        inputStream = httpURLConnection.getInputStream();
        // String localFilePath = "/home/qiniu/test.png";
        String key = getFilename(originUrl);
        return fileUpload(inputStream, key);
    }

    /**
     * 将输入流的文件上传到七牛
     * @param inputStream
     * @param key
     * @return
     * @throws QiniuException
     * @throws JsonProcessingException
     */
    public DefaultPutRet fileUpload(InputStream inputStream, String key) throws QiniuException, JsonProcessingException {
        String upToken = auth.uploadToken(bucketName);
        // 此处的key作为上传后的文件名
        Response response = uploadManager.put(inputStream, key, upToken, null, null);
        // 解析上传成功的结果
        DefaultPutRet putRet = objectMapper.readValue(response.bodyString(), DefaultPutRet.class);
        return putRet;
    }

    /**
     * 将所提供的url中的所有图片数据都上传至七牛
     * @param originUrls
     */
    public void fileBatchUpload(List<String> originUrls) throws InterruptedException {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(MAX_IMAGE_COT);
        int urlCot = originUrls.size();
        AtomicInteger produceCot = new AtomicInteger(0);
        AtomicInteger consumption = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        // 借助生产者消费者模型
        Runnable getImageInputStreamTask = () -> {
            originUrls.forEach(url -> {
                url = url.trim();
                logger.info("{} is added into array", url);
                arrayBlockingQueue.add(url);
                produceCot.incrementAndGet();
            });
            countDownLatch.countDown();
        };
        Runnable uploadImageTask = () -> {
            // 只要完成的任务数少于原始数据数就不断继续
            while (consumption.get() < urlCot) {
                String urlStr = null;
                try {
                    // 阻塞式获取
                    urlStr = arrayBlockingQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    URL url = new URL(urlStr.trim());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    // Header的Content-Length存放了文件长度，后序可以考虑借此实现上传进度
                    InputStream inputStream = urlConnection.getInputStream();
                    String key = getFilename(urlStr);
                    logger.info("{} will upload", key);
                    fileUpload(inputStream, key);
                    consumption.incrementAndGet();
                    logger.info("{} upload successfully", key);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            countDownLatch.countDown();
        };
        new Thread(getImageInputStreamTask).start();
        new Thread(uploadImageTask).start();
        countDownLatch.await();
    }

    /**
     * https://hellooooo.top/image/xxx.jpg -> image/xxx.jpg
     * @param path
     * @return
     */
    private String getFilename(String path) {
        if (StringUtils.isNullOrEmpty(path)) {
            return null;
        }
        int ind = path.indexOf(originDomain);
        // 非指定域名下的图片
        if (ind == -1) {
            return null;
        }
        String substring;
        try {
            // 这里+1是为了移除字符串开头的斜杠
            substring = path.substring(ind + 1 + originDomain.length());
        } catch (Exception e) {
            logger.error("QiniuUtils#getFilename: " + path);
            return null;
        }
        return substring;
    }
}
