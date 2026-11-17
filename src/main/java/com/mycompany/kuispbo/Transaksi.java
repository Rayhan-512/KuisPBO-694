package com.mycompany.kuispbo;

import java.io.Serializable;

public class Transaksi implements Serializable {
    private static final long serialVersionUID = 1L;
    private Penonton penonton;
    private JadwalFilm film;
    private int jumlah;
    
    public Transaksi(Penonton penonton, JadwalFilm film, int jumlah) {
        this.penonton = penonton;
        this.film = film;
        this.jumlah = jumlah;
    }
    
    public Penonton getPenonton() {
        return penonton;
    }
    
    public JadwalFilm getFilm() {
        return film;
    }
    
    public int getJumlah() {
        return jumlah;
    }
    
    @Override
    public String toString() {
        return "Penonton: " + penonton.getNama() + " | Film: " + film.getNamaFilm() + " | Jumlah Tiket: " + jumlah;
    }
}