package top.hellooooo.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hellooooo.blog.util.QiniuUtil;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Q
 * @date 08/04/2021 15:04
 * @description
 */
@SpringBootTest
public class FileUploadTest {

    @Autowired
    private QiniuUtil qiniuUtil;

    @Test
    public void testUploadSingleFile() throws IOException, InterruptedException {
        // System.out.println(qiniuUtil.singleFileUpload("https://www.hellooooo.top/image/1.png"));
        qiniuUtil.fileBatchUpload(Arrays.asList("https://www.hellooooo.top/image/1.png", "https://www.hellooooo.top/image/2.png", "https://www.hellooooo.top/image/3.png"));
    }


}