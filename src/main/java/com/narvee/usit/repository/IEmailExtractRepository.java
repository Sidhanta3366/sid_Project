package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.narvee.usit.entity.ExtractEmail;
import com.narvee.usit.dto.ListExtractMailDTO;

@Repository
public interface IEmailExtractRepository extends JpaRepository<ExtractEmail, Serializable> {
	public List<ExtractEmail> findByFrommail(String mail);

	@Query(value = "select mailfrom from extractemail order by mailfrom", nativeQuery = true)
	public Set<String> dulicatecheck();

	@Query(value = "select id,body,company,mailfrom,receiveddate,sentdate,mailsubject from extractemail where emailflg is null order by receiveddate desc", nativeQuery = true)
	public List<ListExtractMailDTO> listAll();

	@Query(value = "select max(receiveddate)as Date from extractemail where mailto = :mailto", nativeQuery = true)
	public Date maxDateFunction(String mailto);
	
	public List<ExtractEmail> findBySubject(String subject);
	
	public boolean existsBySubject(String subject);
	
	@Modifying
	@Query("update  ExtractEmail set emailflg='false' WHERE id in (:ids)")
	public void deletemailById(List<Long> ids);
	
	@Modifying
	@Query("update  ExtractEmail set emailflg='dashboard' WHERE id in (:ids)")
	public void moveIBtoDB(List<Long> ids);
	
	@Query(value = "select id,body,company,mailfrom,receiveddate,sentdate,mailsubject from extractemail where emailflg='dashboard' order by updateddate desc", nativeQuery = true)
	public List<ListExtractMailDTO> dailyRequirement();
	
	
	//	select body,company,mailfrom,receiveddate,sentdate,mailsubject where emailflg is null

	//Written JPQL query for fetching all data from database
//	@Query(value = "SELECT e, a from extractemail e left join e.attachments a")
//	public List<Object[]> findAllEmailsWithAttachments();

}
