package com.narvee.usit.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.ConsultantReportDTO;
import com.narvee.usit.dto.DateSearcherDto;
import com.narvee.usit.dto.SubmissionDTO;
import com.narvee.usit.helper.ListInterview;
import com.narvee.usit.service.IReportService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/report")
@CrossOrigin
public class ReportController {
	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	@Autowired
	private IReportService service;
	
	@ApiOperation("To Move  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Move"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/creport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> consultantReport(@RequestBody DateSearcherDto dateSearcherDto) {
		logger.info("!!! inside class: ConsultantController, method : consultantReport");
		List<ConsultantReportDTO> consutantReport = service.consutantReport(dateSearcherDto);
		return new ResponseEntity<>(new RestAPIResponse("success", "Consultant consultantReport", consutantReport),
				HttpStatus.OK);
	}
	
	@ApiOperation("To Move  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Move"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/popup", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> popUp(@RequestBody DateSearcherDto dateSearcherDto) {
		logger.info("!!! inside class: ConsultantController, method : consultantReport");
		System.out.println(dateSearcherDto);
		List<ConsultantDTO> reportDrillDown = service.reportDrillDownsearch(dateSearcherDto);
		//DateSearcherDto(startDate=2023-05-10, endDate=2023-05-31, groupby=employee, status=Initiated, id=5)
		//DateSearcherDto(startDate=2023-05-24, endDate=2023-05-31, groupby=consultant, status=consultant, id=5)
		//List<ConsultantReportDTO> consutantReport = service.consutantReport(dateSearcherDto);
		return new ResponseEntity<>(new RestAPIResponse("success", "Consultant consultantReport", reportDrillDown),
				HttpStatus.OK);
	}
	
	@ApiOperation("To Move  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Move"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/popupSub", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> popupSub(@RequestBody DateSearcherDto dateSearcherDto) {
		logger.info("!!! inside class: ConsultantController, method : consultantReport");
		System.out.println(dateSearcherDto);
		List<SubmissionDTO> reportDrillDown = service.getsalessubmission(dateSearcherDto);
		return new ResponseEntity<>(new RestAPIResponse("success", "Consultant consultantReport", reportDrillDown),
				HttpStatus.OK);
	}
	@ApiOperation("To Move  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Move"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/popupInt", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> popupInt(@RequestBody DateSearcherDto dateSearcherDto) {
		logger.info("!!! inside class: ConsultantController, method : consultantReport");
		System.out.println(dateSearcherDto);
		List<ListInterview> reportDrillDown = service.getInterviewDropdown(dateSearcherDto);
		return new ResponseEntity<>(new RestAPIResponse("success", "Consultant consultantReport", reportDrillDown),
				HttpStatus.OK);
	}
	
}
