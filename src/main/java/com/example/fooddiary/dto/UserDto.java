package com.example.fooddiary.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String username;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Неверный формат email")
    private String email;

    @NotNull(message = "Возраст не может быть null")
    @Positive(message = "Возраст должен быть положительным числом")
    private Integer age;

    @NotNull(message = "Рост не может быть null")
    @Positive(message = "Рост должен быть положительным числом")
    @Max(value = 300, message = "Рост не может быть более 300см")
    private Double height;

    @NotNull(message = "Вес не может быть null")
    @Positive(message = "Вес должен быть положительным числом")
    @Max(value = 700, message = "Вес не может быть более 700кг")
    private Double weight;

    @NotBlank(message = "Пол не может быть пустым")
    @Pattern(regexp = "[MW]", message = "Пол должен быть либо 'M' (мужской), либо 'W' (женский)")
    private String gender;
}