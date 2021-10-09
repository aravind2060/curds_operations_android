package com.example.partc;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ProductsRepository {

    //create operation
    @POST("addproduct")
    Call<ProductEntity> addProduct(@Body ProductEntity productEntity);

    //update Operation
    @PUT("updateproduct")
    Call<ProductEntity> updateProduct(@Body ProductEntity productEntity);

    //read Operation
    @GET("getallproducts")
    @Headers({"ACCEPT:application/xml"})
    Call<ProductResponse> getAllProducts();

    //delete Operation
    @DELETE("deleteproduct")
    Call<Boolean> deleteProduct(@Query("productId") int productId);
}
