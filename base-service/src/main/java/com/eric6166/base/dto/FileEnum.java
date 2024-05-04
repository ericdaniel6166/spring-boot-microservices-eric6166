package com.eric6166.base.dto;

import com.eric6166.base.utils.BaseConst;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum FileEnum {
    PDF(BaseConst.EXTENSION_PDF, BaseConst.MIME_TYPE_PDF),
    DOC(BaseConst.EXTENSION_DOC, BaseConst.MIME_TYPE_DOC),
    DOCX(BaseConst.EXTENSION_DOCX, BaseConst.MIME_TYPE_DOCX),
    ;

    String extension;
    String mimeType;


}
