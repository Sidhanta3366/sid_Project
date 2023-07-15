package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table
@Data
public class ConsultantFileUploads {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long docid;
	private String filename;
	@Column(name = "consultant_id")
	private long consultantid;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "createddate", nullable = false, updatable = true)
	// @UpdateTimestamp
	private LocalDateTime createddate;
	@PrePersist
	public void setCreateddate() {
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		this.createddate = now;
	}
}
