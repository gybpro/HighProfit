package com.high.highprofit.advice;

import com.high.highprofit.dto.ResultDTO;
import com.high.highprofit.exception.ServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@ControllerAdvice // 增强控制器(控制器切面)
public class ExceptionAdvice {
    /* 错误分2类处理：
        1. 未知异常，捕获Exception异常，给客户端提示系统异常即可！
        2. 业务异常，使用自定义异常！提示具体错误！
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResultDTO serviceExceptionAdvice(ServiceException e) {
        e.printStackTrace(); // 在控制台打印错误日志
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode("0");
        resultDTO.setMessage(e.getMessage());
        return resultDTO;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResultDTO exceptionAdvice(Exception e) {
        e.printStackTrace(); // 在控制台打印错误日志
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode("0");
        // resultDTO.setMessage("系统异常！");
        resultDTO.setMessage(e.getMessage());
        return resultDTO;
    }
}
