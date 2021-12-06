package com.goodmeaning.vo;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter 
@ToString 
@Builder
@AllArgsConstructor 
@NoArgsConstructor
@Entity 
@Table(name="tbl_user")
public class UserVO {
	
	// hsh test

	@Id
	private String user_phone;
	@NonNull
	@Column(nullable = false)
	private String user_id;
	@NonNull
	@Column(nullable = false)
	private String user_pw;
	@NonNull
	@Column(nullable = false)
	private String user_email;
	@NonNull
	@Column(nullable = false)
	private String user_name;
	@NonNull
	@Column(nullable = false)
	private String user_postcode;
	@NonNull
	@Column(nullable = false)
	private String user_address;

	private String user_address_detail;
	@NonNull
	@Column(nullable = false)
	private Date user_birth;
	@CreationTimestamp
	private Timestamp user_joindate;
	@NonNull
	@Column(nullable = false)
	private String join_platform;
	@Enumerated(EnumType.STRING)
	private UserRole user_role;
	
	@BatchSize(size=100)
	@JsonIgnore
	@OneToMany(mappedBy = "userphone",
	cascade = CascadeType.ALL,
	fetch = FetchType.LAZY)
	List<OrderVO> orders;
}
