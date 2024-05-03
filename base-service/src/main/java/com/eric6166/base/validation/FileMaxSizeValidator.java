package com.eric6166.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class FileMaxSizeValidator implements ConstraintValidator<ValidFileMaxSize, MultipartFile> {

    long maxSize;

    @Override
    public void initialize(ValidFileMaxSize constraintAnnotation) {
        maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file.isEmpty()) {
            return true;
        }
        return maxSize * 1024 * 1024 >= file.getSize();

    }


}