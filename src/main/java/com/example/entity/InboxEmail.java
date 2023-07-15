package com.example.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="inbox_mails")
public class InboxEmail {

	public InboxEmail(String from, String subject, Date sentDate, String contentType, String content) {
		this.from = from;
	    this.subject = subject;
	    this.sentDate = sentDate;
	    this.contentType = contentType;
	    this.content = content;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name ="sender")
	private String from;
	@Column(name ="sentDate")
	private Date sentDate;
	@Column(name ="subject")
	private String subject;
	@Column(name ="contentType")
	private String contentType;
	@Column(name ="content")
	@Lob
	private String content;
}
