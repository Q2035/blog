package top.hellooooo.blog.util;

public class CommonResult<T> {
    private int code;
    private T data;
    private String message;
    private boolean success;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 成功
     * @param data
     * @return
     * @param <T>
     */
    public static <T> CommonResult<T> succ(T data) {
        final CommonResult commonResult = new CommonResult();
        commonResult.setData(data);
        commonResult.setCode(StatusCode.SUCCESS.getCode());
        commonResult.setSuccess(true);
        return commonResult;
    }

    @Override
    public String toString() {
        return "CommonResult{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                ", success=" + success +
                '}';
    }
}
