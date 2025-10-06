package com.praktikum.whitebox.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test Class Kategori - Complete Coverage")
public class KategoriTest {

    private Kategori kategori;

    @BeforeEach
    void setUp() {
        kategori = new Kategori("CAT001", "Elektronik", "Kategori untuk produk elektronik");
    }

    @Test
    @DisplayName("Test constructor default")
    void testDefaultConstructor() {
        Kategori defaultKategori = new Kategori();
        assertNull(defaultKategori.getKode());
        assertNull(defaultKategori.getNama());
        assertNull(defaultKategori.getDeskripsi());
        assertFalse(defaultKategori.isAktif());
    }

    @Test
    @DisplayName("Test constructor dengan parameter")
    void testParameterizedConstructor() {
        assertEquals("CAT001", kategori.getKode());
        assertEquals("Elektronik", kategori.getNama());
        assertEquals("Kategori untuk produk elektronik", kategori.getDeskripsi());
        assertTrue(kategori.isAktif());
    }

    @Test
    @DisplayName("Test setters dan getters")
    void testSettersAndGetters() {
        kategori.setKode("CAT002");
        kategori.setNama("Fashion");
        kategori.setDeskripsi("Kategori fashion");
        kategori.setAktif(false);

        assertEquals("CAT002", kategori.getKode());
        assertEquals("Fashion", kategori.getNama());
        assertEquals("Kategori fashion", kategori.getDeskripsi());
        assertFalse(kategori.isAktif());
    }

    @Test
    @DisplayName("Test equals - kasus sama object")
    void testEqualsSameObject() {
        assertTrue(kategori.equals(kategori));
    }

    @Test
    @DisplayName("Test equals - kasus null")
    void testEqualsNull() {
        assertFalse(kategori.equals(null));
    }

    @Test
    @DisplayName("Test equals - kasus class berbeda")
    void testEqualsDifferentClass() {
        assertFalse(kategori.equals("String object"));
    }

    @Test
    @DisplayName("Test equals - kasus kode sama")
    void testEqualsSameKode() {
        Kategori kategori2 = new Kategori("CAT001", "Nama Berbeda", "Deskripsi Berbeda");
        assertTrue(kategori.equals(kategori2));
    }

    @Test
    @DisplayName("Test equals - kasus kode berbeda")
    void testEqualsDifferentKode() {
        Kategori kategori2 = new Kategori("CAT002", "Elektronik", "Kategori untuk produk elektronik");
        assertFalse(kategori.equals(kategori2));
    }

    @Test
    @DisplayName("Test hashCode konsistensi")
    void testHashCodeConsistency() {
        Kategori kategori2 = new Kategori("CAT001", "Nama Berbeda", "Deskripsi Berbeda");
        assertEquals(kategori.hashCode(), kategori2.hashCode());
    }

    @Test
    @DisplayName("Test toString")
    void testToString() {
        String toStringResult = kategori.toString();
        assertTrue(toStringResult.contains("CAT001"));
        assertTrue(toStringResult.contains("Elektronik"));
        assertTrue(toStringResult.contains("Kategori untuk produk elektronik"));
        assertTrue(toStringResult.contains("aktif=true"));
    }
}