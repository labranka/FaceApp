package com.example.faceapp;

public class Korisnik {
    int id;
    String email;
    String kIme;
    String lozinka;

    public Korisnik() {
    }

    public Korisnik(String email, String kIme, String lozinka) {
        this.email = email;
        this.kIme = kIme;
        this.lozinka = lozinka;
    }

    public Korisnik(int id, String email, String kIme, String lozinka) {
        this.id = id;
        this.email = email;
        this.kIme = kIme;
        this.lozinka = lozinka;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getkIme() {
        return kIme;
    }

    public void setkIme(String kIme) {
        this.kIme = kIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
