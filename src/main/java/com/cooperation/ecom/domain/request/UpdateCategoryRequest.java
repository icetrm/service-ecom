package com.cooperation.ecom.domain.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateCategoryRequest {
    @Min(value = 0, message = "error.message.lowerZero")
    private Long id;
    @Pattern(regexp = "[^<>'!?#$:;{}\\\\\"\\[\\]]*$", message = "error.message.specialCharacter")
    private String name;
}
