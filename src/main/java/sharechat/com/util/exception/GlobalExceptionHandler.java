package sharechat.com.util.exception;

import sharechat.com.util.result.ResultCode;
import sharechat.com.util.result.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RestControllerAdvice
@Order(1)
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }


    private String handlerErrors(BindingResult bindingResult) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        FieldError error = (FieldError)errors.get(0);
        return error.getDefaultMessage();
    }

    @ExceptionHandler({BizException.class})
    public Result<?> bizExceptionHandler(HttpServletRequest request, BizException e) {
        Result<?> result = Result.error(e.getCode() == null ? ResultCode.BIZ_ERROR.getCode() : e.getCode(), e.getMessage());
        return this.printLogAndReturn(request, result, e);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class})
    public Result<?> httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request, Exception e) {
        Result<?> result = Result.error(ResultCode.REQ_MODE_NOT_SUPPORTED);
        return this.printLogAndReturn(request, result, e);
    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> exceptionHandler(HttpServletRequest request, Exception e) {
        Result<?> result = Result.error(ResultCode.SYS_ERROR);
        return this.printLogAndReturn(request, result, e);
    }

    private Result<?> printLogAndReturn(HttpServletRequest request, Result<?> result, Exception e) {
        ObjectMapper mapper = new ObjectMapper();

        String requestUrl = request.getRequestURL().toString() + (!StringUtils.hasLength(request.getQueryString()) ? "" : "?" + request.getQueryString());
        try {
            log.error("<-异常返回-> 请求接口:{} | 异常时间:{} | 异常结果:{}", requestUrl, System.currentTimeMillis(), mapper.writeValueAsString(result));

        } catch (JsonProcessingException jsonProcessingException) {
            jsonProcessingException.printStackTrace();
        }
        log.error("<--异常堆栈信息-->");
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        log.error(stringWriter.toString());
        return result;
    }
}
