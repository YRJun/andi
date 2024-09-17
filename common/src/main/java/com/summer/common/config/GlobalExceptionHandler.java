package com.summer.common.config;

import com.summer.common.constant.Constant;
import com.summer.common.exception.ConflictException;
import jakarta.annotation.Nonnull;
import org.slf4j.MDC;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.Principal;

/**
 * 全局异常处理返回
 * @author Renjun Yu
 * @date 2024/06/14 23:00
 */
//@RestControllerAdvice(annotations = UseExceptionHandler.class)
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("FieldCanBeLocal")
    private final String INTERNAL_SERVER_ERROR_MESSAGE = "内部服务错误";
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
        ProblemDetail problemDetail = createProblemDetail(ex, status, message, null, null, request);
        this.setPropertyForProblemDetail(problemDetail, request.getUserPrincipal());
        return handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail problemDetail = createProblemDetail(ex, status, ex.getMessage(), null, null, request);
        this.setPropertyForProblemDetail(problemDetail, request.getUserPrincipal());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({ConflictException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> handleConflictExceptions(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDetail problemDetail = createProblemDetail(ex, status, INTERNAL_SERVER_ERROR_MESSAGE, null, null, request);
        this.setPropertyForProblemDetail(problemDetail, request.getUserPrincipal());
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), status, request);
    }

    private void setPropertyForProblemDetail(ProblemDetail problemDetail, Principal principal) {
        problemDetail.setProperty(Constant.TRACE_ID, MDC.get(Constant.TRACE_ID));
        problemDetail.setProperty(Constant.SPAN_ID, MDC.get(Constant.SPAN_ID));
        if (principal != null) {
            problemDetail.setProperty("principal", principal.getName());
        }
    }

}



