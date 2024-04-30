package com.eric6166.common.exception;

import com.eric6166.base.exception.ErrorDetail;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ValidationErrorDetail extends ErrorDetail {

    //    @JsonIgnore // uncomment // for local testing
    String keyField;
    String field;
    //    @JsonIgnore // uncomment // for local testing
    String keyObject;
    String object;
    //    @JsonIgnore // uncomment // for local testing
    Object rejectedValue;
    String message;

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
