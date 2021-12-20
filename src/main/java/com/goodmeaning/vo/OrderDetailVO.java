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
@ToString (exclude ="orderNum")
@Builder
@AllArgsConstructor 
@NoArgsConstructor
@Entity 
@Table(name="tbl_orderDetail")
public class OrderDetailVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long orderDetailNum;
	
	@NonNull
	@Column(nullable = false)
	private int orderDetailQuantity;
	
	@NonNull
	@Column(nullable = false)
	private int orderDetailPrice;
	
	@JoinColumn(name="productNum")
	@ManyToOne
	ProductVO productNum;

	@JsonIgnore
	@JoinColumn(name="orderNum")
	@ManyToOne
	OrderVO orderNum;	
}
