package com.eric6166.base.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
public class FileExtensionValidator implements ConstraintValidator<ValidFileExtension, MultipartFile> {

    List<String> extensions;

    @Override
    public void initialize(ValidFileExtension constraintAnnotation) {
        extensions = List.of(constraintAnnotation.extensions());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        var extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isBlank(extension)) {
            return false;
        }
        return extensions.contains(extension.toLowerCase());

    }


}