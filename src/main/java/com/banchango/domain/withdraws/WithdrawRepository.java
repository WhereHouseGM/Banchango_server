package com.banchango.domain.withdraws;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WithdrawRepository extends JpaRepository<Withdraw, Integer> {
    Optional<Withdraw> findByUserId(Integer userId);
}
