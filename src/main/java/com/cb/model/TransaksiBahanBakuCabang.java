package com.cb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static jakarta.persistence.TemporalType.DATE;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaksi_bahan_baku_cabang")
public class TransaksiBahanBakuCabang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotNull
    @Column(nullable = false)
    private String namaCabang;

    @Column
    @Temporal(DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate tglTransaksi;

    @NotEmpty
    @NotNull
    @Column(nullable = false)
    private String namaBahan;

    @NotEmpty
    @NotNull
    @Column(nullable = false)
    private String namaGudang;

    @NotNull
    @Column(nullable = false)
    private Double harga;

    @NotEmpty
    @NotNull
    @Column(nullable = false)
    private String type;

    @NotNull
    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Double total;

    @Column(nullable = true)
    private String catatan;

}
