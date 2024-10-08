package ru.practicum.shareit.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookingDtoRequest> {
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingDtoRequest bookingDto, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}