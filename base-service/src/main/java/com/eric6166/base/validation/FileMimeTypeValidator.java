package com.eric6166.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.tika.Tika;
import org.apache.tika.io.TikaInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileMimeTypeValidator implements ConstraintValidator<ValidFileMimeType, MultipartFile> {

    private final Tika tika;

    List<String> mimeTypes;

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
        String detect = tika.detect(TikaInputStream.get(file.getInputStream()));
        return mimeTypes.contains(detect);

    }


}