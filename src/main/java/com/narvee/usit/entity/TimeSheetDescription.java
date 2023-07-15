package com.narvee.usit.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "time_sheet_description")
public class TimeSheetDescription {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long timesheetdesid;
		
		private String status;
		
		@Column(length = 150)
		private String description;
		
		@Column(length = 100)
		private String taskname;
		
		@Column(length = 100)
		private String projectname;
		
	
		@OneToOne(fetch = FetchType.LAZY)
		@JoinColumn(name = "userid")
		private Users user;
}
