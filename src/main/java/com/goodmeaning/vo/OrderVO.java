package com.goodmeaning.vo;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
	private Long order_num;
	@NonNull
	@Column(nullable = false)
	private Date order_date;
	@NonNull
	@Column(nullable = false)
	private String delivery_recipient;
	@NonNull
	@Column(nullable = false)
	private String deivery_phone;
	@NonNull
	@Column(nullable = false)
	private String postcode;
	@NonNull
	@Column(nullable = false)
	private String address;
	
	private String address_detail;
	@NonNull
	@Column(nullable = false)
	private int order_total_price;
	@NonNull
	@Column(nullable = false)
	private String order_status;
	
	@JsonIgnore
	@ManyToOne
	UserVO userphone;
	
	//
}
