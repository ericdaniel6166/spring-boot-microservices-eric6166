package com.eric6166.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
public class FileMimeTypeValidator implements ConstraintValidator<ValidFileMimeType, MultipartFile> {

    final Tika tika;

    List<String> mimeTypes;

    @Override
    public void initialize(ValidFileMimeType constraintAnnotation) {
        mimeTypes = List.of(constraintAnnotation.mimeTypes());
    }

    @SneakyThrows
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file.isEmpty()) {
            return true;
        }
        return mimeTypes.contains(tika.detect(file.getInputStream()));

    }


}