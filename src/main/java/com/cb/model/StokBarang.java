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
@Table(name = "stok_barang")
public class StokBarang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotNull
    @Column(nullable = false)
    private String namaGudang;

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
    private String type;

    @NotNull
    @Column(nullable = false)
    private Double harga;

    @NotNull
    @Column(nullable = false)
    private Integer qty;

    @Column(nullable = false)
    private Integer stok;


}
