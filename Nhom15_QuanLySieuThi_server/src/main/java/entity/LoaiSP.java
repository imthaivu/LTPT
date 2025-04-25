/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


//Entity loại sản phẩm
@Entity
@Table(name = "LoaiSP")
public class LoaiSP implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    @Column(name = "MaLoaiSP")
    private String maLoaiSP;
    @Column(name = "TenLoaiSP", columnDefinition = "nvarchar(100)")
    private String tenLoaiSP;

    @OneToMany(mappedBy = "loaiSP")
    private List<SanPham> sanPham;

    public LoaiSP(String maLoaiSP, String tenLoaiSP) {
        this.maLoaiSP = maLoaiSP;
        this.tenLoaiSP = tenLoaiSP;
    }

    public LoaiSP() {
        //TODO Auto-generated constructor stub
    }

    public String getMaLoaiSP() {
        return maLoaiSP;
    }


    public void setMaLoaiSP(String maLoaiSP) {
        this.maLoaiSP = maLoaiSP;
    }

    public String getTenLoaiSP() {
        return tenLoaiSP;
    }

    public void setTenLoaiSP(String tenLoaiSP) {
        this.tenLoaiSP = tenLoaiSP;
    }

}
