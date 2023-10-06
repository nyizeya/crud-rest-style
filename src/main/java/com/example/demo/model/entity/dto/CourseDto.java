package com.example.demo.model.entity.dto;

import com.example.demo.model.entity.Course;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private Long id;

    @NotEmpty(message = "Name must not be empty")
    private String name;

    @NotEmpty(message = "Description must not be empty")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please select start date")
    private LocalDate startDate;

    @NotNull(message = "Please select a level")
    private Course.Level level;

    @NotNull(message = "Please select a instructor")
    private Long instructorId;

    private String instructorName;
}
