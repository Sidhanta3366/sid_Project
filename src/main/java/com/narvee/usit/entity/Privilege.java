package com.narvee.usit.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "privilege")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Privilege {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "name")
	private String name;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "role_privilege", joinColumns = {
			@JoinColumn(name = "privilege_id", nullable = false, updatable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "role_id", nullable = false, updatable = false) })
	private Set<Roles> roles = new HashSet<Roles>(0);

	@Column(name = "type")
	private String type;

	@Column(name = "description")
	private String description;

	@Column(name = "createdby", insertable = true, updatable = false)
	private Long createdBy;

	@Column(name = "updatedby", insertable = true)
	private Long updatedBy;

//	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_timestamp", insertable = true, updatable = false)
	private Date createdTimestamp;

//	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_timestamp", insertable = true, updatable = true)
	@UpdateTimestamp
	private Date updatedTimestamp;

	public enum privilegeType {
		VENDOR, RECRUITER, TECH_SUPPORT, CONSULTANT, VISA, QUALIFICATION, TECHNOLOGY_TAGS
	}
}
