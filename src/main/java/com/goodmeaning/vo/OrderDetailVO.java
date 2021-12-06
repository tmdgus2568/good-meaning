package com.goodmeaning.vo;

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
@Table(name="tbl_orderDetail")
public class OrderDetailVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long order_detail_num;
	
	@NonNull
	@Column(nullable = false)
	private int order_detail_quantity;
	
	@NonNull
	@Column(nullable = false)
	private int order_detail_price;
	
	@JoinColumn(name="product_num")
	@ManyToOne
	ProductVO product_num;

	@JoinColumn(name="order_num")
	@ManyToOne
	OrderVO order_num;	
}
