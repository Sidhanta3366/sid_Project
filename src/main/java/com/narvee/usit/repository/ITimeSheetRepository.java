package com.narvee.usit.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.usit.entity.TimeSheet;

@Repository
public interface ITimeSheetRepository extends JpaRepository<TimeSheet, Long>{

}
