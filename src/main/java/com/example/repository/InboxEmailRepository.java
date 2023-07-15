package com.example.repository;

import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.InboxEmail;


@Repository
public interface InboxEmailRepository extends JpaRepository<InboxEmail, Long>{

	void save(List<String> emails);


	
	@Transactional
    @Modifying
  public void deleteBySubject(@Param ("subject")String subject);

}

