package top.hellooooo.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.hellooooo.blog.util.QiniuUtil;

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
    public void testUploadSingleFile() {
        System.out.println(qiniuUtil.fileUpload(""));
    }

}