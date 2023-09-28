package com.example.demo;

import com.example.demo.model.entity.Course;
import com.example.demo.model.entity.Instructor;
import com.example.demo.model.service.CourseService;
import com.example.demo.model.service.InstructorService;
import com.example.demo.security.model.ApplicationUserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Bean
    CommandLineRunner runner(CourseService courseRepo, InstructorService instructorRepo) {
        return args -> {

            Instructor i1 = Instructor.builder()
                    .name("Min Lwin")
                    .email("minlwin@gmail.com")
                    .password("12345678")
                    .phone("09362856940")
                    .role(ApplicationUserRole.ADMIN)
                    .build();

            Instructor i2 = Instructor.builder()
                    .name("Sayar Kyaw")
                    .email("kyaw@gmail.com")
                    .password("12345678")
                    .phone("03823759483")
                    .build();


            Instructor i3 = Instructor.builder()
                    .name("Zin Ko Winn")
                    .email("zinko@gmail.com")
                    .password("12345678")
                    .phone("01937395908")
                    .build();

            Course c1 = Course.builder()
                    .name("Java Standard Edition")
                    .description("Java For Beginners")
                    .startDate(LocalDate.now())
                    .level(Course.Level.Basic)
                    .instructor(i1)
                    .build();

            Course c2 = Course.builder()
                    .name("Spring Context")
                    .description("Spring For Beginners")
                    .startDate(LocalDate.now())
                    .level(Course.Level.Intermediate)
                    .instructor(i1)
                    .build();

            Course c3 = Course.builder()
                    .name("Securing Rest Api")
                    .description("Securing API With Spring Security")
                    .startDate(LocalDate.now())
                    .level(Course.Level.Advanced)
                    .instructor(i2)
                    .build();

//            i1.setCourses(Arrays.asList(c1, c2));
//            i2.setCourses(Collections.singletonList(c3));
//
//            Arrays.asList(i1, i2, i3).forEach(instructorRepo::saveInstructor);
//            Arrays.asList(c1, c2 ,c3).forEach(courseRepo::saveCourse);

//            instructorRepo.saveAll(Arrays.asList(i1, i2, i3));
//            courseRepo.saveAll(Arrays.asList(c1, c2, c3));

        };
    }

}
