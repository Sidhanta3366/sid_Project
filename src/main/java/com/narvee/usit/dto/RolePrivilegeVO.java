package com.narvee.usit.dto;

import java.util.List;

import com.narvee.usit.vo.DropdownVO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RolePrivilegeVO {
	private List<DropdownVO> 			roleNames;
	private List<DropdownVO> 			vendor;
	private List<DropdownVO> 			recruiter;
	private List<DropdownVO> 			tech_support;
	private List<DropdownVO> 			consultant;
	private List<DropdownVO> 			visa;
	private List<DropdownVO> 			configuration;
	private List<DropdownVO> 			qualification;
	private List<DropdownVO> 			technology_tags; 

}
