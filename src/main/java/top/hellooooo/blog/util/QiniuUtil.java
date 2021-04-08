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
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringUtils;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import top.hellooooo.blog.exception.FileUploadException;

import javax.xml.stream.events.Characters;
import java.nio.charset.StandardCharsets;

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

    public void init(){
        auth = Auth.create(accessKey, secretKey);
        // 华南机房
        cfg = new Configuration(Region.region2());
        bucketManager = new BucketManager(auth, cfg);
    }



    /**
     * 将指定URL的文件上传至七牛云
     * 相同文件不会覆盖上传
     * @param originUrl
     * @return false 传送失败 true 传送成功
     */
    public boolean fileUpload(String originUrl) {
        //构造一个带指定 Region 对象的配置类
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        // 传输的本地文件路径
        String localFilePath = "D:\\Tmp\\a.jpg";
        // String localFilePath = "/home/qiniu/test.png";
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = getFilename(originUrl);
        // 形如 image/a.jpg -> https://cdn.hellooooo.top/image/a.jpg
        String upToken = auth.uploadToken(bucketName);
        try {
            // 此处的key作为上传后的文件名
            Response response = uploadManager.put(localFilePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = objectMapper.readValue(response.bodyString(), DefaultPutRet.class);
            logger.info(putRet.key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            try {
                logger.error(r.bodyString());
            } catch (QiniuException ex2) {
                return false;
            }
        } catch (JsonProcessingException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件相关信息
     * @return
     */
    public FileInfo getFileInfo(String key){
        //构造一个带指定 Region 对象的配置类
        try {
            FileInfo fileInfo = bucketManager.stat(bucketName, key);
            return fileInfo;
        } catch (QiniuException ex) {
            logger.error(ex.response.toString());
        }
        return null;
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
