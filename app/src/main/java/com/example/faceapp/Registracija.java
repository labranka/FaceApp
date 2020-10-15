package com.example.faceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registracija extends AppCompatActivity {

    private EditText korisnikIme, korisnikLozinka, korisnikLozinka2, korisnikEmail;
    private Button regButton;
    private TextView imateNalog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);
        setupUIViews();

        firebaseAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(provera()){
                    //Ubaciti podatke u bazu
                    String user_email = korisnikEmail.getText().toString().trim();//trim je za uklanjanje praznih mesta koje korisnik napravi
                    String user_password = korisnikLozinka.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Registracija.this, "Registracija uspešna!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Registracija.this, MainActivity.class));
                            }else{
                                Toast.makeText(Registracija.this, "Registracija NEUSPEŠNA!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        imateNalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Registracija.this, MainActivity.class));
            }
        });
    }

    //Pridruzivanje promenljivih id promenljivama iz layout-a
    private void setupUIViews(){
        korisnikIme = (EditText)findViewById(R.id.etKIme);
        korisnikLozinka = (EditText)findViewById(R.id.etLozinka);
        korisnikLozinka2 = (EditText)findViewById(R.id.etLozinka2);
        korisnikEmail = (EditText)findViewById(R.id.etEmail);
        regButton = (Button)findViewById(R.id.btnKreiraj);
        imateNalog =(TextView)findViewById(R.id.tvImateNalog);
    }

    private Boolean provera(){
        Boolean rezultat = false;

        String name = korisnikIme.getText().toString();
        String sifra = korisnikLozinka.getText().toString();
        String sifra2 = korisnikLozinka2.getText().toString();
        String email = korisnikEmail.getText().toString();

        if(name.isEmpty() || sifra.isEmpty() || sifra2.isEmpty() || email.isEmpty()){
            Toast.makeText(this, "Morate uneti sve podatke!", Toast.LENGTH_SHORT).show();
        }
        else if(sifra.contentEquals(sifra2)==false){
            Toast.makeText(this, "Šifre se ne podudaraju!!", Toast.LENGTH_SHORT).show();
        }
        else if(sifra.length()<6){
            Toast.makeText(this, "Šifra mora sadržati najmanje 6 karaktera!!!", Toast.LENGTH_SHORT).show();
        }
        else{
            rezultat = true;
        }
        return rezultat;
    }
}