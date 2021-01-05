package com.banchango.domain.estimates;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstimatesRepository extends JpaRepository<Estimates, Integer> {

}
