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
@Table(name="tbl_cart")
public class CartVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long cartNum;
	
	@NonNull
	@Column(nullable = false)
	private int cartCount;
	
	@NonNull
	@Column(nullable = false)
	private int cartPrice;
	
	@JoinColumn(name="productNum")
	@ManyToOne
	ProductVO productNum;	
	
	@JoinColumn(name="userPhone")
	@ManyToOne
	UserVO userPhone;


}
