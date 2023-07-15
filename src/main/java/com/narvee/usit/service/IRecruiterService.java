package com.narvee.usit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.dto.VendorAssRecruiterDTO;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.ListRecruiter;
public interface IRecruiterService {
	public Recruiter save(Recruiter vms);

	public List<Recruiter> getall();

	public int rejectRecruiter(Long id, String remarks);

	public boolean deleteById(long id);

	public Recruiter getbyId(long id);

	public List<VendorAssRecruiterDTO> recruiterinfobyVmsid(long id);

	public List<Recruiter> duprecruiter(String email);

	public int changeStatus(String status, Long id, String remarks);

	public int approve(String stat, Long id);

	public void saveexcel(MultipartFile file);

	public Page<ListRecruiter> getallrecruiter(String role, long userid,int pageNo, int pageSize,String field);
	
	public void uploadExcel(MultipartFile file,long userid);
	
}
