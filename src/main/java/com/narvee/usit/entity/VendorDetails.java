package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "vendor")
public class VendorDetails {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vmsid;

	@Column(length = 200)
	private String company;

	@Column(length = 200)
	private String fedid;

	@Column(length = 200)
	private String tyretype;

	@Column(length = 200)
	private String vendortype;

	@Column(length = 200)
	private String client;

	@Column(length = 100)
	// private String vms_stat = "Entry";
	private String vms_stat = "Initiated";

	@Column(name = "status", length = 100)
	private String status = "Active";

	@Column(name = "updatedby")
	private long updatedby;

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

	@Column(length = 255)
	private String remarks;

	@Column(length = 200)
	private String staff;

	@Column(length = 200)
	private String revenue;

	@Column(length = 255)
	private String website;

	@Column(length = 255)
	private String facebook;

	@Column(length = 20)
	private String phonenumber;

	@Column(length = 200)
	private String industrytype;

	@Column(length = 255)
	private String details;

	@Column(length = 255)
	private String linkedinid;

	@Column(length = 255)
	private String twitterid;

	public VendorDetails() {
	}

	@Column(length = 50)
	private String companytype;

	@OneToOne
	@JoinColumn(name = "addedby")
	public Users user;

	@Column(length = 255)
	public String headquerter;

}
