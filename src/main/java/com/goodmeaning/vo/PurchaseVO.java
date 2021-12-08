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
@Table(name="tbl_purchase")
public class PurchaseVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long purchaseNum;
	@NonNull
	@Column(nullable = false)
	private int purchaseQuantity;
	@CreationTimestamp
	private Timestamp purchaseDate;
	
	@JoinColumn(name="productNum")
	@ManyToOne
	ProductVO productNum;
	
}
