package com.coursecatalog.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CourseDTO {
    @NotBlank(message = "Tên khoá học không được bỏ trống!")
    private String name;
    @NotBlank(message = "Mô tả khoá học không được bỏ trống!")
    private String description;
    @Min(value = 1,message = "Giá tiền phải lớn hơn 0!")
    private Double price;
    private String imageUrl;
}
