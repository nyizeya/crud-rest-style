package com.example.demo.model.repo;

import com.example.demo.model.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepo extends JpaRepository<Token, Long> {

    @Query(
            "select t from Token t inner join Instructor i on t.instructor.id = i.id " +
                    "where i.id = :id and (t.expired = false or t.revoked = false)"
    )
    List<Token> findAllValidTokenUser(@Param("id") Long id);

    Optional<Token> findByToken(String token);

}
