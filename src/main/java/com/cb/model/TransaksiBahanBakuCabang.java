package com.cb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate tglTransaksi;

    @NotEmpty
    @NotNull
    @Column(nullable = false)
    private String namaBahan;

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

    @NotNull
    @Column(nullable = false)
    private Double total;

    @Column(nullable = true)
    private String catatan;

}
