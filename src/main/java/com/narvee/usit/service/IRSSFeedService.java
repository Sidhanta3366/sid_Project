package com.narvee.usit.service;

import java.util.List;

import com.narvee.usit.entity.RSSFeed;

public interface IRSSFeedService {
	public boolean ReadRSSFeed(String feedUrl);

	public List<RSSFeed> getAllData();

	public void deleteByAllIds(List<Long> id);
	
	public RSSFeed getRssById(long id);
}
