package com.duybao.QUANLYCHITIEU.Exception;

import com.duybao.QUANLYCHITIEU.DTO.Response.ApiResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleBaseException(AppException ex) {
        ApiResponse<Object> response = ApiResponse.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getErrorCode().getMessage())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getErrorCode().getHttpStatusCode()).body(response);
    }

    @ExceptionHandler(org.springframework.security.authorization.AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDeniedExceptions(AuthorizationDeniedException ex) {
        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;
        ApiResponse<Object> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<ApiResponse<Object>> handleJwtException(JwtException ex) {
//        ErrorCode errorCode = ErrorCode.TOKEN_INVALID;
//        ApiResponse<Object> response = ApiResponse.builder()
//                .code(errorCode.getCode())
//                .message(errorCode.getMessage())
//                .timestamp(LocalDateTime.now())
//                .build();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();


        List<String> codeKey = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();


        List<ErrorCode> errorCode = codeKey.stream().map(this::mapToErrorCode).toList();
        List<String> errM=errorCode.stream().map(ErrorCode::getMessage).toList();
        List<Integer> errC=errorCode.stream().map(ErrorCode::getCode).toList();
        String errM2= String.join(",",errM);
//        Integer errC2= String.join(",",errC);
        ApiResponse<Object> body = ApiResponse.<Object>builder()
                .success(false)
                .message(errM2)
                .timestamp(java.time.LocalDateTime.now())
                .build();

//        return ResponseEntity.status(errorCode.getHttpStatusCode()).body(body);
        return ResponseEntity.badRequest().body(body);

    }

    private ErrorCode mapToErrorCode(String codeKey) {
        if (codeKey == null) return ErrorCode.INTERNAL_ERROR;
        try {
            // Nếu defaultMessage chính là tên enum, dùng valueOf
            return ErrorCode.valueOf(codeKey);
        } catch (IllegalArgumentException e) {
            return ErrorCode.INVALID_REQUEST;
        }
    }
}
