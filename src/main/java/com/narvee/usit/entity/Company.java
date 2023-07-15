package com.narvee.usit.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	
	@Column(name = "cid")
	private Long companyid;
	
	private String companyname;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "creationtimestamp", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime creationtimestamp;
	
	@UpdateTimestamp
	private LocalDateTime updationtimestamp;
}
