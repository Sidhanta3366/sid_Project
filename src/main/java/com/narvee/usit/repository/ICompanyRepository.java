package com.narvee.usit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.narvee.usit.entity.Company;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Long>{

	public Optional<Company> findByCompanyname(String companyname);
	public Optional<Company> findByCompanynameAndCompanyidNot(String companyname, Long companyid);
	

}
