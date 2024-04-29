package com.eric6166.common.exception;

import com.eric6166.base.exception.ErrorDetail;
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

    public ValidationErrorDetail(String field, String object, String message) {
        this.field = field;
        this.message = message;
        this.object = object;
    }

    public ValidationErrorDetail(String field, String object, Object rejectedValue, String message) {
        this.field = field;
        this.object = object;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
