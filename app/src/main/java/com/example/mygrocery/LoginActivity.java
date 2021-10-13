package com.example.mygrocery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText userName,userPassword;
    Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findByIds();
    }
    private void findByIds(){
     userName=findViewById(R.id.sign_in_txt_edit_txt_email);
     userPassword=findViewById(R.id.sign_in_txt_edit_txt_password);
     signInButton=findViewById(R.id.sign_in_btn_SignIn);
    }

    public void signIn(View view){
     view.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(userName.getText().toString().contentEquals("abc") && userPassword.getText().toString().contentEquals("abc")){
                 Toast.makeText(view.getContext(),"Login Success  !", Toast.LENGTH_LONG).show();

                 startActivity(new Intent(getApplicationContext(),MainActivity.class));
             }else{
                 Toast.makeText(view.getContext(),"Try ...UserName:abc,Password:abc", Toast.LENGTH_LONG).show();
             }
         }
     });
    }
}
