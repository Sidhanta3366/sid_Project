package com.narvee.usit.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.query.Param;

import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.ConsultantReportDTO;
import com.narvee.usit.dto.DateSearcherDto;
import com.narvee.usit.dto.SubmissionDTO;
import com.narvee.usit.helper.ListInterview;

public interface IReportService {

	public List<ConsultantReportDTO> consutantReport(DateSearcherDto dateSearcherDto);
	
	public List<ConsultantDTO> reportDrillDownsearch(DateSearcherDto dateSearcherDto);
	
	// for submission
	public List<SubmissionDTO> getsalessubmission(DateSearcherDto dateSearcherDto);
	
	// for interview
	public List<ListInterview> getInterviewDropdown(DateSearcherDto dateSearcherDto);

	
}
