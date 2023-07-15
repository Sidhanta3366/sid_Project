package com.narvee.usit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.usit.entity.RSSFeed;

public interface IRSSFeedRepository extends JpaRepository<RSSFeed, Long> {
	
	public Optional<RSSFeed> findByfeedTitle(String feedtitle);
	
	//public Optional<RSSFeed> findByfeedTitle(String feedtitle);

}
