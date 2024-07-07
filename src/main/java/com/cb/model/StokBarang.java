package com.cb.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Integer Qty;

    @NotNull
    @Column(nullable = false)
    private Integer stok;


}
