package com.narvee.usit.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ListVendor {
	public long getId();
	
	public long getAddedby();

	public String getCompany();

	public String getPseudoname();

	public String getCompanytype();

	public String getLocation1();

	public String getLocation2();

	public String getHeadquerter();

	public String getFullname();

	public String getFedid();

	public String getVendortype();

	public String getTyretype();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd  HH:mm")
	public LocalDateTime getCreateddate();
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd  HH:mm")
	public LocalDateTime getUpdateddate();

	public String getVms_stat();

	public String getStatus();

	public String getRemarks();
}
