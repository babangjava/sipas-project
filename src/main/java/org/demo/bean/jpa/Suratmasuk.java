package org.demo.bean.jpa;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.demo.web.common.CustomddMMMyyyyDeserializer;
import org.demo.web.common.CustomddMMMyyyySerializer;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "surat_masuk")
public class Suratmasuk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SURATMASUK_SEQ")
    @SequenceGenerator(name = "SURATMASUK_SEQ", sequenceName = "SURATMASUK_SEQ", allocationSize = 1)
    @Column(name = "id")

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
    private Integer id;

    @NotEmpty(message = "Nomor Surat tidak boleh kosong")
    @Column(name = "no_surat", nullable = false, length = 100)
    private String noSurat;

    @NotNull(message = "Tanggal Surat tidak boleh kosong")
    @JsonSerialize(using = CustomddMMMyyyySerializer.class)
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tanggal", nullable = false)
    private Date tanggal;

    @NotNull(message = "Tanggal Diterima tidak boleh kosong")
    @JsonSerialize(using = CustomddMMMyyyySerializer.class)
    @JsonDeserialize(using = CustomddMMMyyyyDeserializer.class)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "tanggal_diterima", nullable = false)
    private Date tanggalDiterima;

    @NotEmpty(message = "Asal Surat tidak boleh kosong")
    @Column(name = "asal_surat", nullable = false, length = 250)
    private String asalSurat;

    @NotEmpty(message = "Perihal tidak boleh kosong")
    @Column(name = "prihal", nullable = false, length = 250)
    private String prihal;

    @NotEmpty(message = "Tujuan Surat tidak boleh kosong")
    @Column(name = "tujuan", nullable = false, length = 250)
    private String tujuan;

    @Column(name = "odner", length = 250)
    private String odner;

    @Column(name = "document_id")
    private Integer documentId;

    @Transient
    private MultipartFile fileUpload;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNoSurat() {
        return noSurat;
    }

    public void setNoSurat(String noSurat) {
        this.noSurat = noSurat;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getPrihal() {
        return prihal;
    }

    public void setPrihal(String prihal) {
        this.prihal = prihal;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Integer documentId) {
        this.documentId = documentId;
    }

    public MultipartFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(MultipartFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public Date getTanggalDiterima() {
        return tanggalDiterima;
    }

    public void setTanggalDiterima(Date tanggalDiterima) {
        this.tanggalDiterima = tanggalDiterima;
    }

    public String getAsalSurat() {
        return asalSurat;
    }

    public void setAsalSurat(String asalSurat) {
        this.asalSurat = asalSurat;
    }

    public String getOdner() {
        return odner;
    }

    public void setOdner(String odner) {
        this.odner = odner;
    }
}
