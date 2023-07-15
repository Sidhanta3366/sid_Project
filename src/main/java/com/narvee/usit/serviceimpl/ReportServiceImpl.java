package com.narvee.usit.serviceimpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.ConsultantReportDTO;
import com.narvee.usit.dto.DateSearcherDto;
import com.narvee.usit.dto.SubmissionDTO;
import com.narvee.usit.helper.ListInterview;
import com.narvee.usit.repository.IConsultantRepository;
import com.narvee.usit.service.IReportService;

@Service
public class ReportServiceImpl implements IReportService {

	@Autowired
	private IConsultantRepository repository;

	@Override
	public List<ConsultantReportDTO> consutantReport(DateSearcherDto dateSearcherDto) {
		if (dateSearcherDto.getGroupby().equalsIgnoreCase("date"))
			return repository.consutantReportGroupbyDate(dateSearcherDto.getStartDate(), dateSearcherDto.getEndDate());
		else if (dateSearcherDto.getGroupby().equalsIgnoreCase("employee"))
			return repository.consutantReportGroupbyEmployee(dateSearcherDto.getStartDate(),
					dateSearcherDto.getEndDate());
		else
			return repository.consutantReportGroupbyConsultant(dateSearcherDto.getStartDate(),
					dateSearcherDto.getEndDate());
	}

	@Override
	public List<ConsultantDTO> reportDrillDownsearch(DateSearcherDto dateSearcherDto) {
		String status = dateSearcherDto.getStatus();
		String groupby = dateSearcherDto.getGroupby();

		List<String> statusArg = new ArrayList<>();

		if (groupby.equalsIgnoreCase("consultant")) {
			if (status.equalsIgnoreCase("consultant")) {
				statusArg.add("Active");
				statusArg.add("Initiated");
				statusArg.add("Completed");
				statusArg.add("Verified");
			} else {
				statusArg.add(status);
			}
			return repository.reportDrillDown(statusArg, dateSearcherDto.getStartDate(), dateSearcherDto.getEndDate(),
					dateSearcherDto.getId());
		} else {
			if (status.equalsIgnoreCase("employee")) {
				statusArg.add("Active");
				statusArg.add("Initiated");
				statusArg.add("Completed");
				statusArg.add("Verified");
			} else {
				statusArg.add(status);
			}
			return repository.reportDrillDownForEmployee(statusArg, dateSearcherDto.getStartDate(),
					dateSearcherDto.getEndDate(), dateSearcherDto.getId());
		}
	}

	@Override
	public List<SubmissionDTO> getsalessubmission(DateSearcherDto dateSearcherDto) {
		String groupby = dateSearcherDto.getGroupby();
		if (groupby.equalsIgnoreCase("consultant")) {
			return repository.getsalessubmissionreport(dateSearcherDto.getStartDate(), dateSearcherDto.getEndDate(),
					dateSearcherDto.getId());
		} else {
			return repository.getsalessubmissionreportForEmployee(dateSearcherDto.getStartDate(),
					dateSearcherDto.getEndDate(), dateSearcherDto.getId());
		}
	}

	@Override
	public List<ListInterview> getInterviewDropdown(DateSearcherDto dateSearcherDto) {
		String groupby = dateSearcherDto.getGroupby();
		if (groupby.equalsIgnoreCase("consultant")) {
			return repository.getInterviewDropdown(dateSearcherDto.getStartDate(), dateSearcherDto.getEndDate(),
					dateSearcherDto.getId());
		} else {
			return repository.getInterviewDropdownForEmployee(dateSearcherDto.getStartDate(),
					dateSearcherDto.getEndDate(), dateSearcherDto.getId());
		}

	}

}
