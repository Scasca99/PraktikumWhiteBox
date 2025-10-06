package com.praktikum.whitebox;

import com.praktikum.whitebox.model.Produk;
import com.praktikum.whitebox.service.KalkulatorDiskon;
import com.praktikum.whitebox.util.ValidationUtils;

public class Main {
    public static void main(String[] args) {
        System.out.println("Whitebox Testing Lab - Running...");

        // Test basic functionality
        Produk produk = new Produk("TEST001", "Test Product", "Test Category", 10000, 50, 10);
        System.out.println("Produk created: " + produk.getNama());

        // Test validation
        System.out.println("Valid kode: " + ValidationUtils.isValidKodeProduk("ABC123"));

        // Test discount calculator
        KalkulatorDiskon kalkulator = new KalkulatorDiskon();
        double diskon = kalkulator.hitungDiskon(1000, 5, "REGULER");
        System.out.println("Discount: " + diskon);
    }
}