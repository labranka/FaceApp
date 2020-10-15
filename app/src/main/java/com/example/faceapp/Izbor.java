package com.example.faceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Izbor extends AppCompatActivity {

    ImageView imgLice;
    ImageView imgBroj;
    ImageView imgEmocija;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izbor);

        imgLice = findViewById(R.id.imgLice);
        imgBroj = findViewById(R.id.imgBroj);

        imgBroj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Izbor.this, Prepoznavanje.class));
            }
        });



        imgLice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Izbor.this, OpisSlike.class);
                startActivity(intent);
            }
        });
    }
}
