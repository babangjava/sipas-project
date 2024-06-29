package org.demo.bean.jpa;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "documents")
public class DocumentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTS_SEQ")
    @SequenceGenerator(name = "DOCUMENTS_SEQ", sequenceName = "DOCUMENTS_SEQ", allocationSize = 1)
    @Column(name = "id")
//    @Id
//    @GeneratedValue(strategy=GenerationType.AUTO)
//    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @Lob
    @Column(name = "document")
    private byte[] data;

    public DocumentEntity() {
    }

    public DocumentEntity(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}