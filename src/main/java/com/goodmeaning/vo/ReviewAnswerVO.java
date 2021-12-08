package com.goodmeaning.vo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name="tbl_review_answer")
public class ReviewAnswerVO {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ranswerNum;
	
	@NonNull
	@Column(nullable = false)
	private String ranserContent;
	
	@CreationTimestamp
	private Timestamp ranswerCreatedate;
	
	@UpdateTimestamp
	private Timestamp ranswerUpdatedate;

	@JoinColumn(name="userPhone")
	@ManyToOne
	UserVO userPhone;
	
	@JoinColumn(name="reviewNum")
	@ManyToOne
	ReviewVO reviewNum;
	
	
}
