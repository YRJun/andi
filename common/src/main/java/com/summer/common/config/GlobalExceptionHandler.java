package com.summer.common.config;

import com.summer.common.constant.Constant;
import jakarta.annotation.Nonnull;
import org.slf4j.MDC;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 全局异常处理返回
 * @author Renjun Yu
 * @date 2024/06/14 23:00
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(@Nonnull Exception ex,
                                                             @Nullable Object body,
                                                             @Nonnull HttpHeaders headers,
                                                             @Nonnull HttpStatusCode statusCode,
                                                             @Nonnull WebRequest request) {
        logger.error(ex.getMessage(), ex);
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @Nonnull HttpHeaders headers,
                                                                  @Nonnull HttpStatusCode status,
                                                                  @Nonnull WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        String message = ex.getMessage();
        FieldError fieldError = bindingResult.getFieldError();
        if (bindingResult.hasErrors() && fieldError != null) {
            message = fieldError.getField() + ":" + fieldError.getDefaultMessage();
        }
        ProblemDetail body = createProblemDetail(ex, status, message, null, null, request);
        body.setProperty(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID));
        return handleExceptionInternal(ex, body, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail body = createProblemDetail(ex, status, ex.getMessage(), null, null, request);
        body.setProperty(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID));
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessExceptions(DataAccessException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail body = createProblemDetail(ex, status, "内部服务错误", null, null, request);
        body.setProperty(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID));
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<Object> handleRestClientExceptions(RestClientException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail body = createProblemDetail(ex, status, "内部服务错误", null, null, request);
        body.setProperty(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID));
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentExceptions(IllegalArgumentException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail body = createProblemDetail(ex, status, ex.getMessage(), null, null, request);
        body.setProperty(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID));
        return handleExceptionInternal(ex, body, new HttpHeaders(), status, request);
    }



}



