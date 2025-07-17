package com.example.vehicleinspection.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public abstract class BaseDTO implements Serializable {
    private Long id;
    private String createdBy;
    private String lastModifiedBy;
    private Long version;
}