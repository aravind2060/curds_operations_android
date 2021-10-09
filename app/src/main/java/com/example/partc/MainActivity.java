package com.example.partc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    private Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://partcbackend.herokuapp.com/products/")
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();
    private ProductsRepository repository=retrofit.create(ProductsRepository.class);

    private SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findByIds();


    }

    public void findByIds(){
        seekBar=findViewById(R.id.seekbar);
    }


    public void createProduct(ProductEntity product){
        Call<ProductEntity> call=repository.addProduct(product);
        call.enqueue(new Callback<ProductEntity>() {
            @Override
            public void onResponse(Call<ProductEntity> call, Response<ProductEntity> response) {
                System.out.println(call.request());
                System.out.println("add Product Success! \n"+response.body());
            }

            @Override
            public void onFailure(Call<ProductEntity> call, Throwable t) {
                System.out.println(call.request().body());
                System.out.println("add Product failed : "+t.getMessage());
            }
        });

    }
    public void updateProduct(ProductEntity product){

        Call<ProductEntity> call=repository.updateProduct(product);
        call.enqueue(new Callback<ProductEntity>() {
            @Override
            public void onResponse(Call<ProductEntity> call, Response<ProductEntity> response) {
                System.out.println("update success! \n"+response.body());
            }

            @Override
            public void onFailure(Call<ProductEntity> call, Throwable t) {
                System.out.println(call.request().body());
                System.out.println("Failure: update \n"+t.getMessage());
            }
        });
    }

    public void getProducts(){
        Call<ProductResponse> call=repository.getAllProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                System.out.println("Success!\n"+response.body().getProducts().get(0));
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                System.out.println("Failure:\n"+t.getMessage());
            }
        });
    }

    public void deleteProduct(int productId){

        Call<Boolean> call=repository.deleteProduct(productId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println(call.request());
                System.out.println("Deletion is Success:\n"+response.body());
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }
}
