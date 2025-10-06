package com.praktikum.whitebox.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Kalkulator Diskon - Complete Branch Coverage")
public class KalkulatorDiskonTest {

    private KalkulatorDiskon kalkulatorDiskon;

    @BeforeEach
    void setUp() {
        kalkulatorDiskon = new KalkulatorDiskon();
    }

    // Test semua branch untuk kuantitas diskon
    @ParameterizedTest
    @DisplayName("Test semua branch kuantitas diskon")
    @CsvSource({
            "1, 0.0",    // < 5: 0%
            "4, 0.0",    // < 5: 0%
            "5, 0.05",   // >= 5: 5%
            "9, 0.05",   // < 10: 5%
            "10, 0.10",  // >= 10: 10%
            "49, 0.10",  // < 50: 10%
            "50, 0.15",  // >= 50: 15%
            "99, 0.15",  // < 100: 15%
            "100, 0.20", // >= 100: 20%
            "200, 0.20"  // >= 100: 20%
    })
    void testAllQuantityBranches(int kuantitas, double expectedDiskonPersen) {
        double harga = 1000;
        String tipePelanggan = "BARU"; // 2% additional

        double diskon = kalkulatorDiskon.hitungDiskon(harga, kuantitas, tipePelanggan);
        double expectedDiskon = harga * kuantitas * (expectedDiskonPersen + 0.02);

        assertEquals(expectedDiskon, diskon, 0.001);
    }

    // Test semua branch untuk tipe pelanggan
    @ParameterizedTest
    @DisplayName("Test semua branch tipe pelanggan")
    @CsvSource({
            "PREMIUM, 0.10",
            "REGULER, 0.05",
            "BARU, 0.02",
            "TIDAK_ADA, 0.0",
            "UNKNOWN, 0.0",
            ", 0.0"
    })
    void testAllCustomerTypeBranches(String tipePelanggan, double expectedAdditionalDiskon) {
        double harga = 1000;
        int kuantitas = 5; // 5% base discount

        double diskon = kalkulatorDiskon.hitungDiskon(harga, kuantitas, tipePelanggan);
        double expectedDiskon = harga * kuantitas * (0.05 + expectedAdditionalDiskon);

        assertEquals(expectedDiskon, diskon, 0.001);
    }

    // Test boundary maximum diskon 30%
    @Test
    @DisplayName("Test maximum diskon 30%")
    void testMaximumDiscount() {
        double harga = 1000;
        int kuantitas = 100; // 20% base
        String tipePelanggan = "PREMIUM"; // 10% additional = 30% total

        double diskon = kalkulatorDiskon.hitungDiskon(harga, kuantitas, tipePelanggan);
        double expectedDiskon = harga * kuantitas * 0.30; // max 30%

        assertEquals(expectedDiskon, diskon, 0.001);
    }

    @Test
    @DisplayName("Test maximum diskon exceeded - should cap at 30%")
    void testMaximumDiscountExceeded() {
        double harga = 1000;
        int kuantitas = 200; // 20% base
        String tipePelanggan = "PREMIUM"; // 10% additional = 30% total (capped)

        double diskon = kalkulatorDiskon.hitungDiskon(harga, kuantitas, tipePelanggan);
        double expectedDiskon = harga * kuantitas * 0.30; // max 30%

        assertEquals(expectedDiskon, diskon, 0.001);
    }

    // Test edge cases untuk hitungHargaSetelahDiskon
    @Test
    @DisplayName("Test hitungHargaSetelahDiskon dengan diskon maksimum")
    void testHitungHargaSetelahDiskonMaximum() {
        double harga = 1000;
        int kuantitas = 100;
        String tipePelanggan = "PREMIUM";

        double hargaSetelahDiskon = kalkulatorDiskon.hitungHargaSetelahDiskon(harga, kuantitas, tipePelanggan);

        double totalSebelumDiskon = harga * kuantitas;
        double diskon = totalSebelumDiskon * 0.30;
        double expected = totalSebelumDiskon - diskon;

        assertEquals(expected, hargaSetelahDiskon, 0.001);
    }

    // Test semua branch untuk getKategoriDiskon
    @ParameterizedTest
    @DisplayName("Test semua branch getKategoriDiskon")
    @CsvSource({
            "-0.1, TANPA_DISKON",
            "0.0, TANPA_DISKON",
            "0.001, DISKON_RINGAN",
            "0.05, DISKON_RINGAN",
            "0.099, DISKON_RINGAN",
            "0.1, DISKON_SEDANG",
            "0.15, DISKON_SEDANG",
            "0.199, DISKON_SEDANG",
            "0.2, DISKON_BESAR",
            "0.25, DISKON_BESAR",
            "0.3, DISKON_BESAR",
            "1.0, DISKON_BESAR"
    })
    void testAllKategoriDiskonBranches(double persentaseDiskon, String expectedKategori) {
        String kategori = kalkulatorDiskon.getKategoriDiskon(persentaseDiskon);
        assertEquals(expectedKategori, kategori);
    }

    // Test exception cases
    @Test
    @DisplayName("Test hitungDiskon - harga nol")
    void testHitungDiskonHargaZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            kalkulatorDiskon.hitungDiskon(0, 5, "REGULER");
        });
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test hitungDiskon - kuantitas nol")
    void testHitungDiskonKuantitasZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            kalkulatorDiskon.hitungDiskon(1000, 0, "REGULER");
        });
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test hitungDiskon - harga negatif")
    void testHitungDiskonHargaNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            kalkulatorDiskon.hitungDiskon(-1000, 5, "REGULER");
        });
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());
    }

    @Test
    @DisplayName("Test hitungDiskon - kuantitas negatif")
    void testHitungDiskonKuantitasNegatif() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            kalkulatorDiskon.hitungDiskon(1000, -5, "REGULER");
        });
        assertEquals("Harga dan kuantitas harus positif", exception.getMessage());
    }
}