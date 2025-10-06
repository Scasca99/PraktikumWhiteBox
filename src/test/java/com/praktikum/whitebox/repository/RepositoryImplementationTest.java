package com.praktikum.whitebox.repository;

import com.praktikum.whitebox.model.Produk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Repository Produk Implementation")
public class RepositoryImplementationTest {

    private RepositoryImplementation repository;
    private Produk produkTest;

    @BeforeEach
    void setUp() {
        repository = new RepositoryImplementation();
        produkTest = new Produk("PROD001", "Laptop Gaming", "Elektronik", 15000000, 10, 5);
    }

    @Test
    @DisplayName("simpan gagal karena kode null")
    void simpanKodeNull() {
        Produk p = new Produk(null, "Nama", "Kategori", 1000, 5, 1);
        assertFalse(repository.simpan(p));
    }

    @Test
    @DisplayName("cariByKode produk tidak ditemukan")
    void cariByKodeTidakAda() {
        assertTrue(repository.cariByKode("XYZ").isEmpty());
    }

    @Test
    @DisplayName("cariByNama nama kosong/null")
    void cariByNamaKosongNull() {
        assertTrue(repository.cariByNama(null).isEmpty());
        assertTrue(repository.cariByNama("   ").isEmpty());
    }

    @Test
    @DisplayName("cariByNama nama ditemukan")
    void cariByNamaDitemukan() {
        repository.simpan(produkTest);
        assertEquals(1, repository.cariByNama("Laptop").size());
    }

    @Test
    @DisplayName("cariByKategori kategori kosong/null")
    void cariByKategoriKosongNull() {
        assertTrue(repository.cariByKategori(null).isEmpty());
        assertTrue(repository.cariByKategori(" ").isEmpty());
    }

    @Test
    @DisplayName("cariByKategori kategori ditemukan")
    void cariByKategoriDitemukan() {
        repository.simpan(produkTest);
        assertEquals(1, repository.cariByKategori("Elektronik").size());
    }

    @Test
    @DisplayName("cariProdukStokMenipis & habis")
    void cariProdukStokMenipisHabis() {
        Produk p1 = new Produk("P1","A","X",1000,1,5);
        Produk p2 = new Produk("P2","B","X",1000,0,5);
        repository.simpan(p1);
        repository.simpan(p2);
        assertEquals(1, repository.cariProdukStokMenipis().size());
        assertEquals(1, repository.cariProdukStokHabis().size());
    }

    @Test
    @DisplayName("hapus produk berhasil & gagal")
    void hapusProduk() {
        repository.simpan(produkTest);
        assertTrue(repository.hapus("PROD001"));
        assertFalse(repository.hapus("TIDAKADA"));
    }

    @Test
    @DisplayName("updateStok berhasil & gagal")
    void updateStok() {
        repository.simpan(produkTest);
        assertTrue(repository.updateStok("PROD001", 50));
        assertFalse(repository.updateStok("TIDAKADA", 10));
    }

    @Test
    @DisplayName("cariSemua kosong & berisi")
    void cariSemua() {
        assertTrue(repository.cariSemua().isEmpty());
        repository.simpan(produkTest);
        assertFalse(repository.cariSemua().isEmpty());
    }


    @Test
    @DisplayName("Test simpan produk berhasil")
    void testSimpanProdukBerhasil() {
        boolean hasil = repository.simpan(produkTest);
        assertTrue(hasil);

        Optional<Produk> found = repository.cariByKode("PROD001");
        assertTrue(found.isPresent());
        assertEquals("Laptop Gaming", found.get().getNama());
    }

    @Test
    @DisplayName("Test simpan produk null")
    void testSimpanProdukNull() {
        boolean hasil = repository.simpan(null);
        assertFalse(hasil);
    }

    // Tambahkan test methods lainnya sesuai kebutuhan
}