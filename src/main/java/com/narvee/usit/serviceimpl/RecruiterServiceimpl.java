package com.narvee.usit.serviceimpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.dto.RecruiterDTO;
import com.narvee.usit.dto.VendorAssRecruiterDTO;
import com.narvee.usit.entity.PhoneNumberFormat;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.ExcelUploads;
import com.narvee.usit.helper.ListRecruiter;
import com.narvee.usit.repository.IPhoneNumber;
import com.narvee.usit.repository.IRecruiterRepository;
import com.narvee.usit.repository.IVendorRepository;
import com.narvee.usit.service.IRecruiterService;
import com.narvee.usit.service.IVendorService;

@Service
@Transactional
public class RecruiterServiceimpl implements IRecruiterService {
	public static final Logger logger = LoggerFactory.getLogger(RecruiterServiceimpl.class);

	@Autowired
	IRecruiterRepository repo;

	@Autowired
	IVendorRepository vendorrepo;

	// @Autowired
	// IExcelRepository execrepo;

	@Override
	public Recruiter save(Recruiter vms) {
		logger.info("RecruiterServiceimpl.save()");
		System.out.println(vms);
		return repo.saveAndFlush(vms); // repo.save(vms);
	}

	@Override
	public List<Recruiter> getall() {
		logger.info("RecruiterServiceimpl.getall()");
		return repo.findAll();// repo.getall();
	}

	@Override
	public boolean deleteById(long id) {
		logger.info("VmsServiceImpl.deleteById()");
		repo.deleteById(id);
		return true;
	}

	@Override
	public Recruiter getbyId(long id) {
		logger.info("RecruiterServiceimpl.getbyId()");
		return repo.findById(id).get();
	}

	@Override
	public List<VendorAssRecruiterDTO> recruiterinfobyVmsid(long id) {
		logger.info("RecruiterServiceimpl.recruiterinfobyVmsid()");
		return repo.findByVendorVmsid(id);
	}

	@Override
	public List<Recruiter> duprecruiter(String email) {
		// TODO Auto-generated method stub
		return repo.findByEmail(email);
	}

	@Override
	public int changeStatus(String status, Long id, String remarks) {
		logger.info("VmsServiceImpl.changeStatus()");
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		
		if(status.equalsIgnoreCase("Active")) {
			return repo.toggleStatus(status, id, remarks,now,"Initiated");
		}
		else {
			return repo.toggleStatus(status, id, remarks,now,"Initiated");
		}
		
	}

	@Override
	public int approve(String stat, Long id) {
		logger.info("VmsServiceImpl.saveVms()");
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		
		int save = repo.approveStatus(stat, id,now);
		if (save != 0)
			return 1;
		else
			return 0;
	}
	
	

	@Override
	public void saveexcel(MultipartFile file) {
		/*
		 * try { //List<Recruiter> listOfexcel =
		 * Helper.convertExcelToListOfRecruiter(file.getInputStream());
		 * //System.out.println(listOfexcel); //saveintoDB(listOfexcel); } catch
		 * (IOException e) { e.printStackTrace(); }
		 */
	}

	public void saveintoDB(List<Recruiter> listOfexcel) {
		VendorDetails vendor = null;
		Long vmsid = null;
		Long stateid = null;
		Users user = new Users();
		for (ListIterator<Recruiter> it = listOfexcel.listIterator(); it.hasNext();) {
			Recruiter value = it.next();
			vendor = new VendorDetails();
			if (value.getVendor() != null) {
				VendorDetails vv = vendorrepo.findByCompany(value.getVendor().getCompany());
				if (vv == null) {
					vendor.setCompany(value.getVendor().getCompany());

				} else {
					vmsid = vv.getVmsid();
					vendor.setVmsid(vmsid);
					value.setVendor(vendor);
				}
			}

			user.setUserid(1);
			value.setUser(user);

			try {
				List<Recruiter> findbymail = repo.findByEmail(value.getEmail());
				if (findbymail == null) {
				} else {
					it.remove();
				}
			} catch (NullPointerException e) {
			}

		} // main forloop

		this.repo.saveAll(listOfexcel);

	}

	/*
	 * @Override public ExceRec save(ExceRec vms) { // TODO Auto-generated method
	 * stub return execrepo.saveAndFlush(vms); }
	 * 
	 * @Override public List<ExceRec> duprecru(String email) { // TODO
	 * Auto-generated method stub return execrepo.findByEmail(email); }
	 */

	@Override
	public Page<ListRecruiter> getallrecruiter(String role, long userid, int pageNo, int pageSize, String field) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		if (role.equalsIgnoreCase("Employee") || role.equalsIgnoreCase("Team Lead")) {
			if (field.equalsIgnoreCase("empty")) {
				return repo.getallrecruiterById(userid, pageable);
			} else {
				return repo.getallrecruiterByIdPagination(field, userid, pageable);
			}
		} else {

			if (field.equalsIgnoreCase("empty")) {
				return repo.getallrecruiter(pageable);
			} else {
				return repo.getallrecruiterPagination(field, pageable);
			}
		}
	}

	@Override
	public int rejectRecruiter(Long id, String remarks) {
		logger.info("VmsServiceImpl.saveVms()");
		int save = repo.rejectRecruiter(remarks, id);
		if (save != 0)
			return 1;
		else
			return 0;
	}

	@Override
	public void uploadExcel(MultipartFile file,long userid) {
		try {
			List<RecruiterDTO> excelData = ExcelUploads.convertExcelToListOfVendorData(file.getInputStream());
			excelData.forEach(entity -> {
				try {
					String recruitertype = "Recruiter";
					String vendorType = "Recruiting";
					String value = entity.getEmail();
					String company = entity.getCompany();
					try {
//						if (entity.getType().equalsIgnoreCase("sales")) {
//							recruitertype = "Bench Sales";
//							vendorType = "Bench Sales";
//						} else if (entity.getType().equalsIgnoreCase("both")) {
//							recruitertype = "Both";
//							vendorType = "Both";
//						} else {
							recruitertype = "Recruiter";
							vendorType = "Recruiting";
						//}
						// String result = value.substring(value.indexOf("@") + 1, value.indexOf("."));
						// if(vendorrepo.existsByCompany(company)) {
							//System.out.println("if condition company already exists===="+company);
						 //}
						// else {
							//System.out.println("else  condition company not exists---"+company);
						// }
						 
						List<VendorDetails> vendor = vendorrepo.findByCompanyLike(company);
						if (vendor == null || vendor.isEmpty()) {
							Recruiter recruiter = new Recruiter();
							VendorDetails Vendorentity2 = new VendorDetails();
							Vendorentity2.setCompany(company);
							Users user = new Users();
							user.setUserid(userid);
							Vendorentity2.setUser(user);
							Vendorentity2.setCompanytype(vendorType);
							//Vendorentity2.setVms_stat("Approved");
							VendorDetails vendorEntity2 = vendorrepo.save(Vendorentity2);
							VendorDetails vdr = new VendorDetails();
							vdr.setVmsid(vendorEntity2.getVmsid());
							recruiter.setVendor(vdr);
							recruiter.setEmail(value);
							recruiter.setRecruiter(entity.getName());
							recruiter.setUsnumber(entity.getContact_Number());
							recruiter.setRecruitertype(recruitertype);
							// extension
							recruiter.setExtension(entity.getExt());
							recruiter.setUser(user);
							repo.save(recruiter);
						}
						else {
							Recruiter recruiter = new Recruiter();
							VendorDetails Vendorentity = new VendorDetails();
							Users user = new Users();
							user.setUserid(userid);
							vendor.forEach(vendorEntity -> {
								Vendorentity.setVmsid(vendorEntity.getVmsid());
							});
							List<Recruiter> recru = repo.findByEmail(value);
							if (recru == null || recru.isEmpty()) {
								recruiter.setVendor(Vendorentity);
								recruiter.setEmail(value);
								recruiter.setRecruitertype(recruitertype);
								recruiter.setRecruiter(entity.getName());
								recruiter.setUsnumber(entity.getContact_Number());
								recruiter.setExtension(entity.getExt());
								recruiter.setUser(user);
								repo.save(recruiter);
							} 
//							else {
//								
//								recru.setExtension(entity.getExt());
//								recru.setRecruiter(entity.getName());
//								recru.setUsnumber(entity.getContact_Number());
//								repo.save(recru);
//							}
						}
					} catch (NullPointerException e) {
					}

				} catch (StringIndexOutOfBoundsException e3) {
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
