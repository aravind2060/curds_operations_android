package com.example.partc;




import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;


@Root(name = "products")
public class ProductEntity implements Serializable {

	@Element(name = "productId")
	private int productId;
	@Element(name = "productName")
	private String productName;
	@Element(name = "productPrice")
	private int productPrice;

	public ProductEntity() {
		super();
	}

	public ProductEntity(String productName, int productPrice) {
		super();
		this.productName = productName;
		this.productPrice = productPrice;
	}

	public ProductEntity(int productId, String productName, int productPrice) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = productPrice;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	@Override
	public String toString() {
		return "ProductEntity [productId=" + productId + ", productName=" + productName + ", productPrice="
				+ productPrice + "]";
	}
	
	
}
