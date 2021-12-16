package com.goodmeaning.vo;

import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.web.multipart.MultipartFile;

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
	private Long productNum;
	@NonNull
	@Column(nullable = false)
	private String productName;
	@Column(nullable = false)
	private int productPrice;
	private int productStock;
	@ElementCollection(targetClass = Category.class, fetch = FetchType.EAGER)
	private Set<Category> productCategory;
	@Column(nullable = false, length = 1000)
	private String productMainimg1;
	@Column(length = 1000)
	private String productMainimg2;
	@Column(length = 1000)
	private String productMainimg3;
	@Column(length = 1000)
	private String productMainimg4;
	@Column(nullable = false)
	private String productDetailimg;
	@CreationTimestamp
	private Timestamp productCreatedate;
	@UpdateTimestamp
	private Timestamp productUpdatedate;
	
	private String postingState;
	@Transient
	private MultipartFile[] uploadFile; //배열
	
//	@BatchSize(size=100)
//	@JsonIgnore
//	@OneToMany(mappedBy = "product_num",
//	cascade = CascadeType.ALL,
//	fetch = FetchType.LAZY)
//	List<ProductOptionVO> options;
}
