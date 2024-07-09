package com.cb.controller;

import com.cb.model.StokBarang;

import java.util.ArrayList;
import java.util.List;

public class TestUpdateStok {

    public TestUpdateStok() {
        List<StokBarang> stokBarangExisting = new ArrayList<StokBarang>();
        StokBarang stokBarang1 = new StokBarang();
        StokBarang stokBarang2 = new StokBarang();
        StokBarang stokBarang3 = new StokBarang();

        stokBarang1.setStok(0);
        stokBarang2.setStok(10);
        stokBarang3.setStok(30);

        stokBarangExisting.add(stokBarang1);
        stokBarangExisting.add(stokBarang2);
        stokBarangExisting.add(stokBarang3);

        stokBarangExisting = stokBarangExisting;

        Integer pengambilanBahanBaku = 25;

//        Integer sisa = 0;
//        for (StokBarang stokBarang : stokBarangExisting) {
//            if (stokBarang.getStok() != 0) {
//                int minusStok = pengambilanBahanBaku - stokBarang.getStok();
//                sisa= sisa + minusStok;
//            }
//        }
//        stokBarangExisting = stokBarangExisting;

        for (int i = stokBarangExisting.size() - 1; i >= 0; i--) {
            StokBarang stokBarang = stokBarangExisting.get(i);
            if (pengambilanBahanBaku > 0) {
                if (stokBarang.getStok() >= pengambilanBahanBaku) {
                    stokBarang.setStok(stokBarang.getStok() - pengambilanBahanBaku);
                    pengambilanBahanBaku = 0;
                } else {
                    pengambilanBahanBaku -= stokBarang.getStok();
                    stokBarang.setStok(0);
                }
            }
        }

        // Menampilkan hasil akhir stok untuk verifikasi
        for (StokBarang stokBarang : stokBarangExisting) {
            System.out.println("Stok akhir: " + stokBarang.getStok());
        }
    }



    public static void main(String[] args) {
        new TestUpdateStok();
    }
}
