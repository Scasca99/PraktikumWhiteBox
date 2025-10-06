package com.praktikum.whitebox.util;

import com.praktikum.whitebox.model.Kategori;
import com.praktikum.whitebox.model.Produk;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test ValidationUtils - Complete Coverage")
public class ValidationUtilsTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "A", "AB", "INVALID_CODE_TOO_LONG_AAAA"})
    @DisplayName("Test isValidKodeProduk - invalid cases")
    void testIsValidKodeProdukInvalid(String kode) {
        assertFalse(ValidationUtils.isValidKodeProduk(kode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC", "PROD001", "A123", "CODE12345"})
    @DisplayName("Test isValidKodeProduk - valid cases")
    void testIsValidKodeProdukValid(String kode) {
        assertTrue(ValidationUtils.isValidKodeProduk(kode));
    }

    // 🔧 pakai MethodSource karena butuh .repeat()
    @ParameterizedTest
    @MethodSource("invalidNamaProvider")
    @DisplayName("Test isValidNama - invalid cases")
    void testIsValidNamaInvalid(String nama) {
        assertFalse(ValidationUtils.isValidNama(nama));
    }

    static Stream<String> invalidNamaProvider() {
        return Stream.of("  ", "A", "AB", "N".repeat(101));
    }

    @ParameterizedTest
    @MethodSource("validNamaProvider")
    @DisplayName("Test isValidNama - valid cases")
    void testIsValidNamaValid(String nama) {
        assertTrue(ValidationUtils.isValidNama(nama));
    }

    static Stream<String> validNamaProvider() {
        return Stream.of("ABC", "Laptop Gaming", "N".repeat(100), "N".repeat(50));
    }

    @Test
    @DisplayName("Test isValidHarga - invalid cases")
    void testIsValidHargaInvalid() {
        assertFalse(ValidationUtils.isValidHarga(0));
        assertFalse(ValidationUtils.isValidHarga(-1000));
    }

    @Test
    @DisplayName("Test isValidHarga - valid cases")
    void testIsValidHargaValid() {
        assertTrue(ValidationUtils.isValidHarga(1000));
        assertTrue(ValidationUtils.isValidHarga(0.01));
    }

    @Test
    @DisplayName("Test isValidStok - invalid cases")
    void testIsValidStokInvalid() {
        assertFalse(ValidationUtils.isValidStok(-1));
    }

    @Test
    @DisplayName("Test isValidStok - valid cases")
    void testIsValidStokValid() {
        assertTrue(ValidationUtils.isValidStok(0));
        assertTrue(ValidationUtils.isValidStok(100));
    }

    @Test
    @DisplayName("Test isValidStokMinimum - invalid cases")
    void testIsValidStokMinimumInvalid() {
        assertFalse(ValidationUtils.isValidStokMinimum(-1));
    }

    @Test
    @DisplayName("Test isValidStokMinimum - valid cases")
    void testIsValidStokMinimumValid() {
        assertTrue(ValidationUtils.isValidStokMinimum(0));
        assertTrue(ValidationUtils.isValidStokMinimum(10));
    }

    @Test
    @DisplayName("Test isValidProduk - null produk")
    void testIsValidProdukNull() {
        assertFalse(ValidationUtils.isValidProduk(null));
    }

    @Test
    @DisplayName("Test isValidProduk - invalid produk")
    void testIsValidProdukInvalid() {
        Produk invalidProduk = new Produk("AB", "Laptop", "Elektronik", -1000, -5, -1);
        assertFalse(ValidationUtils.isValidProduk(invalidProduk));
    }

    @Test
    @DisplayName("Test isValidProduk - valid produk")
    void testIsValidProdukValid() {
        Produk validProduk = new Produk("PROD001", "Laptop Gaming", "Elektronik", 15000000, 10, 5);
        assertTrue(ValidationUtils.isValidProduk(validProduk));
    }

    @Test
    @DisplayName("Test isValidKategori - null kategori")
    void testIsValidKategoriNull() {
        assertFalse(ValidationUtils.isValidKategori(null));
    }

    @Test
    @DisplayName("Test isValidKategori - invalid kategori")
    void testIsValidKategoriInvalid() {
        Kategori invalidKategori = new Kategori("AB", "Cat", "Deskripsi yang terlalu panjang".repeat(20));
        assertFalse(ValidationUtils.isValidKategori(invalidKategori));
    }

    @Test
    @DisplayName("Test isValidKategori - valid kategori dengan deskripsi null")
    void testIsValidKategoriValidWithNullDeskripsi() {
        Kategori validKategori = new Kategori("CAT001", "Elektronik", null);
        assertTrue(ValidationUtils.isValidKategori(validKategori));
    }

    @Test
    @DisplayName("Test isValidKategori - valid kategori dengan deskripsi normal")
    void testIsValidKategoriValidWithDeskripsi() {
        Kategori validKategori = new Kategori("CAT001", "Elektronik", "Deskripsi normal");
        assertTrue(ValidationUtils.isValidKategori(validKategori));
    }

    @ParameterizedTest
    @CsvSource({
            "-1, false",
            "0, true",
            "50, true",
            "100, true",
            "101, false"
    })
    @DisplayName("Test isValidPersentase - berbagai kasus")
    void testIsValidPersentase(double persentase, boolean expected) {
        assertEquals(expected, ValidationUtils.isValidPersentase(persentase));
    }

    @Test
    @DisplayName("Test isValidKuantitas - invalid cases")
    void testIsValidKuantitasInvalid() {
        assertFalse(ValidationUtils.isValidKuantitas(0));
        assertFalse(ValidationUtils.isValidKuantitas(-5));
    }

    @Test
    @DisplayName("Test isValidKuantitas - valid cases")
    void testIsValidKuantitasValid() {
        assertTrue(ValidationUtils.isValidKuantitas(1));
        assertTrue(ValidationUtils.isValidKuantitas(100));
    }
}
