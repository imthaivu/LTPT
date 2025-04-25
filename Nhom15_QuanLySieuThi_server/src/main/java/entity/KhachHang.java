/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;


//Entity Khách hàng
@Entity
@Table(name = "KhachHang")
public class KhachHang implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(generator = "sinhMaTuDong")
    @GenericGenerator(name = "sinhMaTuDong",
            parameters = @Parameter(name = "prefix", value = "KH"),
            strategy = "generator.SinhMaTuDong")
    @Column(name = "MaKH")
    private String maKH;
    @Column(name = "TenKH", columnDefinition = "nvarchar(50)")
    private String tenKH;
    @Column(name = "DiaChi", columnDefinition = "nvarchar(200)")
    private String diaChi;
    @Column(name = "SDT")
    private String soDT;
    @OneToMany(mappedBy = "khachHang")
    private List<HoaDon> hoaDon;

    public KhachHang() {
        //TODO Auto-generated constructor stub
    }


    public KhachHang(String maKH) {
        super();
        this.maKH = maKH;
    }


    public KhachHang(String maKH, String tenKH, String diaChi, String soDT) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.diaChi = diaChi;
        this.soDT = soDT;
    }

    public KhachHang(KhachHang customer) {
        this.maKH = customer.getMaKH();
        this.tenKH = customer.getTenKH();
        this.diaChi = customer.getDiaChi();
        this.soDT = customer.getSoDT();
    }


    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
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
