package com.josycom.mayorjay.petapiservice.util

import com.josycom.mayorjay.petapiservice.model.ErrorResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.beans.TypeMismatchException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ResponseExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val errors = mutableListOf<String>()
        ex.bindingResult.fieldErrors.forEach { error ->
            errors.add("${error.field}: ${error.defaultMessage}")
        }
        ex.bindingResult.globalErrors.forEach { error ->
            errors.add("${error.objectName}: ${error.defaultMessage}")
        }
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.localizedMessage, errors)
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request)
    }

    override fun handleTypeMismatch(
        ex: TypeMismatchException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val error = "${("${ex.value.toString()} value for ${ex.propertyName}")} should be of type ${ex.requiredType}"
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.localizedMessage, listOf(error))
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    override fun handleMissingServletRequestPart(
        ex: MissingServletRequestPartException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val error = "${ex.requestPartName} part is missing"
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.localizedMessage, listOf(error))
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    override fun handleMissingServletRequestParameter(
        ex: MissingServletRequestParameterException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val error = "${ex.parameterName} parameter is missing"
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.localizedMessage, listOf(error))
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatch(
        ex: MethodArgumentTypeMismatchException,
        request: WebRequest?
    ): ResponseEntity<Any>? {
        val error = "${ex.name} should be of type ${ex.requiredType?.name}"
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.localizedMessage, listOf(error))
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException, request: WebRequest?): ResponseEntity<Any>? {
        val errors = mutableListOf<String>()
        ex.constraintViolations.forEach { violation ->
            errors.add("${violation.rootBeanClass.name} ${violation.propertyPath}: ${violation.message}")
        }
        val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.localizedMessage, errors)
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }

    override fun handleNoHandlerFoundException(
        ex: NoHandlerFoundException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val error = "No handler found for ${ex.httpMethod} ${ex.requestURL}"
        val errorResponse = ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.localizedMessage, listOf(error))
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    override fun handleHttpRequestMethodNotSupported(
        ex: HttpRequestMethodNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val builder = StringBuilder()
            .append(ex.method)
            .append(" method is not supported for this request. Supported methods are ")
        ex.supportedHttpMethods?.forEach { method -> builder.append("$method ") }
        val errorResponse = ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), ex.localizedMessage, listOf(builder.toString()))
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED)
    }

    override fun handleHttpMediaTypeNotSupported(
        ex: HttpMediaTypeNotSupportedException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val builder = StringBuilder()
            .append(ex.contentType)
            .append(" media type is not supported. Supported media types are ")
        ex.supportedMediaTypes.forEach { mediaType -> builder.append("$mediaType ") }
        val errorResponse =
            ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), ex.localizedMessage, listOf(builder.substring(0, builder.length - 2)))
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

    @ExceptionHandler(IllegalArgumentException::class, Exception::class)
    fun handleOtherExceptions(ex: Exception, request: WebRequest?): ResponseEntity<Any>? {
        val status = if (ex is IllegalArgumentException) HttpStatus.BAD_REQUEST else HttpStatus.INTERNAL_SERVER_ERROR
        val errorResponse = ErrorResponse(status.value(), ex.localizedMessage, listOf(ex.localizedMessage))
        return ResponseEntity<Any>(errorResponse, HttpHeaders(), status)
    }
}