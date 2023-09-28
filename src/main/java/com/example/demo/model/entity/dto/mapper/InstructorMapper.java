package com.example.demo.model.entity.dto.mapper;

import com.example.demo.model.entity.Instructor;
import com.example.demo.model.entity.dto.InstructorDto;
import com.example.demo.model.entity.dto.InstructorRegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    InstructorMapper INSTANCE = Mappers.getMapper(InstructorMapper.class);

    Instructor toEntity(InstructorDto instructorDto);

    InstructorDto toDto(Instructor instructor);

    Instructor toEntity(InstructorRegistrationRequest instructorRegistrationRequest);

}
