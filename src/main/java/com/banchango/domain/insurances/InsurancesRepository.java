package com.banchango.domain.insurances;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InsurancesRepository extends JpaRepository<Insurances, Integer> {

    void deleteByInsuranceId(Integer insuranceId);
}
