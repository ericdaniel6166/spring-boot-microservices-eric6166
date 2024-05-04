package com.eric6166.base.validation;

import com.eric6166.base.dto.FileEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
public class FileMimeTypeValidator implements ConstraintValidator<ValidFileMimeType, MultipartFile> {

    final Tika tika;
    final Detector detector;

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