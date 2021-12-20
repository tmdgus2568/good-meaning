package com.goodmeaning.vo;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

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
	
	
	@CreationTimestamp
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
	
	// eager : 즉시로딩 
	// lazy : 지연로딩
	@JsonIgnore
	@OneToMany(mappedBy = "orderNum", 
			cascade = CascadeType.ALL,
			fetch = FetchType.EAGER )
	List<OrderDetailVO> details;
}
