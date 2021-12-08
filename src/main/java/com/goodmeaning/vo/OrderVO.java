package com.goodmeaning.vo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name="tbl_order")
public class OrderVO {

	@Id
	private Long orderNum;
	@NonNull
	@Column(nullable = false)
	private Date orderDate;
	@NonNull
	@Column(nullable = false)
	private String deliveryRecipient;
	@NonNull
	@Column(nullable = false)
	private String deiveryPhone;
	@NonNull
	@Column(nullable = false)
	private String postcode;
	@NonNull
	@Column(nullable = false)
	private String address;
	
	private String addressDetail;
	@NonNull
	@Column(nullable = false)
	private int orderTotalPrice;
	@NonNull
	@Column(nullable = false)
	private String orderStatus;
	
	@JoinColumn(name="userPhone")
	@ManyToOne
	UserVO userPhone;
	
	//
}
