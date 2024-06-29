package org.demo.bean.jpa;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "upload_document")
public class Uploaddocument implements Serializable {

    private static final long serialVersionUID = 1L;
    //
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UPLOADDOCUMENT_SEQ")
    @SequenceGenerator(name = "UPLOADDOCUMENT_SEQ", sequenceName = "UPLOADDOCUMENT_SEQ", allocationSize = 1)
    @Column(name = "id")
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @NotNull(message = "Unit Kerja tidak boleh kosong")
    @Column(name = "unit_kerja", length = 100)
    private String unitKerja;

    @NotNull(message = "Kategori tidak boleh kosong")
    @Column(name = "kategori", length = 250)
    private String kategori;

//    @NotNull(message = "Document tidak boleh kosong")
    @Column(name = "nama_document", length = 250)
    private String namaDocument;

//    @NotNull(message = "Judul Document tidak boleh kosong")
    @Column(name = "deskripsi", length = 250)
    private String deskripsi;

    @Column(name = "odner", length = 250)
    private String odner;



    @Lob
    @Column(name = "document")
    private byte[] data;

    @Transient
    private MultipartFile fileUpload;


    public Uploaddocument() {
    }


    public Uploaddocument(Integer id, String unitKerja, String kategori, String namaDocument, String deskripsi, String odner) {
        this.id = id;
        this.unitKerja = unitKerja;
        this.kategori = kategori;
        this.namaDocument = namaDocument;
        this.deskripsi = deskripsi;
        this.odner = odner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public MultipartFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(MultipartFile fileUpload) {
        this.fileUpload = fileUpload;
    }

    public String getUnitKerja() {
        return unitKerja;
    }

    public void setUnitKerja(String unitKerja) {
        this.unitKerja = unitKerja;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getNamaDocument() {
        return namaDocument;
    }

    public void setNamaDocument(String namaDocument) {
        this.namaDocument = namaDocument;
    }

    public String getOdner() {
        return odner;
    }

    public void setOdner(String odner) {
        this.odner = odner;
    }
}
