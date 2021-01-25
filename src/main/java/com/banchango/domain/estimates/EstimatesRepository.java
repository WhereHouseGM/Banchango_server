package com.banchango.domain.estimates;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface EstimatesRepository extends JpaRepository<Estimates, Integer> {
    List<Estimates> findByUserId(Integer userId);
    <T> List<T> findByStatusOrderByIdAsc(EstimateStatus status, Pageable pageable, Class<T> clazz);
    <T> List<T> findByOrderByIdAsc(Pageable pageable, Class<T> clazz);

    @Modifying
    @Transactional
    @Query("update Estimates e set e.status=:status where e.id=:estimateId")
    void updateStatusById(Integer estimateId, EstimateStatus status);
}
