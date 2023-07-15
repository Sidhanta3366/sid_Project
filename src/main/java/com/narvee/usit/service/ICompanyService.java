package com.narvee.usit.service;

import java.util.List;

import com.narvee.usit.entity.Company;
public interface ICompanyService {

public boolean saveCompany (Company company);
	
	public List<Company> getAllCompany();
	
	public Company getCompanyByID(Long id);
	
	public boolean updateCompany(Company company);
	
	public boolean deleteCompanyByID(Long id);
}
