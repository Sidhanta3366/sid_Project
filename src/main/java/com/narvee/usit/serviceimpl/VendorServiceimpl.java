package com.narvee.usit.serviceimpl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.*;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.narvee.usit.dto.VendorAssRecruiterDTO;
import com.narvee.usit.helper.ExcelUploads;
import com.narvee.usit.helper.GetVendor;
import com.narvee.usit.helper.ListInterview;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.ExtractEmail;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.ListVendor;
import com.narvee.usit.repository.IRecruiterRepository;
import com.narvee.usit.repository.IVendorRepository;
import com.narvee.usit.service.IRecruiterService;
import com.narvee.usit.service.IVendorService;

@Service
@Transactional
public class VendorServiceimpl implements IVendorService {
	public static final Logger logger = LoggerFactory.getLogger(VendorServiceimpl.class);

	@Autowired
	IVendorRepository repo;

	@Autowired
	IRecruiterRepository recrepo;

	@Autowired
	private EmailService emailService;

	@Override
	public VendorDetails save(VendorDetails vms) {
		if(vms.getVmsid()==null) {
			emailService.VendorInitiationMail(vms);
		}
		return repo.save(vms);
	}

	@Override
	public List<VendorDetails> getall() {
		return repo.findAll();
	}

	@Override
	public boolean deleteById(long id) {
		logger.info("VmsServiceImpl.deleteById()");
		List<VendorAssRecruiterDTO> recruiterInfo = recrepo.findByVendorVmsid(id);
		if ((recruiterInfo == null || recruiterInfo.isEmpty())) {
			repo.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public VendorDetails getbyId(long id) {
		return repo.findById(id).get();
	}

	@Override
	public List<GetVendor> getvendordetails(String flg) {
		List<GetVendor> all = new ArrayList();
		if (flg.equalsIgnoreCase("all")) {
			all = repo.listvendor();
		}
		if (flg.equalsIgnoreCase("Bench Sales")) {
			all = repo.listvendor("Recruiting");
		}
		if (flg.equalsIgnoreCase("Recruiting")) {
			all = repo.listvendor("Bench Sales");
		}
		return all;
	}

	@Override
	public int approve(String stat, Long id) {
		logger.info("VmsServiceImpl.saveVms()");
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		int save = repo.approveStatus(stat, id, now);
		if (save != 0)
			return 1;
		else
			return 0;
	}

	@Override
	public int changeStatus(String status, Long id, String remarks) {
		logger.info("VmsServiceImpl.changeStatus()");
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		if (status.equalsIgnoreCase("Active")) {
			return repo.toggleStatus(status, id, remarks, now, "Initiated");
		} else {
			return repo.toggleStatus(status, id, remarks, now, "Initiated");
		}
	}

	@Override
	public Page<VendorDetails> findPaginated(int pageNo, int pageSize) {
		logger.info("VmsServiceImpl.findPaginated()");
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<VendorDetails> findAll = repo.findAll(pageable);
		return findAll;
	}

	@Override
	public void saveexcel(MultipartFile file, long id) {
		try {
			List<VendorDetails> excelData = ExcelUploads.convertExcelToVendor(file.getInputStream());
			// System.out.println(excelData);
			List<VendorDetails> newVendor = new ArrayList();
			excelData.forEach(entity -> {
				try {
					Users user = new Users();
					user.setUserid(id);

					List<VendorDetails> vendor = repo.findByVendor(entity.getCompany());
					if (vendor == null || vendor.isEmpty()) {
						entity.setUser(user);
						if (entity.getTyretype() == null) {
							entity.setTyretype("");
						}
						if (entity.getVendortype() == null) {
							entity.setVendortype("");
						}
						if (entity.getCompanytype() == null) {
							entity.setCompanytype("");
						}
						newVendor.add(entity);
					} else {
						newVendor.remove(entity);
					}
				} catch (StringIndexOutOfBoundsException e3) {
				}
			});
			Set s1 = new TreeSet(Comparator.comparing(VendorDetails::getCompany, String.CASE_INSENSITIVE_ORDER));
			s1.addAll(newVendor);
			repo.saveAll(s1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Value("${consultant-access-spadmin}")
	private String spadmin;

	@Value("${consultant-access-admin}")
	private String admin;

	@Value("${consultant-access-manager}")
	private String manager;

	@Value("${consultant-access-teamlead}")
	private String teamlead;

	@Override
	public Page<ListVendor> getvendor(String role, long userid, int pageNo, int pageSize, String field) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		// Pageable pageable = PageRequest.of(pageNo , pageSize);
		// System.out.println(pageNo+"index");

		if (role.equalsIgnoreCase("Employee") || role.equalsIgnoreCase("Team Lead")) {
			if (field.equalsIgnoreCase("empty")) {
				return repo.getvendorForExecutive(userid, pageable);
			} else {
				return repo.getvendorForExecutiveFilter(field, userid, pageable);
			}
		} else {
			if (field.equalsIgnoreCase("empty")) {
				return repo.getvendor(pageable);
			} else {
				return repo.getvendorFilter(field, pageable);
			}
		}

		/*
		 * if (flg.equalsIgnoreCase("presales")) { if
		 * (!role.equalsIgnoreCase("Employee")) { if (role.equalsIgnoreCase(admin) ||
		 * role.equalsIgnoreCase(spadmin)) { return repository.adminRoleList(flg); }
		 * else { return repository.managerRoleList(flg, userid); } } else { return
		 * repository.normalRoleList(flg, userid); } } else { return
		 * repository.adminRoleList(flg); }
		 */
	}

	@Override
	public List<VendorDetails> findByCompanyAndHeadquerter(String company, String location) {
		return repo.findByCompanyAndHeadquerter(company, location);
	}

	@Override
	public int rejectVendor(Long id, String remarks) {
		logger.info("VmsServiceImpl.saveVms()");
		int save = repo.rejectVendor(remarks, id);
		if (save != 0)
			return 1;
		else
			return 0;
	}

	@Override
	public List<VendorDetails> checkDuplicateVendor(String vendorname,Long id) {
		if(id==null || id==0) {
			return repo.findByVendor(vendorname);
		}
		else {
			return repo.findByVendorAndId(vendorname,id);
		}
		
	}

}
