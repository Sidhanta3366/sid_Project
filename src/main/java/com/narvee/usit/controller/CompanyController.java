package com.narvee.usit.controller;

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
import com.narvee.usit.entity.Company;
import com.narvee.usit.service.ICompanyService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RequestMapping("/company")
@RestController
@CrossOrigin
public class CompanyController {

	@Autowired
	private ICompanyService service;
	
	
	@ApiOperation("To Save Company")
	@ApiResponses({ @ApiResponse(code = 200, message = "Company Saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveCompanyDetails(@RequestBody Company company){
		
		boolean saveCompany = service.saveCompany(company);
		if(saveCompany) {
			return new ResponseEntity<>(new RestAPIResponse("success", "saved Company Entity", "Entity Saved"),
					HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(new RestAPIResponse("duplicate", "Record Already Exists", "Not Saved"),
					HttpStatus.OK);
		}
	}
	
	@ApiOperation("To getAll Company Data")
	@ApiResponses({ @ApiResponse(code = 200, message = "getAll Company Data"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "fetchAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> fetchAllCompany(){
			return new ResponseEntity<>(
					new RestAPIResponse("success", "getall TimeSheet Data",service.getAllCompany()),HttpStatus.OK);
	}
	
	@ApiOperation("To get CompanyByID")
	@ApiResponses({ @ApiResponse(code = 200, message = "Company data Fetched"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getCompanyByID(@PathVariable Long id) {
		
		Company companyData = service.getCompanyByID(id);
		if(companyData!=null){
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Company data Fetch SuccessFully", companyData),
				HttpStatus.OK);
	}else {
		return new ResponseEntity<>(new RestAPIResponse("failed","id is not present", null),HttpStatus.BAD_REQUEST);
	   }
	}
	
	@ApiOperation("To update Company")
	@ApiResponses({ @ApiResponse(code = 200, message = "Company Details updated"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	/*public ResponseEntity<RestAPIResponse> updateCompany(@RequestBody Company company){
		Object obj = service.updateCompany(company);
		return new ResponseEntity<>(new RestAPIResponse("success","update TimeSheet successfully", obj), HttpStatus.OK);*/
	public ResponseEntity<RestAPIResponse> updateCompany(@RequestBody Company company){
		
		boolean update = service.updateCompany(company);
		if(update) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Company updated successfully ", "Entity updated"),
					HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(new RestAPIResponse("failed", "Record Already Exists", "Not updated"),
					HttpStatus.OK);
		}
	}
	
	@ApiOperation("To Delete Company")
	@ApiResponses({ @ApiResponse(code = 200, message = "Company Deleted"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteCompanyById(@PathVariable Long id){
		boolean val = service.deleteCompanyByID(id);
		
		if (val == true) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Company Deleted", ""), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new RestAPIResponse("fail", "Company Not Deleted", ""),
					HttpStatus.OK);
		}
	}
}
