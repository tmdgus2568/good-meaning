package com.goodmeaning.vo;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Table(name = "tbl_product_register")
public class ProductRegisterVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long regNum;
	@CreationTimestamp
	private Timestamp regCreatedate;
	@UpdateTimestamp
	private Timestamp regUpdatedate;
	@NonNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductRegisterState regState;
	private String regNote;
	
	@JoinColumn(name="productNum")
	@ManyToOne
	ProductVO productNum;
	
	
	
	
	
}
