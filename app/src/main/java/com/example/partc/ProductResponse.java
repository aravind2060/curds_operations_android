package com.example.partc;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name = "ProductResponse")
public class ProductResponse {


    @ElementList(name = "products")
    private List<ProductEntity>products;

    public ProductResponse() {
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "ProductResponse{" +
                "products=" + products +
                '}';
    }
}
