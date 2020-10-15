package com.example.faceapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "korisnici.db";
    private static final int DB_VERSION = 1;

    public MySQLiteHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String upit = "CREATE TABLE korisnici (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, kIme TEXT, lozinka TEXT )";

        db.execSQL(upit);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS korisnici");

        this.onCreate(db);
    }

    public void dodajKorisnika(Korisnik korisnik){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("email", korisnik.getEmail());
        values.put("kIme", korisnik.getkIme());
        values.put("lozinka", korisnik.getLozinka());

        db.insert("korisnici", null, values);

        db.close();
    }

    public boolean postoji(Korisnik korisnik){
        String upit = "SELECT * FROM korisnici WHERE kIme = " + korisnik.getkIme() + " && lozinka = " + korisnik.getLozinka() + "LIMIT 1";



        return false;
    }
}
