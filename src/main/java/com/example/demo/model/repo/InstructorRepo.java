package com.example.demo.model.repo;

import com.example.demo.model.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface InstructorRepo extends JpaRepository<Instructor, Long>, JpaSpecificationExecutor<Instructor> {

    Optional<Instructor> findInstructorByEmail(String email);

    Optional<Instructor> findInstructorByPhone(String phone);

}
