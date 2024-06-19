package com.eric6166.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileMaxSizeValidator implements ConstraintValidator<ValidFileMaxSize, MultipartFile> {

    long maxSizeInBytes;

    @Override
    public void initialize(ValidFileMaxSize constraintAnnotation) {
        maxSizeInBytes = constraintAnnotation.maxSize() * 1024 * 1024;
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        return file == null || file.isEmpty() || file.getSize() <= maxSizeInBytes;
    }


}