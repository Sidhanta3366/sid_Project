package com.narvee.usit.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.usit.dto.RolePrivilegeVO;
import com.narvee.usit.entity.Privilege;
import com.narvee.usit.entity.Privilege.privilegeType;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.repository.IRolesRepository;
import com.narvee.usit.repository.PrivilegeRepository;
import com.narvee.usit.service.IPrivilegeService;
import com.narvee.usit.vo.DropdownVO;
@Service
public class PrivilegeSeviceImpl implements IPrivilegeService {

	@Autowired
	private PrivilegeRepository privRepo;

	@Autowired
	private IRolesRepository roleRepo;

	@Override
	public RolePrivilegeVO getAllPrivileges(Long userId) {
		RolePrivilegeVO privilegeVO = new RolePrivilegeVO();
		
		List<DropdownVO> roleNames = new ArrayList<DropdownVO>();
		List<DropdownVO> vendorPrivileges = new ArrayList<DropdownVO>();
		List<DropdownVO> recruiterPrivileges = new ArrayList<DropdownVO>();
		List<DropdownVO> tech_supportPrivileges = new ArrayList<DropdownVO>();
		List<DropdownVO> consultantPrivileges = new ArrayList<DropdownVO>();
		List<DropdownVO> visaPrivileges = new ArrayList<DropdownVO>();
		List<DropdownVO> qualificationPrivileges = new ArrayList<DropdownVO>();
		List<DropdownVO> technology_tagsPrivileges = new ArrayList<DropdownVO>();
		List<Privilege> privileges = privRepo.findAll();
		
		//System.out.println("Total priviliges size:"+privileges.size());
		for (Privilege singlePrivilege : privileges) {
			if (singlePrivilege.getType().equalsIgnoreCase(
					com.narvee.usit.entity.Privilege.privilegeType.VENDOR.name())) {
				vendorPrivileges.add(new DropdownVO(singlePrivilege
						.getId(), singlePrivilege.getName()));
				privilegeVO.setVendor(vendorPrivileges);
			}
			if (singlePrivilege.getType().equalsIgnoreCase(
					com.narvee.usit.entity.Privilege.privilegeType.RECRUITER.name())) {
				recruiterPrivileges.add(new DropdownVO(singlePrivilege
						.getId(), singlePrivilege.getName()));
				privilegeVO.setRecruiter(recruiterPrivileges);
			}
			if (singlePrivilege.getType().equalsIgnoreCase(
					com.narvee.usit.entity.Privilege.privilegeType.TECH_SUPPORT.name())) {
				tech_supportPrivileges.add(new DropdownVO(
						singlePrivilege.getId(), singlePrivilege.getName()));
				privilegeVO.setTech_support(tech_supportPrivileges);
			}
			if (singlePrivilege.getType().equalsIgnoreCase(
					com.narvee.usit.entity.Privilege.privilegeType.CONSULTANT.name())) {
				consultantPrivileges.add(new DropdownVO(singlePrivilege.getId(),
						singlePrivilege.getName()));
				privilegeVO.setConsultant(consultantPrivileges);
			}
			if (singlePrivilege.getType().equalsIgnoreCase(
					com.narvee.usit.entity.Privilege.privilegeType.VISA.name())) {
				visaPrivileges.add(new DropdownVO(singlePrivilege
						.getId(), singlePrivilege.getName()));
				privilegeVO.setVisa(visaPrivileges);
			}
			if (singlePrivilege.getType().equalsIgnoreCase(
					com.narvee.usit.entity.Privilege.privilegeType.QUALIFICATION.name())) {
				qualificationPrivileges.add(new DropdownVO(singlePrivilege.getId(),
						singlePrivilege.getName()));
				privilegeVO.setQualification(qualificationPrivileges);
			}
			if (singlePrivilege.getType().equalsIgnoreCase(
					com.narvee.usit.entity.Privilege.privilegeType.TECHNOLOGY_TAGS.name())) {
				technology_tagsPrivileges.add(new DropdownVO(singlePrivilege.getId(),
						singlePrivilege.getName()));
				privilegeVO.setTechnology_tags(technology_tagsPrivileges);
			}
		}
//		List<Roles> allRoles = roleRepo.findAll();
//		for (Roles role : allRoles) {
//			if (userId > role.getRoleid() || userId == 11) {
//				roleNames.add(new DropdownVO(role.getRoleid(), role.getRolename()));
//			}
//		}
//		privilegeVO.setRoleNames(roleNames);
		return privilegeVO;
	}

	@Override
	public Privilege savePrevileges(Privilege privilege) {
		 return privRepo.save(privilege);
	}

}
