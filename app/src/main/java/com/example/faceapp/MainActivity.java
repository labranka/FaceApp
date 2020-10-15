package com.example.faceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText etIme;
    EditText etSifra;
    TextView txtPokusaj;
    Button btnDalje;
    Button btnNovi;
    Switch swTema;

    private int brojac = 5;

    private FirebaseAuth firebaseAuth;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etIme = findViewById(R.id.etIme);
        etSifra = findViewById(R.id.etSifra);
        btnDalje = findViewById(R.id.btnDalje);
        btnNovi = findViewById(R.id.btnNovi);
        swTema = findViewById(R.id.swTema);
        txtPokusaj = findViewById(R.id.txtPokusaj);

        txtPokusaj.setText("Broj pokušaja: 5");

        etIme.addTextChangedListener(unos);
        etSifra.addTextChangedListener(unos);

        firebaseAuth = FirebaseAuth.getInstance();


        FirebaseUser korisnik = firebaseAuth.getCurrentUser();


        btnNovi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registracija.class);
                startActivity(intent);
            }
        });
        btnDalje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(etIme.getText().toString().trim(), etSifra.getText().toString().trim());
            }
        });

        sharedPreferences = this.getSharedPreferences("tema", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if(sharedPreferences.getBoolean("DanNoc",false) == true){
            swTema.setChecked(true);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        if(sharedPreferences.getBoolean("DanNoc",false) == false){
            swTema.setChecked(false);
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        swTema.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("DanNoc", true);
                    editor.commit();
                }
                else{
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("DanNoc", false);
                    editor.commit();
                }
            }
        });

    }

    private void validate(String ime, String sifra){

        firebaseAuth.signInWithEmailAndPassword(ime, sifra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Uspešno!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Izbor.class));
                } else {
                    Toast.makeText(MainActivity.this, "Neuspešno!", Toast.LENGTH_SHORT).show();
                    brojac--;
                    txtPokusaj.setText("Ostalo Vam je još: " + brojac + " pokušaja.");
                    if (brojac == 0) {
                        btnDalje.setEnabled(false);
                    }
                }
            }
        });
    }

    private TextWatcher unos = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String ime = etIme.getText().toString();
            String sifra = etSifra.getText().toString();

            btnDalje.setEnabled(!ime.isEmpty() && !sifra.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}
