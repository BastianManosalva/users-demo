package com.example.usersdemo.models.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.example.usersdemo.models.entity.User;

public interface UserRepository extends CrudRepository<User, UUID> {

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
