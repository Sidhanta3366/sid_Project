package com.narvee.usit.service;

import java.util.List;

import com.narvee.usit.entity.TimeSheetDescription;

public interface ITimeSheetDescriptionService {

	public TimeSheetDescription saveTimeSheetDesc(TimeSheetDescription timeSheetDescription);
	
	public List<TimeSheetDescription> getAllTimeSheetDescription();
	
	public TimeSheetDescription getTimeSheetDescriptionByID(Long id);
	
	public TimeSheetDescription updateTimeSheetDescription(TimeSheetDescription timeSheetdesc);
	
	public boolean deleteTimeSheetDescriptionByID(Long id);
}
