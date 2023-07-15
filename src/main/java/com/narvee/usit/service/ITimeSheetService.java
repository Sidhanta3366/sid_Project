package com.narvee.usit.service;

import java.util.List;

import com.narvee.usit.entity.TimeSheet;
public interface ITimeSheetService {

public TimeSheet saveTimeSheet (TimeSheet timeSheet);
	
	public List<TimeSheet> getAllTimesheet();
	
	public TimeSheet getTimeSheetByID(Long id);
	
	public boolean editTimeSheetByID(TimeSheet timeSheet);
	
	public boolean deleteTimeSheetByID(Long id);
	
	public TimeSheet updateTimeSheet(TimeSheet timeSheet);
}
