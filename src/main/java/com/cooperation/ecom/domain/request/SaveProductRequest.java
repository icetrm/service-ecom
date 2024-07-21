package com.cooperation.ecom.domain.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SaveProductRequest {
    @Pattern(regexp = "[^<>'!?#$:;{}\\\\\"\\[\\]]*$", message = "error.message.specialCharacter")
    private String title;
    @Pattern(regexp = "[^<>'!?#$:;{}\\\\\"\\[\\]]*$", message = "error.message.specialCharacter")
    private String description;
    @DecimalMin(value = "0", message = "error.message.lowerZero")
    private Double price;
    @DecimalMin(value = "0", message = "error.message.lowerZero")
    private Double discountPercentage;
    @DecimalMin(value = "0", message = "error.message.lowerZero")
    private Double rating;
    @Min(value = 0, message = "error.message.lowerZero")
    private Integer stock;
    @Pattern(regexp = "[^<>'!?#$:;{}\\\\\"\\[\\]]*$", message = "error.message.specialCharacter")
    private String brand;
    @Min(value = 0, message = "error.message.lowerZero")
    private Long categoryID;
}
