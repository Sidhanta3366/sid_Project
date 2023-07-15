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
import com.narvee.usit.entity.TimeSheetDescription;
import com.narvee.usit.repository.ITimeSheetDescriptionRepository;
import com.narvee.usit.repository.ITimeSheetRepository;
import com.narvee.usit.service.ITimeSheetDescriptionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/timesheetDescription")
@CrossOrigin
public class TimeSheetDescriptionController {

	@Autowired
	private ITimeSheetDescriptionService service;
	
	@Autowired
	private ITimeSheetDescriptionRepository repository;
	
	
	@ApiOperation("To Save TimeSheetDescription")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheetDescription Saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveTimeSheetDescription(@RequestBody TimeSheetDescription timeSheetdescription){
		
		Object obj = service.saveTimeSheetDesc(timeSheetdescription);
		return new ResponseEntity<>(new RestAPIResponse("success", "Save TimeSheet Successfully", obj),
				HttpStatus.CREATED);
	}
	
	
	@ApiOperation("To getAll TimeSheetDescription Data")
	@ApiResponses({ @ApiResponse(code = 200, message = "getAll TimeSheetDescription Data"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "fetchAll", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> fetchAllTimeSheetdescription(){
			return new ResponseEntity<>(
					new RestAPIResponse("success", "getAll TimeSheetDescription Data",service.getAllTimeSheetDescription()),HttpStatus.OK);
	}
	
	
	@ApiOperation("To get TimeSheetDescriptionByID")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheetDescription data Fetched"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getTimeSheetdescByID(@PathVariable Long id) {
		
		TimeSheetDescription timeSheetdescData = service.getTimeSheetDescriptionByID(id);
		if(timeSheetdescData!=null){
		return new ResponseEntity<>(
				new RestAPIResponse("success", "TimeSheetDescription data Fetch SuccessFully", timeSheetdescData),
				HttpStatus.OK);
	}else {
		return new ResponseEntity<>(new RestAPIResponse("failed","id is not present", null),HttpStatus.BAD_REQUEST);
	   }
	}
	
	
	@ApiOperation("To update TimeSheetDescription")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheetDescription updates"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateTimeSheet(@RequestBody TimeSheetDescription timeSheetdesc){
		Object obj = service.updateTimeSheetDescription(timeSheetdesc);
		return new ResponseEntity<>(new RestAPIResponse("success","update TimeSheetDescription successfully", obj), HttpStatus.OK);
	}
	
	
	@ApiOperation("To Delete TimeSheetDescription")
	@ApiResponses({ @ApiResponse(code = 200, message = "TimeSheetDescription Deleted"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteTimeSheetDescriptionById(@PathVariable Long id){
		boolean val = service.deleteTimeSheetDescriptionByID(id);
		
		if (val == true) {
			return new ResponseEntity<>(new RestAPIResponse("success", "TimeSheetDescription Deleted", ""), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new RestAPIResponse("fail", "TimeSheetDescription Not Deleted", ""),
					HttpStatus.OK);
		}
	}
	
	
	//get status 
			/*@GetMapping("/{id}/status")
		    public ResponseEntity<String> getTimeSheetDescriptionStatus(@PathVariable Long id) {
		        Optional<TimeSheetDescription> optionalTimeSheet = repository.findById(id);
		        if (optionalTimeSheet.isEmpty()) {
		            return ResponseEntity.notFound().build();
		        }
		        TimeSheetDescription timeSheet = optionalTimeSheet.get();
		        String status = timeSheet.getStatus();
		        return ResponseEntity.ok(status);
		    }*/
	
}
