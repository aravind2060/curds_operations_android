package com.example.partc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    /*TODO : separate repo for network calls (proper mvc)
    * Change to snackbar
    * Also instead of update button for every record
    * swipe for delete
    * */
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();
    private static Retrofit retrofit=new Retrofit.Builder()
            .baseUrl("https://partcbackend.herokuapp.com/products/")
            .client(okHttpClient)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();
    private static ProductsRepository repository=retrofit.create(ProductsRepository.class);


    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private List<ProductEntity>list=new ArrayList<>();
    private ProductsAdapter adapter;
    private float textSize=10;
    private Slider slider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findByIds();
        setRecyclerView();
    }

    public void findByIds(){
        slider=findViewById(R.id.slider);
        recyclerView=findViewById(R.id.recyclerview);
        floatingActionButton=findViewById(R.id.floatingButton);
        setSlider();
        setFloatingActionButton();
    }
    public void setRecyclerView(){
        adapter=new ProductsAdapter(MainActivity.this,textSize,list);
        getProducts();
        recyclerView.setAdapter(adapter);
    }

    public void setFloatingActionButton(){

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                Activity activity = (Activity) MainActivity.this;
                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialogbox, null);
                dialogBuilder.setView(dialogView);

                TextView text = (TextView) dialogView.findViewById(R.id.dialog_title);
                text.setText("ADD Product");

                TextInputEditText editProductName=dialogView.findViewById(R.id.edit_productName);
                TextInputEditText editProductPrice=dialogView.findViewById(R.id.edit_productPrice);



                dialogBuilder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ProductEntity productEntity=new ProductEntity(editProductName.getText().toString(),Integer.parseInt(editProductPrice.getText().toString()));

                        createProduct(productEntity);
                        list.add(productEntity);
                        adapter.notifyDataSetChanged();
                    }
                });

                dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();

                alertDialog.show();
            }
        });
    }

    public void setSlider(){
      slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
          @Override
          public void onStartTrackingTouch(@NonNull Slider slider) {


          }

          @Override
          public void onStopTrackingTouch(@NonNull Slider slider) {

              textSize=slider.getValue();
              System.out.println(textSize);
              if(textSize==0)
                adapter.textSize=15;
              else if (textSize==50)
                  adapter.textSize=20;
              else
                  adapter.textSize=25;
              adapter.notifyDataSetChanged();
          }
      });
    }
    public void createProduct(ProductEntity product){
        Call<ProductEntity> call=repository.addProduct(product);
        call.enqueue(new Callback<ProductEntity>() {
            @Override
            public void onResponse(Call<ProductEntity> call, Response<ProductEntity> response) {
                System.out.println(call.request());
                System.out.println("add Product Success! \n"+response.body());
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Add Product Success!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ProductEntity> call, Throwable t) {
                System.out.println(call.request().body());
                System.out.println("add Product failed : "+t.getMessage());
                Toast.makeText(getApplicationContext(),"Add Product Failed!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void updateProduct(Context context,ProductEntity product){

        Call<ProductEntity> call=repository.updateProduct(product);
        call.enqueue(new Callback<ProductEntity>() {
            @Override
            public void onResponse(Call<ProductEntity> call, Response<ProductEntity> response) {
                System.out.println("update success! \n"+product);
                Toast.makeText(context,"Update Success!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ProductEntity> call, Throwable t) {
                System.out.println(call.request().body());
                System.out.println("Failure: update \n"+t.getMessage());
                Toast.makeText(context,"Update Failed!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<ProductEntity> getProducts(){

        Call<ProductResponse> call=repository.getAllProducts();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                System.out.println("Success!\n"+response.body().getProducts());
                list.addAll(response.body().getProducts());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                System.out.println("Failure:\n"+t.getMessage());
            }
        });
        return list;
    }

    public void deleteProduct(Context context,int productId){

        Call<Boolean> call=repository.deleteProduct(productId);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println(call.request());
                System.out.println("Deletion is Success:\n"+response.body());
                Toast.makeText(context,"Deletion success!",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }



}

class ProductsAdapter extends  RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>{

    List<ProductEntity> productEntityList=new ArrayList<ProductEntity>();
    float textSize=18;
    MainActivity mainActivity=new MainActivity();
    ProductViewHolder productViewHolder;

    Context context;
    public ProductsAdapter(Context context,float textSize,List<ProductEntity> productEntities){
        this.context=context;
        this.textSize=textSize;
        this.productEntityList=productEntities;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productViewHolder=new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.displayproducts,parent,false));
    return productViewHolder;
    }

    public  void changeTextSize(float textSize){
        productViewHolder.productPrice.setTextSize(textSize);
        productViewHolder.productName.setTextSize(textSize);
    }
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        holder.productName.setTextSize(textSize);
        holder.productPrice.setTextSize(textSize);

        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                Activity activity = (Activity) context;
                LayoutInflater inflater = activity.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialogbox, null);
                dialogBuilder.setView(dialogView);

                TextView text = (TextView) dialogView.findViewById(R.id.dialog_title);
                text.setText("Update Product");

                EditText editProductName=dialogView.findViewById(R.id.edit_productName);
                EditText editProductPrice=dialogView.findViewById(R.id.edit_productPrice);

                editProductName.setText(productEntityList.get(position).getProductName());
                editProductPrice.setText(""+productEntityList.get(position).getProductPrice());

                dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProductEntity productEntity=productEntityList.get(position);
                        productEntity.setProductName(editProductName.getText().toString());
                        productEntity.setProductPrice(Integer.parseInt(editProductPrice.getText().toString()));
                        mainActivity.updateProduct(context,productEntity);
                        notifyDataSetChanged();
                    }
                });

                dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = dialogBuilder.create();

                alertDialog.show();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 mainActivity.deleteProduct(context,productEntityList.get(position).getProductId());
                 productEntityList.remove(position);
                 notifyDataSetChanged();
            }
        });
        holder.productName.setText(productEntityList.get(position).getProductName());
        holder.productPrice.setText(""+productEntityList.get(position).getProductPrice());
    }

    @Override
    public int getItemCount() {
        return productEntityList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {

       public ImageButton updateButton,deleteButton;
       public TextView productName,productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            updateButton=itemView.findViewById(R.id.updateProduct);
            deleteButton=itemView.findViewById(R.id.deleteProduct);
            productName=itemView.findViewById(R.id.productName);
            productPrice=itemView.findViewById(R.id.productPrice);

        }
    }
}

