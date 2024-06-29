package org.demo.bean.jpa;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "menu_item")

public class Menuitem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MENUITEM_SEQ")
    @SequenceGenerator(name = "MENUITEM_SEQ", sequenceName = "MENUITEM_SEQ", allocationSize = 1)
    @Column(name = "id_menu")
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id_menu")
    private Integer idMenu;

    @NotEmpty(message = "Header tidak boleh kosong")
    @Column(name = "header", nullable = false, length = 100)
    private String header;

    @NotEmpty(message = "Detail tidak boleh kosong")
    @Column(name = "detail", nullable = false, length = 100)
    private String detail;

    public Integer getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(Integer idMenu) {
        this.idMenu = idMenu;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
