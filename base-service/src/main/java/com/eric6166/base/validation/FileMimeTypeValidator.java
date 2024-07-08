package com.eric6166.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
public class FileMimeTypeValidator implements ConstraintValidator<ValidFileMimeType, MultipartFile> {

    private final Tika tika;

    private List<String> mimeTypes;

    @Override
    public void initialize(ValidFileMimeType constraintAnnotation) {
        mimeTypes = List.of(constraintAnnotation.mimeTypes());
    }

    @SneakyThrows
    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        var detect = tika.detect(TikaInputStream.get(file.getInputStream()));
        return mimeTypes.contains(detect);

    }


}