package com.narvee.usit.controller;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.RSSFeed;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.service.IRSSFeedService;
import com.narvee.usit.vo.RSSFEEDVO;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/rssfeed")
@CrossOrigin
public class RSSFeedController {
	
	private static final Logger logger = LoggerFactory.getLogger(RSSFeedController.class);
	
	@Autowired
	private IRSSFeedService service;
	
	@ApiOperation("To save RssFeed data")
	@ApiResponses({@ApiResponse(code = 200, message = "RssFeed save"),
					@ApiResponse(code = 404, message  = "Bad Request"),
					@ApiResponse(code = 500, message = "Internal Server Error")})
	@RequestMapping(value = "/save", method = RequestMethod.POST , produces ="application/json")
	public ResponseEntity<RestAPIResponse> saveRssFeed(@RequestBody RSSFEEDVO ent){
		logger.info("inside RssFeedController ! method = saveRssFeed() ");
		return new ResponseEntity (new RestAPIResponse("success", "RssFeed saved Successfully", service.ReadRSSFeed(ent.getFeedUrl())),HttpStatus.CREATED);
	}
	
	@ApiOperation("To get single role")
	@ApiResponses({ @ApiResponse(code = 200, message = "fetched single role"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getRSSById/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getRole(@PathVariable Long id) {
		logger.info("inside RolesController.getRole()=> fetching single role by id");
		RSSFeed  rssfeed = service.getRssById(id);
		return new ResponseEntity<>(new RestAPIResponse("success", "RSS Feteched By ID", rssfeed), HttpStatus.OK);
	}
	
	
	@ApiOperation("To fetch all rssfeed data")
	@ApiResponses({@ApiResponse(code = 200, message = "Fetch rss feed"),
					@ApiResponse(code = 404, message = "Bad request"),
					@ApiResponse(code = 500, message = "Internal server error")})
	@RequestMapping(value = "/getalldata", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllData(){
		logger.info("inside RssFeedController ! method = getAllData() ");
		return new ResponseEntity<> (new RestAPIResponse("success", "Feched all data", service.getAllData()),HttpStatus.OK);
	}
	@ApiOperation("To delete multiple rss feeds")
	@ApiResponses({@ApiResponse(code = 200, message = "deleted rss feed"),
					@ApiResponse(code = 404, message = "Bad request"),
					@ApiResponse(code = 500, message = "Internal server error")})
	@RequestMapping(value = "/deletedata", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteByids(@RequestParam ("ids") List<Long> id){
		service.deleteByAllIds(id);
		return new ResponseEntity<> (new RestAPIResponse("Success", "ids deleted","deleted"),HttpStatus.OK); 
	}
}