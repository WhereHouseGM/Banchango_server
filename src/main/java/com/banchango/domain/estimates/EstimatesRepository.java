package com.banchango.domain.estimates;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimatesRepository extends JpaRepository<Estimates, Integer> {
    List<Estimates> findByUserId(Integer userId);

    List<Estimates> findByStatusOrderByIdDesc(EstimateStatus status, Pageable pageable);

    List<Estimates> findByOrderByIdDesc(Pageable pageable);
}
