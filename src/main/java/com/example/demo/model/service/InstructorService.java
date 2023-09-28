package com.example.demo.model.service;

import com.example.demo.model.entity.Instructor;
import com.example.demo.model.entity.dto.ChangePasswordRequest;
import com.example.demo.model.entity.dto.CheckPasswordRequest;
import com.example.demo.model.entity.dto.InstructorUpdateRequest;
import com.example.demo.model.repo.InstructorRepo;
import com.example.demo.security.exception.DuplicateResourceException;
import com.example.demo.security.model.ApplicationUserRole;
import com.example.demo.security.utility.JwtUtilityService;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class InstructorService {

    private final InstructorRepo instructorRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilityService jwtService;

    public List<Instructor> getAllInstructors() {
        return instructorRepo.findAll();
    }

    public List<Instructor> search(Optional<String> name) {
        Specification<Instructor> whichName = name.isPresent() ?
                (root, query, cb) -> cb.like(cb.lower(root.get("name")), name.get().toLowerCase().concat("%"))
                : Specification.where(null);

        return instructorRepo.findAll(whichName);
    }

    public List<Instructor> findAllInstructors() {
        return instructorRepo.findAll();
    }

    public Instructor findInstructorById(Long id) {
        return instructorRepo.findById(id).orElseThrow(()
                -> new EntityNotFoundException(String.format("instructor with id [%d] not found", id)));
    }

    private void removeInstructorById(Long id) {
        instructorRepo.deleteById(id);
    }

    public Instructor saveInstructor(Instructor instructor) {
        if (instructor.getRole() == null) {
            instructor.setRole(ApplicationUserRole.INSTRUCTOR);
        }

        if (checkIfEmailAlreadyExists(instructor.getEmail()))
            throw new DuplicateResourceException("email already exists");

        if (checkIfPhoneAlreadyExists(instructor.getPhone()))
            throw new DuplicateResourceException("phone already exists");

        if (null == instructor.getId()) {
            instructor.setPassword(passwordEncoder.encode(instructor.getPassword()));
        }

        return instructorRepo.save(instructor);
    }

    public Instructor loadInstructorByUsername(String username) {
        return instructorRepo.findInstructorByEmail(username).orElseThrow(()
                -> new EntityNotFoundException(String.format("user with email [%s] not found", username)));
    }

    public boolean changePassword(HttpServletRequest httpRequest, ChangePasswordRequest changeRequest) {
        String jwtToken = jwtService.extractTokenFromAuthHeader(httpRequest.getHeader("Authorization"));
        String username = jwtService.extractUsername(jwtToken);

        Long id = loadInstructorByUsername(username).getId();
        Instructor instructor = findInstructorById(id);

        String currentPassword = changeRequest.getCurrentPassword();
        String newPassword = changeRequest.getNewPassword();
        String confirmPassword = changeRequest.getConfirmPassword();

        if (passwordEncoder.matches(currentPassword, instructor.getPassword())) {

            if (newPassword.equals(confirmPassword)) {
                if (currentPassword.equals(newPassword)) {
                    return true;
                }

                instructor.setPassword(passwordEncoder.encode(newPassword));

                instructorRepo.save(instructor);
                return true;
            }
        }

        return false;

    }

    public boolean checkPassword(HttpServletRequest request, CheckPasswordRequest checkPasswordRequest) {
        final String password = checkPasswordRequest.getPassword();
        final String jwtToken = jwtService.extractTokenFromAuthHeader(request.getHeader("Authorization"));
        final String username = jwtService.extractUsername(jwtToken);
        final Instructor instructor = loadInstructorByUsername(username);

        return passwordEncoder.matches(password, instructor.getPassword());
    }

    public boolean checkIfEmailAlreadyExists(String email) {
        return instructorRepo.findInstructorByEmail(email).isPresent();
    }

    public boolean checkIfPhoneAlreadyExists(String phone) {
        return instructorRepo.findInstructorByPhone(phone).isPresent();
    }

    public void removeInstructor(HttpServletRequest request) {
        String jwtToken = jwtService.extractTokenFromAuthHeader(request.getHeader("Authorization"));
        String username=  jwtService.extractUsername(jwtToken);
        Instructor instructor = loadInstructorByUsername(username);

        removeInstructorById(instructor.getId());
    }

    public void removeInstructor(long id) {
        Instructor instructor = findInstructorById(id);
        removeInstructorById(instructor.getId());
    }

    public Instructor update(
            InstructorUpdateRequest instructorUpdateRequest,
            HttpServletRequest request
    ) {
        String jwtToken = jwtService.extractTokenFromAuthHeader(request.getHeader(HttpHeaders.AUTHORIZATION));
        String userEmail = jwtService.extractUsername(jwtToken);

        Instructor oldInstructor = loadInstructorByUsername(userEmail);
        boolean changed = false;

        if (StringUtils.hasLength(instructorUpdateRequest.getName())) {
            oldInstructor.setName(instructorUpdateRequest.getName());
            changed = true;
        }

        if (StringUtils.hasLength(instructorUpdateRequest.getPhone())) {
            if (!instructorUpdateRequest.getPhone().equals(oldInstructor.getPhone())) {
                if (checkIfPhoneAlreadyExists(instructorUpdateRequest.getPhone())) {
                    throw new DuplicateResourceException("phone already exists");
                }

                oldInstructor.setPhone(instructorUpdateRequest.getPhone());
                changed = true;
            }

        }

        if (StringUtils.hasLength(instructorUpdateRequest.getEmail())) {
            if (!instructorUpdateRequest.getEmail().equals(userEmail)) {
                if (checkIfEmailAlreadyExists(instructorUpdateRequest.getEmail())) {
                    throw new DuplicateResourceException("email already exists");
                }

                oldInstructor.setEmail(instructorUpdateRequest.getEmail());
                changed = true;
            }
        }

        if (changed) {
            oldInstructor = instructorRepo.save(oldInstructor);
        }

        return oldInstructor;

    }
}
