package com.narvee.usit.dto;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.narvee.usit.entity.ConsultantFileUploads;
import com.narvee.usit.entity.ConsultantTrack;
import com.narvee.usit.entity.PhoneNumberFormat;
import com.narvee.usit.entity.Qualification;
import com.narvee.usit.entity.Technologies;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.Visa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DailyConsultantsDTO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long consultantid;

	@Column(name = "consultantname", length = 100)
	private String consultantname;

	@Column(name = "firstname", length = 35)
	private String firstname;

	@Column(name = "lastname", length = 35)
	private String lastname;

	@Column(name = "contactnumber", length = 18)
	private String contactnumber;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "number")
	private PhoneNumberFormat number;

	@Column(name = "consultantemail", length = 80)
	private String consultantemail;

	private String position;
	private String currentlocation;

	@OneToOne
	@JoinColumn(name = "visa_status")
	private Visa visa;

	@Column(name = "experience", length = 3)
	private String experience;

	// if relocation to other reason
	@Column(name = "relocation", length = 20)
	private String relocation;

	@Column(name = "relocatother", length = 250)
	private String relocatOther;

	@OneToOne(fetch = FetchType.LAZY) // (cascade = CascadeType.DETACH)
	@JoinColumn(name = "techid")
	private Technologies technology;

	@Column(name = "ratetype", length = 20)
	private String ratetype;

	@Column(name = "hourlyrate", length = 20)
	private String hourlyrate;

	@Column(name = "skills", columnDefinition = "TEXT")
	private String skills;

	@Column(name = "summary", columnDefinition = "MEDIUMTEXT")
	private String summary;

	@Column(name = "priority", length = 10)
	private String priority;

//	// educational background for recruiting'
//	@Column(name = "qualification", length = 150)
//	private String qualification;

	@OneToOne
	@JoinColumn(name = "qid")
	private Qualification qualification;

//	@Column(name = "specialization", length = 100)
//	private String specialization;

	@Column(name = "university", length = 220)
	private String university;

	@Column(name = "yop", length = 6)
	private String yop;

	@Column(name = "linkedin", length = 250)
	private String linkedin;

	@Column(name = "passportnumber", length = 20)
	private String passportnumber;

	@Column(name = "projectavailabity", length = 50)
	private String projectavailabity;

	@Column(name = "availabilityforinterviews", length = 100)
	private String availabilityforinterviews;

	// upload files
	@Column(name = "resume", length = 200)
	private String resume;

	@Column(name = "h1bcopy", length = 100)
	private String h1bcopy;

	@Column(name = "dlcopy", length = 100)
	private String dlcopy;

	// recruiting multiple file uploads
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "consultant_id")
	private List<ConsultantFileUploads> fileupload;
	// ConsultantTrack

	// @OneToMany(cascade = CascadeType.ALL)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "consultantid", nullable = false)
	private List<ConsultantTrack> track;

	// recruiting current mployer details
	@Column(name = "companyname", length = 200)
	private String companyname;

	@Column(name = "refname", length = 100)
	private String refname;

	@Column(name = "refname1", length = 40)
	private String refname1;

	@Column(name = "refemail", length = 40)
	private String refemail;

	@Column(name = "refemail1", length = 40)
	private String refemail1;

	@Column(name = "refcont", length = 40)
	private String refcont;

	@Column(name = "refcont1", length = 40)
	private String refcont1;

	@Column(name = "remarks", length = 255)
	private String remarks;

	@OneToOne
	@JoinColumn(name = "addedby")
	private Users addedby;

	@OneToOne
	@JoinColumn(name = "updatedbyid")
	private Users updatedby;

	@Column(name = "consultantflg", length = 20)
	private String consultantflg;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "createddate", nullable = false, updatable = false)
	// @CreationTimestamp
	private LocalDateTime createddate;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "updateddate", nullable = false, updatable = true)
	// @UpdateTimestamp
	private LocalDateTime updateddate;

	@Column(name = "status", length = 30)
	private String status = "Active";

	private String comment;

}
