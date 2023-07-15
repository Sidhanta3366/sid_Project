package com.narvee.usit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.dto.RolePrivilegeVO;
import com.narvee.usit.entity.Privilege;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.service.IPrivilegeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/priviliges")
@CrossOrigin
public class RolesPriviliageController {

	@Autowired
	IPrivilegeService privilegeService;
	
	@ApiOperation("To get all Roles")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched all roles"),
			@ApiResponse(code = 404, message = "Entities not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getPrivileges/{roleId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRoles(@PathVariable long roleId) {
		System.out.println("========================="+roleId);
		RolePrivilegeVO privileges = privilegeService.getAllPrivileges(roleId);
		System.out.println("========================="+roleId);
		return new ResponseEntity<>(new RestAPIResponse("Success", "All Roles Fetched", privileges), HttpStatus.OK);
	}
	
	@ApiOperation("To save Previleges")
	@ApiResponses({ @ApiResponse(code = 200, message = "Previleges saved"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/savePrevileges", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> savePrevg(@RequestBody Privilege previleges) {
		System.out.println(previleges);
		privilegeService.savePrevileges(previleges);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Previleges Saved", ""), HttpStatus.CREATED);
	}
}
