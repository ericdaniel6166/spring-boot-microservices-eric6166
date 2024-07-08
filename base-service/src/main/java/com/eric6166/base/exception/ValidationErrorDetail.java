package com.eric6166.base.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDetail extends ErrorDetail {

    //    @JsonIgnore // uncomment // for local testing
    private String keyField;
    private String field;
    //    @JsonIgnore // uncomment // for local testing
    private String keyObject;
    private String object;
    //    @JsonIgnore // uncomment // for local testing
    private Object rejectedValue;
    private String message;

    public ValidationErrorDetail(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public ValidationErrorDetail(String keyObject, String object, Object rejectedValue, String message) {
        this.keyObject = keyObject;
        this.object = object;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public ValidationErrorDetail(String keyField, String field, String object, Object rejectedValue, String message) {
        this.keyField = keyField;
        this.field = field;
        this.object = object;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
