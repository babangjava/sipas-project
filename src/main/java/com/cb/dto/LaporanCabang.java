package com.cb.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@Setter
public class LaporanCabang {
    public LaporanCabang(String namaCabang, LocalDate tglTransaksi) {
        this.namaCabang = namaCabang;
        this.tglTransaksi = tglTransaksi;
        this.bulan = tglTransaksi != null ? tglTransaksi.getMonthValue() : 0;
    }

    // Constructor for bulan
    public LaporanCabang(String namaCabang, int bulan) {
        this.namaCabang = namaCabang;
        this.bulan = bulan;
        this.tglTransaksi = LocalDate.now().withMonth(bulan);
    }


    // Method to get the month name from tglTransaksi
    public String getBulan() {
        return tglTransaksi.getMonth().getDisplayName(TextStyle.FULL, new Locale("id", "ID"));
    }

    private int bulan;
    private String namaCabang;
    private LocalDate tglTransaksi;
    private Double totalBahanBaku;
    private Double totalOmzet;
    private Double totalPengeluaran;
    private Double keuntungan;
}
