package com.example.demo.model.service;

import com.example.demo.model.entity.Token;
import com.example.demo.model.repo.TokenRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepo repo;

    public List<Token> findAllValidTokenByUser(Long id) {
        return repo.findAllValidTokenUser(id);
    }

    public Optional<Token> findByToken(String token) {
        return repo.findByToken(token);
    }

    public List<Token> saveAllToken(Collection<Token> tokens) {
        return repo.saveAll(tokens);
    }

    public Token saveToken(Token token) {
        return repo.save(token);
    }

}
