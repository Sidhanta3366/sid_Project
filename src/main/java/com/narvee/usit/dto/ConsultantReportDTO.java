package com.narvee.usit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ConsultantReportDTO {
	public String getPseudoname();
	
	public String getDateOnly();
	
	public String getConsultantname();
	
	public String getConsultantid();
	
	public String getUserid();
	
	public String getId();

	public String getCompleted();

	public String getInitiated();

	public String getVerified();

	public String getActive();

	public String getInActive();

	public String getRejected();
	
	public String getSubmission();

	public String getInterview();

	public LocalDate getCreateddate();

}
