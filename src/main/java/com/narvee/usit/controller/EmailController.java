package com.narvee.usit.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.dto.ListExtractMailDTO;
import com.narvee.usit.entity.ExtractEmail;
import com.narvee.usit.service.IEmailBKPService;
import com.narvee.usit.vo.Request;
import com.narvee.usit.vo.RequestForArrayType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/emailbkp")
@CrossOrigin
public class EmailController {

	@Autowired
	private IEmailBKPService service;

	// created api for fetching all data from database
	@RequestMapping(value = "/getMails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllMailWithAttachment() {
		List<ListExtractMailDTO> allmails = service.listAll();
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Fetched All Data from ExtractEmail and EmailAttachment", allmails),
				HttpStatus.OK);
	}

	// created api for fetching all data from database
	@RequestMapping(value = "/getdailyrequirement", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getDailyRequirement() {
		List<ListExtractMailDTO> allmails = service.dailyRequirement();
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Fetched All Data from ExtractEmail and EmailAttachment", allmails),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteMailByIds", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<RestAPIResponse> deleteMailByIds(@RequestBody RequestForArrayType req) {
		List<Long> list = new ArrayList<Long>(List.of(req.getIds()));
		service.deleteAllByIdInBatch(list);
		return new ResponseEntity<>(new RestAPIResponse("Success"), HttpStatus.OK);
	}

	@RequestMapping(value = "/moveToDB", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<RestAPIResponse> moveToDB(@RequestBody RequestForArrayType req) {
		List<Long> list = new ArrayList<Long>(List.of(req.getIds()));
		service.moveibMailstoDB(list);
		return new ResponseEntity<>(new RestAPIResponse("success"), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/extractemails", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> extractEmail(@RequestBody Request request) {
		// fot gmail
		String host = "smtp.gmail.com";
		String port = "993";// 995 pop3
		final String username = request.getUserName(); // "swamy@narveetech.com";
		final String password = request.getUserPwd(); // "Narvee123$";// change accordingly
		Date fromdate = request.getFromdate();
		Date todate = request.getTodate();
		long userid = request.getUserid();
		String message = "nDomain";
		HttpStatus Status = HttpStatus.OK;
		String msg = "success";
//		try {
//			String result = username.substring(username.indexOf("@") + 1, username.indexOf("."));
//
//		} catch (StringIndexOutOfBoundsException e3) {
//		}
		message = service.mailExtraction(host, port, username, password, fromdate, todate,userid);
		if (message.equalsIgnoreCase("error1")) {
			Status = HttpStatus.NOT_IMPLEMENTED;
			msg = "fail";
		} else if (message.equalsIgnoreCase("error2")) {
			Status = HttpStatus.NOT_IMPLEMENTED;
			msg = "fail";
		}

		else if (message.equalsIgnoreCase("Authentication Error")) {
			Status = HttpStatus.NOT_IMPLEMENTED;
			msg = "AuthError";
		} else if (message.equalsIgnoreCase("Success")) {
			Status = HttpStatus.OK;
			msg = "success";
		} else if (message.equalsIgnoreCase("nDomain")) {
			Status = HttpStatus.OK;
			msg = "success";
		} else {
			msg = "fail";
			Status = HttpStatus.NOT_IMPLEMENTED;
		}
		return new ResponseEntity<>(new RestAPIResponse(msg, "Fetched All Emails", ""), HttpStatus.OK);
	}
	@ApiOperation("Consultant Download Files")
	@ApiResponses({ @ApiResponse(code = 200, message = "Consultant Download Files"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/downloadfiles/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@PathVariable long id) throws IOException {
		Resource file = service.download(id);
		Path path = file.getFile().toPath();
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getMailByID(@PathVariable long id) {
		Optional<ExtractEmail> getmailbyid = service.getmailbyid(id);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", getmailbyid), HttpStatus.OK);
	}

}
