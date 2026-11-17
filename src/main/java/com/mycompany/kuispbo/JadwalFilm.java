package com.mycompany.kuispbo;

import java.io.Serializable;

public class JadwalFilm implements Serializable {
    private static final long serialVersionUID = 1L;
    private String kode;
    private String namaFilm;
    private int kuota;
    
    public JadwalFilm(String kode, String namaFilm, int kuota) {
        this.kode = kode;
        this.namaFilm = namaFilm;
        this.kuota = kuota;
    }
    
    public String getKode() {
        return kode;
    }
    
    public String getNamaFilm() {
        return namaFilm;
    }
    
    public int getKuota() {
        return kuota;
    }
    
    public void kurangiKuota(int jumlah) {
        kuota -= jumlah;
    }
}