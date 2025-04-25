/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Knight Black
 */

//Entity Khách hàng
@Entity
@Table(name = "KhachHang")
public class KhachHang implements Serializable{
	public static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "sinhMaTuDong")
	@GenericGenerator(name = "sinhMaTuDong",
						parameters = @Parameter(name="prefix", value = "KH"),
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



	/**
     * @contructor KhachHang
     * @param maKH, tenKH, diaChi, soDT
     */
    public KhachHang(String maKH, String tenKH, String diaChi, String soDT) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.diaChi = diaChi;
        this.soDT = soDT;
    }
    
    /**
     * @contructor KhachHang
     * @param KhachHang customer
     */
    public KhachHang(KhachHang customer) {
        this.maKH = customer.getMaKH();
        this.tenKH = customer.getTenKH();
        this.diaChi = customer.getDiaChi();
        this.soDT = customer.getSoDT();
    }
    
    
    /**
     * return maKH
    */
    public String getMaKH() {
        return maKH;
    }

    /**
     * 
     * @param String maKH
     */
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    /**
     * return tenKH
    */
    public String getTenKH() {
        return tenKH;
    }

    /**
     * 
     * @param String tenKH
     */
    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    /**
     * return diaChi
    */
    public String getDiaChi() {
        return diaChi;
    }

    /**
     * 
     * @param String diaChi
     */
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    /**
     * return soDT
    */
    public String getSoDT() {
        return soDT;
    }

    /**
     * 
     * @param String soDT
     */
    public void setSoDT(String soDT) {
        this.soDT = soDT;
    }
    
    
}
