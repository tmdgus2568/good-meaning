package com.goodmeaning.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "tbl_product_option")
public class ProductOptionVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long option_num;
	@NonNull
	@Column(nullable = false)
	private String option_name;
	@Column(nullable = false)
	private int option_stock;
	private int extraprice;
	
	@JoinColumn(name="product_num")
	@ManyToOne
	ProductVO product_num;
	
}
