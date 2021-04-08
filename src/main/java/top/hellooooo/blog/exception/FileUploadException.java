package top.hellooooo.blog.exception;

/**
 * @author Q
 * @date 08/04/2021 15:34
 * @description
 */
public class FileUploadException extends RuntimeException {
    public FileUploadException() {
    }

    public FileUploadException(String message) {
        super(message);
    }

    public FileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}

