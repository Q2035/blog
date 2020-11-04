package top.hellooooo.blog.pojo;

import java.util.Date;

/**
 * @Create: 13/04/2020 21:35
 * @Author: Q
 */
public class FileVO {
    private String fileName;
    private String fileSize;
    private Date updateDate;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
