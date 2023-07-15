package com.narvee.usit.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.usit.entity.Privilege;

public interface PrivilegeRepository extends JpaRepository<Privilege, Serializable>{

}
