package com.narvee.usit.entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
@Entity
@Data
@Table(name = "rssfeed")
public class RSSFeed {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "company")
	private String company;
	
	@Column(name = "feedtitle")
	private String feedTitle;
	
	@Column(name = "feeddescription", columnDefinition = "TEXT")
	private String feeddescription;
	
	@Column(name = "feedLink")
	private String feedLink;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "pubdate")
	private Date pubdate;
	
	@Column(name = "referencenumber")
	private String referenceNumber;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "country")
	private String country;
	
	@Column(name = "postalcode")
	private String postalcode;
	
	@Column(name = "jobtype")
	private String jobtype;
	
	@Column(name = "jobcategory")
	private String jobcategory;
	
	@Column(name = "payrate")
	private String payrate;
	
	@Column(name = "empfirstname")
	private String empfirstname;
	
	@Column(name = "emplastname")
	private String emplastname;
	
	@Column(name = "empemail")
	private String empemail;
	
	@Column(name = "applylink")
	private String applylink;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "createddate", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDate createddate;
}
