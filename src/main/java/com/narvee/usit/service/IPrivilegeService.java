package com.narvee.usit.service;

import java.util.List;

import com.narvee.usit.dto.RolePrivilegeVO;
import com.narvee.usit.entity.Privilege;

public interface IPrivilegeService {
	
	public RolePrivilegeVO getAllPrivileges(Long id);
	
	public Privilege savePrevileges(Privilege privilege);

}
