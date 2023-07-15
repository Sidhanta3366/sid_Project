package com.narvee.usit.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.TimeSheet;
import com.narvee.usit.repository.ITimeSheetRepository;
import com.narvee.usit.service.ITimeSheetService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/timesheet")
@CrossOrigin
public class TimeSheetController {

	@Autowired
	private ITimeSheetService service;
	
	@Autowired
	private ITimeSheetRepository repository;
	
	@ApiOperation("To Save TimeSheet")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheet Saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveTimeSheet(@RequestBody TimeSheet timeSheet){
		
		Object obj = service.saveTimeSheet(timeSheet);
		return new ResponseEntity<>(new RestAPIResponse("success", "Save TimeSheet Successfully", obj),
				HttpStatus.CREATED);
	}
	
	@ApiOperation("To getAll TimeSheet Data")
	@ApiResponses({ @ApiResponse(code = 200, message = "getAll TimeSheet Data"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "fetchAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> fetchAllTimeSheet(){
			return new ResponseEntity<>(
					new RestAPIResponse("success", "getall TimeSheet Data",service.getAllTimesheet()),HttpStatus.OK);
	}
	
	
	@ApiOperation("To get TimeSheetByID")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheet data Fetched"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getRequirmentByID(@PathVariable Long id) {
		
		TimeSheet timeSheetData = service.getTimeSheetByID(id);
		if(timeSheetData!=null){
		return new ResponseEntity<>(
				new RestAPIResponse("success", "TimeSheet data Fetch SuccessFully", timeSheetData),
				HttpStatus.OK);
	}else {
		return new ResponseEntity<>(new RestAPIResponse("failed","id is not present", null),HttpStatus.BAD_REQUEST);
	   }
	}
	
	@ApiOperation("To Edit TimeSheetByID")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheet data updatedById"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/editTimeSheetById", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> editTimeSheetById(@RequestBody TimeSheet timeSheet){
		return new ResponseEntity<>(new RestAPIResponse("success", "TimeSheet Successfully updatedById",
				service.editTimeSheetByID(timeSheet)), HttpStatus.ACCEPTED);
	}
	
	@ApiOperation("To Delete TimeSheet")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheet Deleted"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteTimeSheetById(@PathVariable Long id){
		boolean val = service.deleteTimeSheetByID(id);
		
		if (val == true) {
			return new ResponseEntity<>(new RestAPIResponse("success", "TimeSheet Deleted", ""), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new RestAPIResponse("fail", "TimeSheet Not Deleted", ""),
					HttpStatus.OK);
		}
	}
	
	
	@ApiOperation("To update TimeSheet")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheet updates"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateTimeSheet(@RequestBody TimeSheet timeSheet){
		Object obj = service.updateTimeSheet(timeSheet);
		return new ResponseEntity<>(new RestAPIResponse("success","update TimeSheet successfully", obj), HttpStatus.OK);
	}
	
	
	//get status 
		@GetMapping("/{id}/status")
	    public ResponseEntity<String> getTimeSheetStatus(@PathVariable Long id) {
	        Optional<TimeSheet> optionalTimeSheet = repository.findById(id);
	        if (optionalTimeSheet.isEmpty()) {
	            return ResponseEntity.notFound().build();
	        }
	        TimeSheet timeSheet = optionalTimeSheet.get();
	        String status = timeSheet.getStatus();
	        return ResponseEntity.ok(status);
	    }
}
