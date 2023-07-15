package com.narvee.usit.service;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;

import com.narvee.usit.dto.ListExtractMailDTO;
import com.narvee.usit.entity.ExtractEmail;

public interface IEmailBKPService {
	public List<ExtractEmail> findAllMailDataWithAttachment() ;
	public boolean deleteAllByIdInBatch(List<Long> ids);
	public boolean moveibMailstoDB(List<Long> ids);
	public List<ListExtractMailDTO> listAll();
	public List<ListExtractMailDTO> dailyRequirement();
	public Resource download(long id) throws FileNotFoundException;
	public Optional<ExtractEmail> getmailbyid(long id);
	// to read emails
	public String mailExtraction(String host, String port, String userName, String password, Date fromDate, Date toDate,
			long userid);

}
