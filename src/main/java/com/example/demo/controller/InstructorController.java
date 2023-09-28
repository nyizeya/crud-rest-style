package com.example.demo.controller;

import com.example.demo.model.entity.Instructor;
import com.example.demo.model.entity.dto.*;
import com.example.demo.model.entity.dto.mapper.InstructorMapper;
import com.example.demo.model.service.InstructorService;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("instructors")
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;
    private final InstructorMapper instructorMapper;

    @GetMapping
    public ResponseEntity<DataTablesOutput<InstructorDto>> search(@RequestParam Optional<String> name) {
        List<Instructor> instructors = instructorService.search(name);
        long totalInstructorCount = instructorService.search(Optional.empty()).size();

        List<InstructorDto> instructorDtoList = instructors.stream()
                .map(instructorMapper::toDto).collect(Collectors.toList());

        DataTablesOutput<InstructorDto> dataTablesOutput = createDataTableOutput(instructorDtoList, totalInstructorCount);

        return ResponseEntity.ok(dataTablesOutput);
    }

    @GetMapping("{id}")
    public ResponseEntity<DataTablesOutput<InstructorDto>> getOneInstructor(@PathVariable Long id) {
        Instructor instructor = instructorService.findInstructorById(id);
        InstructorDto instructorDto = instructorMapper.toDto(instructor);

        DataTablesOutput<InstructorDto> dataTablesOutput = new DataTablesOutput<>();
        dataTablesOutput.setData(Lists.newArrayList(instructorDto));
        dataTablesOutput.setRecordsTotal(1);
        dataTablesOutput.setRecordsFiltered(1);

        return ResponseEntity.ok(dataTablesOutput);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('instructor:write')")
    public ResponseEntity<DataTablesOutput<InstructorDto>> createInstructor(
            @Valid @RequestBody InstructorRegistrationRequest request,
            BindingResult result
    ) {

        DataTablesOutput<InstructorDto> dataTablesOutput = new DataTablesOutput<>();

        if (result.hasErrors()) {
            dataTablesOutput.setError("invalid instructor creation request");
            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

        Instructor instructor = instructorMapper.toEntity(request);

        instructorService.saveInstructor(instructor);

        InstructorDto instructorDto = instructorMapper.toDto(instructor);

        dataTablesOutput.setData(Lists.newArrayList(instructorDto));
        dataTablesOutput.setRecordsTotal(1);
        dataTablesOutput.setRecordsFiltered(1);

        return new ResponseEntity<>(dataTablesOutput, CREATED);
    }

    @PutMapping("edit")
    public ResponseEntity<DataTablesOutput<InstructorDto>> updateInstructor(
            @Valid @RequestBody InstructorUpdateRequest instructorUpdateRequest,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {

        DataTablesOutput<InstructorDto> dataTablesOutput = new DataTablesOutput<>();

        if (bindingResult.hasErrors()) {
            dataTablesOutput.setError("invalid instructor update request");
            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

        Instructor instructor = instructorService.update(instructorUpdateRequest, request);
        InstructorDto instructorDto = instructorMapper.toDto(instructor);

        dataTablesOutput.setData(Lists.newArrayList(instructorDto));
        dataTablesOutput.setRecordsTotal(1);
        dataTablesOutput.setRecordsFiltered(1);

        return ResponseEntity.ok(dataTablesOutput);
    }

    @PostMapping("change-password")
    public ResponseEntity<DataTablesOutput<String>> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {

        DataTablesOutput<String> dataTablesOutput = new DataTablesOutput<>();

        if (bindingResult.hasErrors()) {
            dataTablesOutput.setError("invalid password change request");
            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

        boolean result = instructorService.changePassword(request, changePasswordRequest);

        if (result) {
            dataTablesOutput.setData(Lists.newArrayList("password change request succeeded"));
            return new ResponseEntity<>(dataTablesOutput, OK);
        } else {
            dataTablesOutput.setError("password change request failed");
            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

    }

    @DeleteMapping("delete")
    public ResponseEntity<?> delete(HttpServletRequest request) {
        instructorService.removeInstructor(request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('instructor:write')")
    public ResponseEntity<?> delete(@PathVariable long id) {
        instructorService.removeInstructor(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("check-password")
    public ResponseEntity<DataTablesOutput<Boolean>> checkPassword(
            @Valid @RequestBody CheckPasswordRequest checkPasswordRequest,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {

        DataTablesOutput<Boolean> dataTablesOutput = new DataTablesOutput<>();

        if (bindingResult.hasErrors()) {
            dataTablesOutput.setError("invalid check password request");
            return new ResponseEntity<>(dataTablesOutput, BAD_REQUEST);
        }

        boolean result = instructorService.checkPassword(request, checkPasswordRequest);

        dataTablesOutput.setData(Lists.newArrayList(result));

        return ResponseEntity.ok(dataTablesOutput);
    }

    private <T> DataTablesOutput<T> createDataTableOutput(List<T> logs, Long totalCount) {
        DataTablesOutput<T> dataTableOutput = new DataTablesOutput<T>();
        dataTableOutput.setRecordsTotal(totalCount);
        dataTableOutput.setRecordsFiltered(logs.size());
        dataTableOutput.setData(logs);
        return dataTableOutput;
    }

}
