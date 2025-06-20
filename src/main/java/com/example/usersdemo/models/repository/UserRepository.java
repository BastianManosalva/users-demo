package com.example.usersdemo.models.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.usersdemo.models.entity.User;

public interface UserRepository extends CrudRepository<User, String> {

    User findByEmail(String email);

    boolean existsByEmail(String email);
}
