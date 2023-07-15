package com.narvee.usit.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.usit.entity.TimeSheet;
import com.narvee.usit.repository.ITimeSheetRepository;
import com.narvee.usit.service.ITimeSheetService;

@Service
public class TimeSheetServiceImpl implements ITimeSheetService{

	
	@Autowired
	private ITimeSheetRepository repository;
	
	@Override
	public TimeSheet saveTimeSheet(TimeSheet timeSheet) {
		TimeSheet save = repository.save(timeSheet);
		return save;
	}

	
	@Override
	public List<TimeSheet> getAllTimesheet() {
		
		return repository.findAll();
	}


	@Override
	public TimeSheet getTimeSheetByID(Long id) {
		Optional<TimeSheet> timeSheet = repository.findById(id);
		
		if(timeSheet.isPresent()) {
			return timeSheet.get();
		}
		return null;
	}


	@Override
	public boolean editTimeSheetByID(TimeSheet timeSheet) {
		TimeSheet time = repository.save(timeSheet);
		if(time!=null) {
			return true;
		}
		return false;
	}


	@Override
	public boolean deleteTimeSheetByID(Long id) {
		repository.deleteById(id);
		return true;
	}


	@Override
	public TimeSheet updateTimeSheet(TimeSheet timeSheet) {
		TimeSheet save = repository.save(timeSheet);
		return save;
	}
}
