package com.example.zerowaste_api.config; // Or a dedicated 'exception' package

import com.example.zerowaste_api.common.ResponseDTO;
import com.example.zerowaste_api.common.ServiceAppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This handler specifically catches your existing ServiceAppException
     * and formats it into your standard ResponseDTO.
     */
    @ExceptionHandler(ServiceAppException.class)
    public ResponseEntity<ResponseDTO<Object>> handleServiceAppException(ServiceAppException ex, WebRequest request) {

        // Create the error response DTO using information from your exception
        ResponseDTO<Object> errorResponse = new ResponseDTO<>(
                null, // No data for errors
                ex.getMessage(), // The message goes into the 'errorCode' field as requested
                ex.getServiceAppStatusCode() // The status comes directly from the exception
        );

        return new ResponseEntity<>(errorResponse, org.springframework.http.HttpStatus.valueOf(ex.getServiceAppStatusCode()));
    }

    // You can still have a fallback for completely unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Object>> handleGenericException(Exception ex, WebRequest request) {
        // Log the full exception for debugging
        // logger.error("An unexpected error occurred", ex);

        ResponseDTO<Object> errorResponse = new ResponseDTO<>(
                null,
                "An unexpected error occurred. Please contact support.",
                500
        );

        return new ResponseEntity<>(errorResponse, org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
    }
}