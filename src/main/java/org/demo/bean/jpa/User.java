package org.demo.bean.jpa;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_management")

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotEmpty(message = "ID User tidak boleh kosong")
    @Column(name = "id_user", nullable = false)
    private String idUser;

    @NotEmpty(message = "Nama User tidak boleh kosong")
    @Column(name = "nama_user", nullable = false)
    private String namaUser;

    @NotEmpty(message = "Username tidak boleh kosong")
    @Column(name = "username", nullable = false)
    private String user;

    @NotEmpty(message = "Password tidak boleh kosong")
    @Column(name = "password", nullable = false)
    private String password;

    @NotEmpty(message = "Unit tidak boleh kosong")
    @Column(name = "unit", nullable = false)
    private String unit;

    @NotEmpty(message = "Role tidak boleh kosong")
    @Column(name = "role", nullable = false)
    private String role;

    @Transient
    private String oldPassword;

    @Transient
    private String newPassword;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
