package org.demo.web.common;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class ReportModel {
    @NotNull(message = "Tanggal Awal tidak boleh kosong")
    private Date tanggalAwal;

    @NotNull(message = "Tanggal Akhir tidak boleh kosong")
    private Date tanggalAkhir;

    public Date getTanggalAwal() {
        return tanggalAwal;
    }

    public void setTanggalAwal(Date tanggalAwal) {
        this.tanggalAwal = tanggalAwal;
    }

    public Date getTanggalAkhir() {
        return tanggalAkhir;
    }

    public void setTanggalAkhir(Date tanggalAkhir) {
        this.tanggalAkhir = tanggalAkhir;
    }
}
