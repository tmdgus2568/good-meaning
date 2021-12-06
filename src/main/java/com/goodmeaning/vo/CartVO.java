package com.goodmeaning.vo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name="tbl_cart")
public class CartVO {
	
	@Id
	private Long cart_num;
	
	@NonNull
	@Column(nullable = false)
	private int cart_count;
	
	@NonNull
	@Column(nullable = false)
	private int cart_price;
	

	@ManyToOne
	ProductVO product;	
	
	@ManyToOne
	UserVO userphone;
}
