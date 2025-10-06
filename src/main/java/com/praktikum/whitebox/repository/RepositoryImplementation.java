package com.praktikum.whitebox.repository;

import com.praktikum.whitebox.model.Produk;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RepositoryImplementation implements RepositoryProduk {
    private final Map<String, Produk> produkMap = new ConcurrentHashMap<>();

    @Override
    public boolean simpan(Produk produk) {
        if (produk == null || produk.getKode() == null) {
            return false;
        }
        produkMap.put(produk.getKode(), produk);
        return true;
    }

    @Override
    public Optional<Produk> cariByKode(String kode) {
        return Optional.ofNullable(produkMap.get(kode));
    }

    @Override
    public List<Produk> cariByNama(String nama) {
        if (nama == null || nama.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String namaLower = nama.toLowerCase();
        List<Produk> result = new ArrayList<>();
        for (Produk produk : produkMap.values()) {
            if (produk.getNama().toLowerCase().contains(namaLower)) {
                result.add(produk);
            }
        }
        return result;
    }

    @Override
    public List<Produk> cariByKategori(String kategori) {
        if (kategori == null || kategori.trim().isEmpty()) {
            return Collections.emptyList();
        }
        String kategoriLower = kategori.toLowerCase();
        List<Produk> result = new ArrayList<>();
        for (Produk produk : produkMap.values()) {
            if (produk.getKategori().toLowerCase().contains(kategoriLower)) {
                result.add(produk);
            }
        }
        return result;
    }

    @Override
    public List<Produk> cariProdukStokMenipis() {
        List<Produk> result = new ArrayList<>();
        for (Produk produk : produkMap.values()) {
            if (produk.isStokMenipis()) {
                result.add(produk);
            }
        }
        return result;
    }

    @Override
    public List<Produk> cariProdukStokHabis() {
        List<Produk> result = new ArrayList<>();
        for (Produk produk : produkMap.values()) {
            if (produk.isStokHabis()) {
                result.add(produk);
            }
        }
        return result;
    }

    @Override
    public boolean hapus(String kode) {
        return produkMap.remove(kode) != null;
    }

    @Override
    public boolean updateStok(String kode, int stokBaru) {
        Produk produk = produkMap.get(kode);
        if (produk != null) {
            produk.setStok(stokBaru);
            return true;
        }
        return false;
    }

    @Override
    public List<Produk> cariSemua() {
        return new ArrayList<>(produkMap.values());
    }
}