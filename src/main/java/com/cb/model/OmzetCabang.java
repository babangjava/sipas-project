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
@Table(name = "omzet_cabang")
public class OmzetCabang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @NotNull
    @Column(nullable = false)
    private String namaCabang;

    @Column
    private LocalDate tglTransaksi;

    @NotNull
    @Column(nullable = false)
    private Double omzet;

    @Column(nullable = true)
    private String catatan;

    @NotNull
    @Column(nullable = false)
    private Double total;

}
