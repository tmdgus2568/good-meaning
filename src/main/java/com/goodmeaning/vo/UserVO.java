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

	@Id
	private String userPhone;
	@NonNull
	@Column(nullable = false)
	private String userId;

	private String userPw;
	@NonNull
	@Column(nullable = false)
	private String userEmail;
	@NonNull
	@Column(nullable = false)
	private String userName;
	@NonNull
	@Column(nullable = false)
	private String userPostcode;
	@NonNull
	@Column(nullable = false)
	private String userAddress;

	private String userAddressDetail;
	@NonNull
	@Column(nullable = false)
	private Date userBirth;
	@CreationTimestamp
	private Timestamp userJoindate;
	@NonNull
	@Column(nullable = false)
	private String joinPlatform;
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	
//	@BatchSize(size=100)
//	@JsonIgnore
//	@OneToMany(mappedBy = "userphone",
//	cascade = CascadeType.ALL,
//	fetch = FetchType.LAZY)
//	List<OrderVO> orders;
}
