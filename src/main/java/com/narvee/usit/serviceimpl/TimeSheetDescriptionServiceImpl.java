package com.narvee.usit.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.usit.entity.TimeSheetDescription;
import com.narvee.usit.repository.ITimeSheetDescriptionRepository;
import com.narvee.usit.service.ITimeSheetDescriptionService;


@Service
public class TimeSheetDescriptionServiceImpl implements ITimeSheetDescriptionService{

		@Autowired
		private ITimeSheetDescriptionRepository repository;
	
	@Override
	public TimeSheetDescription saveTimeSheetDesc(TimeSheetDescription timeSheetDescription) {
		
		return repository.save(timeSheetDescription);
	}

	@Override
	public List<TimeSheetDescription> getAllTimeSheetDescription() {
		
		return repository.findAll();
	}

	@Override
	public TimeSheetDescription getTimeSheetDescriptionByID(Long id) {
		Optional<TimeSheetDescription> entity = repository.findById(id);
		
		if(entity.isPresent()) {
			return entity.get();
		}
		return null;
	}

	@Override
	public TimeSheetDescription updateTimeSheetDescription(TimeSheetDescription timeSheetdesc) {
		
		return repository.save(timeSheetdesc);
	}

	@Override
	public boolean deleteTimeSheetDescriptionByID(Long id) {
		repository.deleteById(id);
		return true;
	}

}
