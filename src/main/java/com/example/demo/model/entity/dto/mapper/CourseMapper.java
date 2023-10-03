package com.example.demo.model.entity.dto.mapper;

import com.example.demo.model.entity.Course;
import com.example.demo.model.entity.dto.CourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(source = "instructorId", target = "instructor.id")
    Course toEntity(CourseDto courseDto);

    @Mapping(source = "instructor.name", target = "instructorName")
    @Mapping(source = "instructor.id", target = "instructorId")
    CourseDto toDto(Course course);

    List<CourseDto> toDtoList(List<Course> entityList);
    List<Course> toEntityList(List<CourseDto> dtoList);

}
