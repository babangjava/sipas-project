package com.cb.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LaporanCabang {
    public LaporanCabang(String namaCabang, LocalDate tglTransaksi) {
        this.namaCabang = namaCabang;
        this.tglTransaksi = tglTransaksi;
    }

    private String namaCabang;
    private LocalDate tglTransaksi;
    private Double totalBahanBaku;
    private Double totalOmzet;
    private Double totalPengeluaran;
    private Double keuntungan;
}
