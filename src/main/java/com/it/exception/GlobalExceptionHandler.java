package com.it.exception;

import com.it.common.R;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ClassName: GlobalExceptionHandle
 * Description:
 * 全局异常处理器
 * @Author Joel
 * @Create 2024/9/20
 * @Version 1.0
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public R handleException(Exception e){
        e.printStackTrace();
        return R.error(StringUtils.hasLength(e.getMessage())? e.getMessage():"操作失败");
    }
}
