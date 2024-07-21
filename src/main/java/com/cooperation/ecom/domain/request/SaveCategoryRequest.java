package com.cooperation.ecom.domain.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveCategoryRequest {
    @Pattern(regexp = "[^<>'!?#$:;{}\\\\\"\\[\\]]*$", message = "error.message.specialCharacter")
    private String name;
}
