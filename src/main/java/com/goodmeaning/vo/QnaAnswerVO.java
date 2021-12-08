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
	private Long qanswerNum;
	@NonNull
	@Column(nullable = false)
	private String qanswerContent;
	
	@CreationTimestamp
	private Timestamp qnaCreatedate;
	@UpdateTimestamp
	private Timestamp qnaUpdatedate;
	

	@JoinColumn(name="userPhone")
	@ManyToOne
	UserVO userPhone;
	
	@JoinColumn(name="qnaNum")
	@ManyToOne
	QnaVO qnaNum;

}
