package com.mycompany.kuispbo;

import java.io.*;
import java.util.*;

public class SistemBioskop implements Serializable {
    private static final long serialVersionUID = 1L;
    private String namaBioskop;
    private HashMap<String, Penonton> daftarPenonton;
    private HashMap<String, JadwalFilm> daftarFilm;
    private ArrayList<Transaksi> daftarTransaksi;
    
    public SistemBioskop(String namaBioskop) {
        this.namaBioskop = namaBioskop;
        daftarPenonton = new HashMap<>();
        daftarFilm = new HashMap<>();
        daftarTransaksi = new ArrayList<>();
    }
    
    public void tambahPenonton(String id, String nama) {
        daftarPenonton.put(id, new Penonton(id, nama));
    }
    
    public void tambahJadwalFilm(String kode, String namaFilm, int kuota) {
        daftarFilm.put(kode, new JadwalFilm(kode, namaFilm, kuota));
    }
    
    public void jalankanTransaksiDariFile(String namaFile) throws IOException {
        StringBuilder isiFile = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(namaFile))) {
            String baris;
            while ((baris = br.readLine()) != null) {
                isiFile.append(baris);
            }
        }
        
        System.out.println("\n--- HASIL PEMROSESAN TRANSAKSI ---");
        String[] dataTransaksi = isiFile.toString().split("\\|");
        
        for (String data : dataTransaksi) {
            if (data.trim().isEmpty()) {
                continue;
            }
            
            try {
                String[] atribut = data.split(",");
                String idPenonton = "";
                String kodeFilm = "";
                int jumlah = 0;
                
                for (String bagian : atribut) {
                    String[] nilai = bagian.split("=", 2);
                    
                    if (nilai.length != 2) {
                        throw new TransaksiException("Format atribut tidak valid!");
                    }
                    
                    String kunci = nilai[0].trim();
                    String value = nilai[1].trim();
                    
                    switch (kunci) {
                        case "ID": idPenonton = value;
                            break;
                            
                        case "KD": kodeFilm = value;
                            break;
                            
                        case "JML": jumlah = Integer.parseInt(value);
                            break;
                            
                        default: throw new TransaksiException("Atribut tidak dikenal: " + kunci);
                    }
                }
                
                if (idPenonton.isEmpty() || kodeFilm.isEmpty() || jumlah <= 0) {
                    throw new TransaksiException("Data transaksi tidak lengkap atau jumlah tidak valid!");
                }
                
                Penonton penonton = daftarPenonton.get(idPenonton);
                
                if (penonton == null) {
                    throw new TransaksiException("Penonton dengan ID " + idPenonton + " tidak ditemukan!");
                }
                
                JadwalFilm film = daftarFilm.get(kodeFilm);
                
                if (film == null) {
                    throw new TransaksiException("Film dengan kode " + kodeFilm + " tidak ditemukan!");
                }
                
                if (jumlah > film.getKuota()) {
                    throw new TransaksiException("Kuota film " + film.getNamaFilm() + " tidak mencukupi!");
                }
                
                Transaksi transaksi = new Transaksi(penonton, film, jumlah);
                daftarTransaksi.add(transaksi);
                film.kurangiKuota(jumlah);
                System.out.println("[BERHASIL] " + transaksi);
                
            } catch (Exception e) {
                System.out.println("[GAGAL] " + e.getMessage());
            }
        }
        
        System.out.println("\nTotal transaksi berhasil: "
                + daftarTransaksi.size());
    }
    
    public void tampilkanPenonton() {
        System.out.println("\n--- DAFTAR PENONTON ---");
        for (Penonton p : daftarPenonton.values()) {
            System.out.println(p.tampilkanIdentitas());
        }
    }
    
    public void tampilkanKuotaFilm() {
        System.out.println("\n--- SISA KUOTA FILM ---");
        for (JadwalFilm film : daftarFilm.values()) {
            System.out.println(film.getKode() + " - " + film.getNamaFilm() + " : " + film.getKuota());
        }
    }
    
    public void simpanLaporanAkhir() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("record.bin"))) {
            oos.writeObject(this);
            System.out.println("\n[BERHASIL] Serialization disimpan ke record.bin");
            
        } catch (IOException e) {     
            System.out.println("[ERROR] Serialization gagal: " + e.getMessage());
        }
        
        try (FileWriter writer = new FileWriter("laporan.txt")) {
            writer.write("LAPORAN RIWAYAT TRANSAKSI BIOSKOP\n");
            writer.write("Nama Bioskop: " + namaBioskop + "\n");
            writer.write("====================================\n");
            
            for (int i = daftarTransaksi.size() - 1;
                    i >= 0; i--) {
                writer.write((daftarTransaksi.size() - i) + ". " + daftarTransaksi.get(i) + "\n");
            }
            writer.write("====================================\n");
            System.out.println("[BERHASIL] Laporan disimpan ke laporan.txt");
            
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal membuat laporan: " + e.getMessage());
        }
    }
}