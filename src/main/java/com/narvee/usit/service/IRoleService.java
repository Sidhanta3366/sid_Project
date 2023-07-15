package com.narvee.usit.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.usit.helper.GetRoles;
import com.narvee.usit.entity.Roles;

public interface IRoleService {
	public boolean saveRole(Roles roles);

	public boolean updateRole(Roles role);

	public List<Long> finabyrolenumber(long rolename);

	public List<Roles> getAllRoles();
	
	public Page<Roles> findProductWithPagination(int offset,int pageSize);

	public Roles getRole(Long id);

	public boolean deleteRole(Long id);

	public int changeStatus(String status, Long id, String remarks);

	public List<GetRoles> getRoles();
}
