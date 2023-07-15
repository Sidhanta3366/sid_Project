package com.narvee.usit.serviceimpl;

import java.net.URL;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Multiset.Entry;
import com.narvee.usit.entity.RSSFeed;
import com.narvee.usit.repository.IRSSFeedRepository;
import com.narvee.usit.service.IRSSFeedService;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import java.util.Optional;

@Service
public class RSSFeedServiceImpl implements IRSSFeedService {
	private static final Logger logger = LoggerFactory.getLogger(IRSSFeedService.class);
	@Autowired
	private IRSSFeedRepository repository;

	@Override
	public boolean ReadRSSFeed(String feedUrl) {

		logger.info("Inside class = RSSFeedServiceImpl ! method = ReadRSSFeed");
		try {
			// Fetch the feed and parse it
			URL url = new URL(feedUrl);
			SyndFeedInput input = new SyndFeedInput();

			XmlReader reader = new XmlReader(url);
			SyndFeed feed = input.build(reader);

			String data = "";
			boolean recordSaved = false;

			// Iterate over the feed entries and save them to the database
			List<SyndEntry> entries = feed.getEntries();
			for (SyndEntry entry : entries) {
				RSSFeed rssEntry = new RSSFeed();

				rssEntry.setCompany(feed.getTitle());

				rssEntry.setPubdate(entry.getPublishedDate());
				rssEntry.setFeedTitle(entry.getTitle());
				rssEntry.setFeedLink(entry.getLink());

				data = entry.getDescription().getValue();
				data = data.replaceAll("</?[^>]+>", "");
				rssEntry.setFeeddescription(data);

				rssEntry.setReferenceNumber(entry.getForeignMarkup().get(0).getValue());
				rssEntry.setCity(entry.getForeignMarkup().get(1).getValue());
				rssEntry.setState(entry.getForeignMarkup().get(2).getValue());
				rssEntry.setCountry(entry.getForeignMarkup().get(3).getValue());
				rssEntry.setPostalcode(entry.getForeignMarkup().get(4).getValue());
				rssEntry.setJobtype(entry.getForeignMarkup().get(5).getValue());
				rssEntry.setJobcategory(entry.getForeignMarkup().get(6).getValue());
				rssEntry.setPayrate(entry.getForeignMarkup().get(7).getValue());
				rssEntry.setEmpfirstname(entry.getForeignMarkup().get(13).getValue());
				rssEntry.setEmplastname(entry.getForeignMarkup().get(14).getValue());
				rssEntry.setEmpemail(entry.getForeignMarkup().get(15).getValue());
				rssEntry.setApplylink(entry.getForeignMarkup().get(17).getValue());

				Optional<RSSFeed> rs = repository.findByfeedTitle(rssEntry.getFeedTitle());
				if (rs.isEmpty()) {
					repository.save(rssEntry);
					recordSaved = true;

				}
			}

			return recordSaved;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public List<RSSFeed> getAllData() {
		logger.info("inside class = RSSFeedServiceImpl ! method =  getAllData() ");
		return repository.findAll(Sort.by(Sort.Direction.DESC, "pubdate"));
	}

	@Override
	public void deleteByAllIds(List<Long> id) {
		repository.deleteAllById(id);
	}

	@Override
	public RSSFeed getRssById(long id) {
		return repository.findById(id).get();
	}

}