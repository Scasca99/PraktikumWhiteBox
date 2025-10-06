package com.praktikum.whitebox.service;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.repository.RepositoryProduk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service Inventaris - Complete Coverage")
public class ServiceInventarisTest {

    @Mock
    private RepositoryProduk mockRepositoryProduk;

    private ServiceInventaris serviceInventaris;
    private Produk produkTest;

    @BeforeEach
    void setUp() {
        serviceInventaris = new ServiceInventaris(mockRepositoryProduk);
        produkTest = new Produk("PROD001", "Laptop Gaming", "Elektronik", 15000000, 10, 5);
    }

    // ===== TAMBAH PRODUK =====
    @Test
    @DisplayName("Tambah produk berhasil - semua kondisi valid")
    void testTambahProdukBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        when(mockRepositoryProduk.simpan(produkTest)).thenReturn(true);

        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        assertTrue(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).simpan(produkTest);
    }

    @Test
    @DisplayName("Tambah produk gagal - produk sudah ada")
    void testTambahProdukGagalSudahAda() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).simpan(any(Produk.class));
    }

    @Test
    @DisplayName("Tambah produk gagal - validasi produk gagal")
    void testTambahProdukGagalValidasi() {
        Produk invalidProduk = new Produk("AB", "L", "E", -1000, -5, -1);

        boolean hasil = serviceInventaris.tambahProduk(invalidProduk);

        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
        verify(mockRepositoryProduk, never()).simpan(any(Produk.class));
    }

    @Test
    @DisplayName("Tambah produk gagal - repository simpan gagal")
    void testTambahProdukGagalSimpan() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());
        when(mockRepositoryProduk.simpan(produkTest)).thenReturn(false);

        boolean hasil = serviceInventaris.tambahProduk(produkTest);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).simpan(produkTest);
    }

    // ===== HAPUS PRODUK =====
    @Test
    @DisplayName("Hapus produk berhasil")
    void testHapusProdukBerhasil() {
        Produk produkTanpaStok = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 0, 2);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTanpaStok));
        when(mockRepositoryProduk.hapus("PROD001")).thenReturn(true);

        boolean hasil = serviceInventaris.hapusProduk("PROD001");

        assertTrue(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).hapus("PROD001");
    }

    @Test
    @DisplayName("Hapus produk gagal - kode produk invalid")
    void testHapusProdukInvalidKode() {
        boolean hasil = serviceInventaris.hapusProduk("AB");

        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
        verify(mockRepositoryProduk, never()).hapus(anyString());
    }

    @Test
    @DisplayName("Hapus produk gagal - produk tidak ditemukan")
    void testHapusProdukNotFound() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());

        boolean hasil = serviceInventaris.hapusProduk("PROD001");

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).hapus(anyString());
    }

    @Test
    @DisplayName("Hapus produk gagal - stok masih ada")
    void testHapusProdukStokMasihAda() {
        Produk produkDenganStok = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 5, 2);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkDenganStok));

        boolean hasil = serviceInventaris.hapusProduk("PROD001");

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).hapus(anyString());
    }

    @Test
    @DisplayName("Hapus produk gagal - repository hapus gagal")
    void testHapusProdukGagalHapus() {
        Produk produkTanpaStok = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 0, 2);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTanpaStok));
        when(mockRepositoryProduk.hapus("PROD001")).thenReturn(false);

        boolean hasil = serviceInventaris.hapusProduk("PROD001");

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).hapus("PROD001");
    }

    // ===== CARI PRODUK =====
    @Test
    @DisplayName("Cari produk by kode berhasil")
    void testCariProdukByKodeBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("PROD001");

        assertTrue(hasil.isPresent());
        assertEquals("PROD001", hasil.get().getKode());
        verify(mockRepositoryProduk).cariByKode("PROD001");
    }

    @Test
    @DisplayName("Cari produk by kode - invalid kode")
    void testCariProdukByKodeInvalid() {
        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("AB");

        assertTrue(hasil.isEmpty());
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
    }

    @Test
    @DisplayName("Cari produk by kode - tidak ditemukan")
    void testCariProdukByKodeNotFound() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());

        Optional<Produk> hasil = serviceInventaris.cariProdukByKode("PROD001");

        assertTrue(hasil.isEmpty());
        verify(mockRepositoryProduk).cariByKode("PROD001");
    }

    @Test
    @DisplayName("Cari produk by nama")
    void testCariProdukByNama() {
        List<Produk> expectedProduk = Arrays.asList(produkTest);
        when(mockRepositoryProduk.cariByNama("Laptop")).thenReturn(expectedProduk);

        List<Produk> hasil = serviceInventaris.cariProdukByNama("Laptop");

        assertEquals(expectedProduk, hasil);
        verify(mockRepositoryProduk).cariByNama("Laptop");
    }

    @Test
    @DisplayName("Cari produk by kategori")
    void testCariProdukByKategori() {
        List<Produk> expectedProduk = Arrays.asList(produkTest);
        when(mockRepositoryProduk.cariByKategori("Elektronik")).thenReturn(expectedProduk);

        List<Produk> hasil = serviceInventaris.cariProdukByKategori("Elektronik");

        assertEquals(expectedProduk, hasil);
        verify(mockRepositoryProduk).cariByKategori("Elektronik");
    }

    // ===== UPDATE STOK =====
    @Test
    @DisplayName("Update stok berhasil")
    void testUpdateStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 20)).thenReturn(true);

        boolean hasil = serviceInventaris.updateStok("PROD001", 20);

        assertTrue(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).updateStok("PROD001", 20);
    }

    @Test
    @DisplayName("Update stok gagal - invalid parameter")
    void testUpdateStokInvalidParameter() {
        boolean hasil = serviceInventaris.updateStok("AB", -5);

        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Update stok gagal - produk tidak ditemukan")
    void testUpdateStokProdukNotFound() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());

        boolean hasil = serviceInventaris.updateStok("PROD001", 10);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Update stok gagal - repository update gagal")
    void testUpdateStokGagalUpdate() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 20)).thenReturn(false);

        boolean hasil = serviceInventaris.updateStok("PROD001", 20);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk).updateStok("PROD001", 20);
    }

    // ===== KELUAR STOK =====
    @Test
    @DisplayName("Keluar stok berhasil - stok mencukupi")
    void testKeluarStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 5)).thenReturn(true);

        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);

        assertTrue(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 5);
    }

    @Test
    @DisplayName("Keluar stok gagal - invalid parameter")
    void testKeluarStokInvalidParameter() {
        boolean hasil = serviceInventaris.keluarStok("AB", -5);

        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
    }

    @Test
    @DisplayName("Keluar stok gagal - produk tidak ditemukan")
    void testKeluarStokProdukNotFound() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());

        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Keluar stok gagal - produk tidak aktif")
    void testKeluarStokProdukTidakAktif() {
        produkTest.setAktif(false);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Keluar stok gagal - stok tidak mencukupi")
    void testKeluarStokGagalStokTidakMencukupi() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.keluarStok("PROD001", 15);

        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Keluar stok gagal - repository update gagal")
    void testKeluarStokGagalUpdate() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 5)).thenReturn(false);

        boolean hasil = serviceInventaris.keluarStok("PROD001", 5);

        assertFalse(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 5);
    }

    // ===== MASUK STOK =====
    @Test
    @DisplayName("Masuk stok berhasil")
    void testMasukStokBerhasil() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 15)).thenReturn(true);

        boolean hasil = serviceInventaris.masukStok("PROD001", 5);

        assertTrue(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 15);
    }

    @Test
    @DisplayName("Masuk stok gagal - invalid parameter")
    void testMasukStokInvalidParameter() {
        boolean hasil = serviceInventaris.masukStok("AB", -5);

        assertFalse(hasil);
        verify(mockRepositoryProduk, never()).cariByKode(anyString());
    }

    @Test
    @DisplayName("Masuk stok gagal - produk tidak ditemukan")
    void testMasukStokProdukNotFound() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.empty());

        boolean hasil = serviceInventaris.masukStok("PROD001", 5);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Masuk stok gagal - produk tidak aktif")
    void testMasukStokProdukTidakAktif() {
        produkTest.setAktif(false);
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));

        boolean hasil = serviceInventaris.masukStok("PROD001", 5);

        assertFalse(hasil);
        verify(mockRepositoryProduk).cariByKode("PROD001");
        verify(mockRepositoryProduk, never()).updateStok(anyString(), anyInt());
    }

    @Test
    @DisplayName("Masuk stok gagal - repository update gagal")
    void testMasukStokGagalUpdate() {
        when(mockRepositoryProduk.cariByKode("PROD001")).thenReturn(Optional.of(produkTest));
        when(mockRepositoryProduk.updateStok("PROD001", 15)).thenReturn(false);

        boolean hasil = serviceInventaris.masukStok("PROD001", 5);

        assertFalse(hasil);
        verify(mockRepositoryProduk).updateStok("PROD001", 15);
    }

    // ===== LAPORAN & STATISTIK =====
    @Test
    @DisplayName("Get produk stok menipis")
    void testGetProdukStokMenipis() {
        Produk produkStokMenipis = new Produk("PROD002", "Mouse", "Elektronik", 500000, 3, 5);
        List<Produk> produkMenipis = Collections.singletonList(produkStokMenipis);
        when(mockRepositoryProduk.cariProdukStokMenipis()).thenReturn(produkMenipis);

        List<Produk> hasil = serviceInventaris.getProdukStokMenipis();

        assertEquals(1, hasil.size());
        assertEquals("PROD002", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokMenipis();
    }

    @Test
    @DisplayName("Get produk stok habis")
    void testGetProdukStokHabis() {
        Produk produkStokHabis = new Produk("PROD003", "Keyboard", "Elektronik", 300000, 0, 5);
        List<Produk> produkHabis = Collections.singletonList(produkStokHabis);
        when(mockRepositoryProduk.cariProdukStokHabis()).thenReturn(produkHabis);

        List<Produk> hasil = serviceInventaris.getProdukStokHabis();

        assertEquals(1, hasil.size());
        assertEquals("PROD003", hasil.get(0).getKode());
        verify(mockRepositoryProduk).cariProdukStokHabis();
    }

    @Test
    @DisplayName("Hitung total nilai inventaris")
    void testHitungTotalNilaiInventaris() {
        Produk produk1 = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 2, 1);
        Produk produk2 = new Produk("PROD002", "Mouse", "Elektronik", 500000, 5, 2);
        Produk produkNonAktif = new Produk("PROD003", "Keyboard", "Elektronik", 300000, 3, 1);
        produkNonAktif.setAktif(false);

        List<Produk> semuaProduk = Arrays.asList(produk1, produk2, produkNonAktif);
        when(mockRepositoryProduk.cariSemua()).thenReturn(semuaProduk);

        double totalNilai = serviceInventaris.hitungTotalNilaiInventaris();

        double expected = (10000000 * 2) + (500000 * 5); // hanya produk aktif
        assertEquals(expected, totalNilai, 0.001);
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Hitung total nilai inventaris - empty list")
    void testHitungTotalNilaiInventarisEmpty() {
        when(mockRepositoryProduk.cariSemua()).thenReturn(Collections.emptyList());

        double totalNilai = serviceInventaris.hitungTotalNilaiInventaris();

        assertEquals(0.0, totalNilai, 0.001);
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Hitung total stok")
    void testHitungTotalStok() {
        Produk produk1 = new Produk("PROD001", "Laptop", "Elektronik", 10000000, 2, 1);
        Produk produk2 = new Produk("PROD002", "Mouse", "Elektronik", 500000, 5, 2);
        Produk produkNonAktif = new Produk("PROD003", "Keyboard", "Elektronik", 300000, 3, 1);
        produkNonAktif.setAktif(false);

        List<Produk> semuaProduk = Arrays.asList(produk1, produk2, produkNonAktif);
        when(mockRepositoryProduk.cariSemua()).thenReturn(semuaProduk);

        int totalStok = serviceInventaris.hitungTotalStok();

        int expected = 2 + 5; // hanya produk aktif
        assertEquals(expected, totalStok);
        verify(mockRepositoryProduk).cariSemua();
    }

    @Test
    @DisplayName("Hitung total stok - empty list")
    void testHitungTotalStokEmpty() {
        when(mockRepositoryProduk.cariSemua()).thenReturn(Collections.emptyList());

        int totalStok = serviceInventaris.hitungTotalStok();

        assertEquals(0, totalStok);
        verify(mockRepositoryProduk).cariSemua();
    }
}