package com.eric6166.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileMaxSizeValidator implements ConstraintValidator<ValidFileMaxSize, MultipartFile> {

    long maxSize;

    @Override
    public void initialize(ValidFileMaxSize constraintAnnotation) {
        maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        return maxSize * 1024 * 1024 >= file.getSize();

    }


}