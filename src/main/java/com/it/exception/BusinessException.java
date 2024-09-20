package com.it.exception;

/**
 * ClassName: BusinessException
 * Description:
 * 自定义业务异常类，继承RuntimeException，用于业务逻辑异常的抛出
 * @Author Joel
 * @Create 2024/9/20
 * @Version 1.0
 */



public class BusinessException extends RuntimeException {

    //异常的消息提示
    private String message;

    public BusinessException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
    }

    public BusinessException(Throwable e) {
        super(e);
    }


    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 重写fillInStackTrace 业务异常不需要堆栈信息，提高效率.
     */
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }

}
