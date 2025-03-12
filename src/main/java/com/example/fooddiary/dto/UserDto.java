package com.example.fooddiary.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private String email;
    private Integer age;
    private Double height;
    private Double weight;
    private String gender;
}