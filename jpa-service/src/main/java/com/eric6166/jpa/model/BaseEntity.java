package com.eric6166.jpa.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@FieldDefaults(level = AccessLevel.PROTECTED)
public abstract class BaseEntity<U> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @CreatedBy
    @Column(name = "CREATED_BY")
    U createdBy;

    @CreatedDate
    @Column(name = "CREATED_DATE")
    LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "LAST_MODIFIED_BY")
    U lastModifiedBy;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    LocalDateTime lastModifiedDate;
}
