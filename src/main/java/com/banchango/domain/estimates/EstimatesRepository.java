package com.banchango.domain.estimates;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimatesRepository extends JpaRepository<Estimates, Integer> {
    List<Estimates> findByUserId(Integer userId);

    List<Estimates> findByStatusOrderByCreatedAtDesc(EstimateStatus status, PageRequest pageRequest);

    List<Estimates> findByOrderByCreatedAtDesc(PageRequest pageRequest);
}
