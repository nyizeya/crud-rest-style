package com.example.demo.model.entity.dto.mapper;

import com.example.demo.model.entity.Instructor;
import com.example.demo.model.entity.dto.InstructorDto;
import com.example.demo.model.entity.dto.InstructorRegistrationRequest;
import com.example.demo.security.model.ApplicationUserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    InstructorMapper INSTANCE = Mappers.getMapper(InstructorMapper.class);

    Instructor toEntity(InstructorDto instructorDto);

    @Mapping(target = "role", source = "role", qualifiedByName = "toRole")
    InstructorDto toDto(Instructor instructor);

    Instructor toEntity(InstructorRegistrationRequest instructorRegistrationRequest);

    @Named("toRole")
    default ApplicationUserRole toRole(String roleString) {
        return ApplicationUserRole.valueOf(roleString);
    }
}
