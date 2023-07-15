package com.narvee.usit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.usit.entity.TimeSheetDescription;

@Repository
public interface ITimeSheetDescriptionRepository extends JpaRepository<TimeSheetDescription, Long>{

}
