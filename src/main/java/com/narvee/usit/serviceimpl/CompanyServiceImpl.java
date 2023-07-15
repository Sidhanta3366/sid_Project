package com.narvee.usit.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.usit.entity.Company;
import com.narvee.usit.repository.ICompanyRepository;
import com.narvee.usit.service.ICompanyService;

@Service
public class CompanyServiceImpl implements ICompanyService{

	@Autowired
	private ICompanyRepository repository;
	
	@Override
	public boolean saveCompany(Company company) {
		Optional<Company> entity = null;
		if(company.getCompanyid() == null) {
			entity = repository.findByCompanyname(company.getCompanyname());
		} else {
			entity = repository.findByCompanynameAndCompanyidNot(company.getCompanyname(), company.getCompanyid());
		}
		
		if(entity.isEmpty()) {
			repository.save(company);
			return true;
		}
			
		return false;
	}

	@Override
	public List<Company> getAllCompany() {
		
		return repository.findAll();
	}

	@Override
	public Company getCompanyByID(Long id) {
		Optional<Company> company = repository.findById(id);
		
		if(company.isPresent()) {
			return company.get();
		}
		return null;
	}

	@Override
	public boolean updateCompany(Company company) {
		Optional<Company> entity = repository.findByCompanynameAndCompanyidNot(company.getCompanyname(), company.getCompanyid());
		
		if(entity.isEmpty()) {
			repository.save(company);
			return true;
		}else {
			return false;
		}
		
	}

	@Override
	public boolean deleteCompanyByID(Long id) {
		repository.deleteById(id);
		return true;
	}
	
}
