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
@Table(name="tbl_qna_answer")
public class QnaAnswerVO {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long qanswer_num;
	@NonNull
	@Column(nullable = false)
	private String qanswer_content;
	
	@CreationTimestamp
	private Timestamp qna_createdate;
	@UpdateTimestamp
	private Timestamp qna_updatedate;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "user_phone")
	UserVO user_phone;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "qna_num")
	QnaVO qna_num;

}
