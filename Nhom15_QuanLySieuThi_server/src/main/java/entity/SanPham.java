/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;



/**
 *
 * @author Knight Black
 */

//Entity SanPham
@Entity
@Table(name = "SanPham")
public class SanPham implements Serializable{
	/**
	 *
	 */
	public static final long serialVersionUID = 1L;
	@Id
	@Column(name = "MaSP")
    private String maSP;
	@ManyToOne
    private LoaiSP loaiSP;
	@ManyToOne
    private NCC nhaCungCap;
	@Column(name = "TenSP", columnDefinition = "nvarchar(200)")
    private String tenSP;
	@Column(name = "SoLuong")
    private int soLuong;
	@Column(name = "DonVi")
    private String donVi;
	@Column(name = "Gia")
    private double gia;
	@Column(name = "Anh")
    private String anh;
    
    @OneToMany(mappedBy = "sanPham")
    private List<CT_HoaDon> ctHoaDons;
  
    
	public SanPham() {
	}


	public SanPham(String maSP, LoaiSP loaiSP, NCC nhaCungCap, String tenSP, int soLuong, String donVi, double gia,
			String anh, List<CT_HoaDon> ctHoaDons) {
		super();
		this.maSP = maSP;
		this.loaiSP = loaiSP;
		this.nhaCungCap = nhaCungCap;
		this.tenSP = tenSP;
		this.soLuong = soLuong;
		this.donVi = donVi;
		this.gia = gia;
		this.anh = anh;
		this.ctHoaDons = ctHoaDons;
	}
	
	


	public SanPham(String maSP, LoaiSP loaiSP, NCC nhaCungCap, String tenSP, int soLuong, String donVi, double gia,
			String anh) {
		super();
		this.maSP = maSP;
		this.loaiSP = loaiSP;
		this.nhaCungCap = nhaCungCap;
		this.tenSP = tenSP;
		this.soLuong = soLuong;
		this.donVi = donVi;
		this.gia = gia;
		this.anh = anh;
	}
	
	



	public SanPham(String maSP) {
		super();
		this.maSP = maSP;
	}


	public String getMaSP() {
		return maSP;
	}


	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}


	public LoaiSP getLoaiSP() {
		return loaiSP;
	}


	public void setLoaiSP(LoaiSP loaiSP) {
		this.loaiSP = loaiSP;
	}


	public NCC getNhaCungCap() {
		return nhaCungCap;
	}


	public void setNhaCungCap(NCC nhaCungCap) {
		this.nhaCungCap = nhaCungCap;
	}


	public String getTenSP() {
		return tenSP;
	}


	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}


	public int getSoLuong() {
		return soLuong;
	}


	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}


	public String getDonVi() {
		return donVi;
	}


	public void setDonVi(String donVi) {
		this.donVi = donVi;
	}


	public double getGia() {
		return gia;
	}


	public void setGia(double gia) {
		this.gia = gia;
	}


	public String getAnh() {
		return anh;
	}


	public void setAnh(String anh) {
		this.anh = anh;
	}


	public List<CT_HoaDon> getCtHoaDons() {
		return ctHoaDons;
	}


	public void setCtHoaDons(List<CT_HoaDon> ctHoaDons) {
		this.ctHoaDons = ctHoaDons;
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	@Override
	public int hashCode() {
		return Objects.hash(maSP);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SanPham other = (SanPham) obj;
		return Objects.equals(maSP, other.maSP);
	}


	@Override
	public String toString() {
		return "SanPham [maSP=" + maSP + ", loaiSP=" + loaiSP + ", nhaCungCap=" + nhaCungCap + ", tenSP=" + tenSP
				+ ", soLuong=" + soLuong + ", donVi=" + donVi + ", gia=" + gia + ", anh=" + anh + ", ctHoaDons="
				+ ctHoaDons + "]";
	}
	
	


	


}
