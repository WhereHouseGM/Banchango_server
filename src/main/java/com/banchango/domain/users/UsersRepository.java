package com.banchango.domain.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);
    Optional<Users> findByEmailAndPassword(String email, String password);
    Optional<Users> findById(Integer id);
}