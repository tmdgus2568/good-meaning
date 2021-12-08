package com.goodmeaning.vo;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
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
@Table(name="tbl_qna")
public class QnaVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long qnaNum;
	@NonNull
	@Column(nullable = false)
	private String qnaTitle;
	@NonNull
	@Column(nullable = false)
	@ColumnDefault("0")
	private int qnaSecret; //0 공개, 1 비공개
	
	@CreationTimestamp
	private Timestamp qnaCreatedate;
	@UpdateTimestamp
	private Timestamp qnaUpdatedate;
	
	@JoinColumn(name="userPhone")
	@ManyToOne
	UserVO userPhone;
	
	@JoinColumn(name="productNum")
	@ManyToOne
	ProductVO productNum;
	
//	@BatchSize(size=100)
//	@JsonIgnore
//	@OneToMany(mappedBy = "qna_num",
//	cascade = CascadeType.ALL,
//	fetch = FetchType.LAZY)
//	List<QnaAnswerVO> answers;
}
