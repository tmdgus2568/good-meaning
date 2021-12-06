package com.goodmeaning.vo;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;

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
@Table(name = "tbl_product")
public class ProductVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long product_num;
	@NonNull
	@Column(nullable = false)
	private String product_name;
	@NonNull
	@Column(nullable = false)
	private int product_price;

	private int product_stock;
	@NonNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
//	@Convert(converter = SetConverter.class)
//	@ElementCollection
	private Category product_category;
	@NonNull
	@Column(nullable = false)
	private String product_mainimg;
	@NonNull
	@Column(nullable = false)
	private String product_detailimg;
	
	@BatchSize(size=100)
	@JsonIgnore
	@OneToMany(mappedBy = "product_num",
	cascade = CascadeType.ALL,
	fetch = FetchType.LAZY)
	List<ProductOptionVO> options;
}
