package com.cb.controller;

import com.cb.model.StokBarang;
import com.cb.model.TransaksiBahanBakuCabang;

import java.util.ArrayList;
import java.util.List;

public class TestUpdateStok {

    public TestUpdateStok() {
        TransaksiBahanBakuCabang obj =new TransaksiBahanBakuCabang();
        obj.setQty(20);
        List<StokBarang> stokBarangExisting = new ArrayList<StokBarang>();
        StokBarang stokBarang1 = new StokBarang();
        StokBarang stokBarang2 = new StokBarang();

        stokBarang1.setStok(25);
        stokBarang2.setStok(1);

        stokBarangExisting.add(stokBarang1);
        stokBarangExisting.add(stokBarang2);

        Integer pengambilanBahanBaku = obj.getQty();
        for (int i = stokBarangExisting.size() - 1; i >= 0; i--) {
            if(stokBarangExisting.get(i).getStok()>=0) {
                if (pengambilanBahanBaku > 0) {
                    if (stokBarangExisting.get(i).getStok() >= pengambilanBahanBaku) {
                        stokBarangExisting.get(i).setStok(stokBarangExisting.get(i).getStok() - pengambilanBahanBaku);
                        pengambilanBahanBaku = 0;
                    } else {
                        pengambilanBahanBaku -= stokBarangExisting.get(i).getStok();
                        stokBarangExisting.get(i).setStok(0);
                    }
                }
            }
        }

        for (StokBarang stokBarang : stokBarangExisting) {

        }
    }



    public static void main(String[] args) {
        new TestUpdateStok();
    }
}
