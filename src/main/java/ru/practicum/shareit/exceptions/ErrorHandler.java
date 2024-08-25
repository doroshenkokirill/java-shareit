package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse notFoundExceptionHandle(final NotUniqueEmailException e) {
        log.error("error 409: {}: {}.", e.getClass().getSimpleName(), e.getMessage());
        return new ExceptionResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValidException(final MethodArgumentNotValidException  e) {
        log.error("error 400: {}: {}.", e.getClass().getSimpleName(), e.getMessage());
        return new ExceptionResponse("Неверный запрос", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse notFoundHandle(final NotFoundException e) {
        log.error("error 404: {}: {}.", e.getClass().getSimpleName(), e.getMessage());
        return new ExceptionResponse("Объект не найден", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleWrongOwnerException(final WrongOwnerException e) {
        log.error("error 403: {}: {}.", e.getClass().getSimpleName(), e.getMessage());
        return new ExceptionResponse("Недостаточно прав", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse exceptionHandle(final Exception e) {
        log.error("error 500: {}: {}.", e.getClass().getSimpleName(), e.getMessage());
        return new ExceptionResponse("Ошибка сервера", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBookingException(final BookingException e) {
        log.error("error 400: {}: {}.", e.getClass().getSimpleName(), e.getMessage());
        return new ExceptionResponse("Ошибка при бронировании", e.getMessage());
    }
}
