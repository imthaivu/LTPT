/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


//Enity Nhà cung cấp
@Entity
@Table(name = "NhaCungCap")
public class NCC implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    @Column(name = "MaNCC")
    private String maNCC;
    @Column(name = "TenNCC", columnDefinition = "nvarchar(100)")
    private String tenNCC;
    @Column(name = "DiaChi", columnDefinition = "nvarchar(200)")
    private String diaChi;
    @Column(name = "SoDT")
    private String soDT;

    @OneToMany(mappedBy = "nhaCungCap")
    private List<SanPham> sanPham;

    public NCC(String maNCC, String tenNCC, String diaChi, String soDT) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.diaChi = diaChi;
        this.soDT = soDT;
    }

    public NCC() {
        //TODO Auto-generated constructor stub
    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDT() {
        return soDT;
    }

    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }

}
