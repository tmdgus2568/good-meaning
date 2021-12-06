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
@Table(name="tbl_review")
public class ReviewVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long review_num;
	
	@NonNull
	@Column(nullable = false)
	private String review_title;
	
	@NonNull
	@Column(nullable = false)
	private String review_content;
	
	@CreationTimestamp
	private Timestamp review_createdate;
	
	@UpdateTimestamp
	private Timestamp review_updatedate;
	
	@Column(nullable = false)
	@ColumnDefault("0")
	private int review_like;
	
	@JoinColumn(name="user_phone")
	@ManyToOne
	UserVO user_phone;
	
	@JoinColumn(name="product_num")
	@ManyToOne
	ProductVO product_num;
	
//	@BatchSize(size=100)
//	@JsonIgnore
//	@OneToMany(mappedBy = "review_num",
//	cascade = CascadeType.ALL,
//	fetch = FetchType.LAZY)
//	List<ReviewAnswerVO> answers;
	
	

}
