package com.example.demo.controller;

import com.example.demo.model.entity.Course;
import com.example.demo.model.entity.dto.CourseDto;
import com.example.demo.model.entity.dto.CourseUpdateRequest;
import com.example.demo.model.entity.dto.DataTablesOutput;
import com.example.demo.model.entity.dto.mapper.CourseMapper;
import com.example.demo.model.service.CourseService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping({"/", "/courses"})
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping
    public ResponseEntity<DataTablesOutput<CourseDto>> search(
                @RequestParam Optional<String> name,
                @RequestParam Optional<Course.Level> level,
                @RequestParam Optional<Integer> pageNumber,
                @RequestParam Optional<Integer> size
            ) {

        int currentPage = pageNumber.filter(a -> a > 0).orElse(1);
        int pageSize = size.orElse(5);

        long totalCourseElements = courseService.search(
                Optional.empty(), Optional.empty(), PageRequest.of(currentPage - 1, pageSize)
        ).getTotalElements();

        Page<CourseDto> filteredCoursePage = courseService.search(
                name, level, PageRequest.of(currentPage - 1, pageSize));

        DataTablesOutput<CourseDto> dataTablesOutput = createDataTableOutput(
                filteredCoursePage, totalCourseElements);

        return ResponseEntity.ok(dataTablesOutput);
    }

    @GetMapping("{id}")
    public ResponseEntity<DataTablesOutput<CourseDto>> getOneCourse(@PathVariable long id) {
        CourseDto dto = courseMapper.toDto(courseService.findCourseById(id));

        DataTablesOutput<CourseDto> dataTablesOutput = DataTablesOutput.<CourseDto>builder()
                .data(Lists.newArrayList(dto))
                .recordsTotal(1)
                .recordsFiltered(1)
                .build();

        return ResponseEntity.ok(dataTablesOutput);
    }

    @PutMapping("update")
    public ResponseEntity<DataTablesOutput<CourseDto>> updateCourse(
            @Valid @RequestBody CourseUpdateRequest request,
            BindingResult bindingResult
    ) {

        DataTablesOutput<CourseDto> dataTablesOutput = new DataTablesOutput<>();

        if (bindingResult.hasErrors()) {
            dataTablesOutput.setError("invalid update course request");
            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

        Course course = courseService.updateCourse(request);
        CourseDto courseDto = courseMapper.toDto(course);

        dataTablesOutput.setData(Lists.newArrayList(courseDto));
        dataTablesOutput.setRecordsTotal(1);
        dataTablesOutput.setRecordsFiltered(1);


        return ResponseEntity.ok(dataTablesOutput);
    }

    @PostMapping
    public ResponseEntity<DataTablesOutput<CourseDto>> createCourse(
            @Valid @RequestBody CourseDto courseDto,
            BindingResult bindingResult
    ) {

        DataTablesOutput<CourseDto> dataTablesOutput = new DataTablesOutput<>();

        if (bindingResult.hasErrors()) {
            dataTablesOutput.setError("invalid course creation request");
            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

        Course course = courseService.saveCourse(courseMapper.toEntity(courseDto));
        CourseDto dto = courseMapper.toDto(course);
        dataTablesOutput.setData(Lists.newArrayList(dto));
        dataTablesOutput.setRecordsTotal(1);
        dataTablesOutput.setRecordsFiltered(1);

        return ResponseEntity.ok(dataTablesOutput);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    private <T> DataTablesOutput<T> createDataTableOutput(Page<T> logs, Long totalCount) {
        DataTablesOutput<T> dataTableOutput = new DataTablesOutput<T>();
        dataTableOutput.setRecordsTotal(totalCount);
        dataTableOutput.setRecordsFiltered(logs.getTotalElements());
        dataTableOutput.setData(logs.getContent());
        return dataTableOutput;
    }

}
