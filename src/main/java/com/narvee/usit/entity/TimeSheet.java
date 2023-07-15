package com.narvee.usit.entity;

import java.time.LocalDateTime;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timesheet")
public class TimeSheet {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 100)
	private String projectname;
//	@Column(length = 150)
//	private String description;
	@Column(length = 100)
	private String taskname;
	
	private String status;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "startdate")
	private LocalDateTime startdate;
	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name = "targetdate")
	private LocalDateTime targetdate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "creationtimestamp", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime creationtimestamp;
	
	@UpdateTimestamp
	private LocalDateTime updationtimestamp;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid")
	private Users user;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name ="id", nullable = true)
	private List<TimeSheetDescription> description;
	
	@Transient
    private static final String STATUS_INITIATED = "Initiated";
    @Transient
    private static final String STATUS_COMPLETED = "Completed";
    @Transient
    private static final String STATUS_PENDING = "Pending";

    
    public String getStatus() {
        if (startdate == null || targetdate == null) {
            return STATUS_INITIATED;
        } else {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(targetdate)) {
                return STATUS_COMPLETED;
            } else {
                return STATUS_PENDING;
            }
        }
    }
}
