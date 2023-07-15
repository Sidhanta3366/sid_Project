package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Data
@Table(name = "Recruiter")
public class Recruiter {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long recid;

	@Column(length = 100)
	private String recruiter;

	@Column(length = 20)
	private String usnumber;

	@Column(length = 100)
	private String iplogin;

//	@Column(length = 200)
//	private String fedid;

	@Column(length = 200)
	private String email;

	@Column(length = 255)
	private String linkedin;

	@Column(name = "status", length=80)
	private String status = "Active";
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "createddate", nullable = false, updatable = false)
	// @CreationTimestamp
	private LocalDateTime createddate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "updateddate", nullable = false, updatable = true)
	// @UpdateTimestamp
	private LocalDateTime updateddate;
	
	@PrePersist
	public void setCreateddate() {
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		this.createddate = now;
		this.updateddate = now;
	}

	@PreUpdate
	public void setUpdateddate() {
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		this.updateddate = now;
	}
	

	@Column(name = "updatedby")
	private long updatedby;

	

	@Column(length = 255)
	private String remarks;

	@Column(length = 100)
	private String rec_stat = "Initiated";

	@Column(length = 200)
	private String designation;
//	
	@OneToOne
	@JoinColumn(name="addedby")
	private Users user;

	@Column(length = 255)
	private String details;

	@OneToOne
	@JoinColumn(name="vendorid")
	private VendorDetails vendor;

	@Column(length = 50)
	private String recruitertype;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "contactnumber")
	private PhoneNumberFormat contactnumber;
	
	@Column(length = 10)
	private String extension;

	public Recruiter() {

	}

}
