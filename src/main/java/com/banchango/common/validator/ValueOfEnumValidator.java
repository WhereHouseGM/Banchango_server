package com.banchango.common.validator;

import com.banchango.common.exception.BadRequestException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum constraintAnnotation) {
        acceptedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if(value == null) return true;
        if(!acceptedValues.contains(value.toString())) {
            throw new BadRequestException();
        } else {
            return true;
        }
    }
}
