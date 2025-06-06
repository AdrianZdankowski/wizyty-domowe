package com.medical.homevisits.appointments.paramedic.repository;

import com.medical.homevisits.appointments.paramedic.entity.Paramedic;

import com.medical.homevisits.appointments.workplace.entity.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParamedicRepository extends JpaRepository<Paramedic, UUID> {
	 List<Paramedic> findByWorkPlace(Workplace workplace);
}

