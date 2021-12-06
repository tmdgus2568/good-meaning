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
	private Long ranswer_num;
	
	@NonNull
	@Column(nullable = false)
	private String ranser_content;
	
	@CreationTimestamp
	private Timestamp ranswer_createdate;
	
	@UpdateTimestamp
	private Timestamp ranswer_updatedate;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_phone")
	UserVO user_phone;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "review_num")
	ReviewVO review_num;
	
	
}
