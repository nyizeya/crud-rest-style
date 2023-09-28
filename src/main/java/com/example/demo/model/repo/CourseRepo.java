package com.example.demo.model.repo;

import com.example.demo.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {

    @Query(
            "select c from Course c JOIN Instructor i ON c.instructor.id = i.id WHERE c.id = :id"
    )
    Optional<Course> findCourseById(@Param("id") Long id);

}
