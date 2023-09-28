package com.example.demo.model.entity.dto;

import com.example.demo.model.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseUpdateRequest {

    @NotNull(message = "id must not be null")
    private Long id;

    private String name;

    private String description;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate startDate;

    private Course.Level level;

    private Long instructorId;

}
