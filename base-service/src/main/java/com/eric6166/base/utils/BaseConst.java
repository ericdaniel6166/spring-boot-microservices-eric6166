package com.eric6166.base.utils;

public final class BaseConst {
    public static final String REQUEST_PARAM_LANGUAGE = "lang";
    public static final int MAXIMUM_PAGE_SIZE = 200;
    public static final int MAXIMUM_CREATE_UPDATE_MULTI_ITEM = 10;
    public static final int MAXIMUM_BIG_DECIMAL_INTEGER = 19;
    public static final int MAXIMUM_BIG_DECIMAL_FRACTION = 4;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_PAGE_SIZE_STRING = "10";
    public static final int MAXIMUM_SORT_COLUMN = 10;
    public static final int INTEGER_ONE = 1;
    public static final int DEFAULT_PAGE_NUMBER = 1;
    public static final String DEFAULT_PAGE_NUMBER_STRING = "1";
    public static final int DEFAULT_SIZE_MAX_STRING = 255;
    public static final int SIZE_MAX_STRING = 60000;
    public static final long DEFAULT_MAX_LONG = Long.MAX_VALUE; //change
    public static final int DEFAULT_MAX_INTEGER = Integer.MAX_VALUE; //change
    public static final String PLACEHOLDER_0 = "{0}";
    public static final String COMMON = "common";
    public static final String ID = "id";
    public static final String DEFAULT_SORT_COLUMN = "id";
    public static final String DEFAULT_SORT_DIRECTION = "ASC";
    public static final String GENERAL_FIELD = "generalField";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";
    //change
    public static final String SPLIT_REGEX_DOT = "\\.";
    public static final String FIELD_ERROR = "error";
    //document
    public static final String MIME_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String MIME_TYPE_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static final String MIME_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String MIME_TYPE_DOC = "application/msword";
    public static final String MIME_TYPE_XLS = "application/vnd.ms-excel";
    public static final String MIME_TYPE_PPT = "application/vnd.ms-powerpoint";
    public static final String MIME_TYPE_PDF = "application/pdf";
    public static final String MIME_TYPE_ZIP = "application/zip";
    //audio
    public static final String MIME_TYPE_WAV = "audio/vnd.wave";
    public static final String MIME_TYPE_MP3 = "audio/audio/mpeg";
    //image
    public static final String MIME_TYPE_TIFF = "image/tiff";
    public static final String MIME_TYPE_SVG = "image/svg+xml";
    public static final String MIME_TYPE_GIF = "image/gif";
    public static final String MIME_TYPE_BMP = "image/bmp";
    public static final String MIME_TYPE_PNG = "image/png";
    public static final String MIME_TYPE_JPG = "image/jpeg";
    //other
    public static final String MIME_TYPE_XML = "application/xml";
    public static final String MIME_TYPE_TXT = "text/plain";
    public static final String MIME_TYPE_HTML = "text/html";

    //extension
    public static final String EXTENSION_PDF = "pdf";
    public static final String EXTENSION_DOCX = "docx";
    public static final String EXTENSION_DOC = "doc";

    private BaseConst() {
        throw new IllegalStateException("Utility class");
    }
}
