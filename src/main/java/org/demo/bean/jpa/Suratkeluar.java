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
@Table(name = "surat_keluar")
public class Suratkeluar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SURATKELUAR_SEQ")
    @SequenceGenerator(name = "SURATKELUAR_SEQ", sequenceName = "SURATKELUAR_SEQ", allocationSize = 1)
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

    @NotEmpty(message = "Unit Kerja tidak boleh kosong")
    @Column(name = "departement", nullable = false, length = 100)
    private String departement;

    @NotEmpty(message = "Pembuat tidak boleh kosong")
    @Column(name = "pembuat_surat", nullable = false, length = 100)
    private String pembuatSurat;

    @NotEmpty(message = "Perihal tidak boleh kosong")
    @Column(name = "prihal", nullable = false, length = 250)
    private String prihal;

    @NotEmpty(message = "Tujuan tidak boleh kosong")
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

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public String getPembuatSurat() {
        return pembuatSurat;
    }

    public void setPembuatSurat(String pembuatSurat) {
        this.pembuatSurat = pembuatSurat;
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

    public String getOdner() {
        return odner;
    }

    public void setOdner(String odner) {
        this.odner = odner;
    }
}
