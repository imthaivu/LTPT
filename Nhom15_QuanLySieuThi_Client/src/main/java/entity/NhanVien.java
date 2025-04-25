/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 *
 * @author Knight Black
 */

//Entity NhanVien
@Entity
@Table(name = "NhanVien")
public class NhanVien implements Serializable{
	/**
	 *
	 */
	public static final long serialVersionUID = 1L;
	/**
	 *
	 */
	@Id
	@Column(name = "MaNv")
    private String maNV;
	@ManyToOne
    private ChucVu chucVu;
	@Column(name = "HoTen", columnDefinition = "nvarchar(200)")
    private String hoTen;
	@Column(name = "NgaySinh")
    private Date ngaySinh;
	@Column(name = "SoDT")
    private String soDT;
	@Column(name = "GioiTinh")
    private boolean gioiTinh;
	@Column(name = "CaLamViec")
    private String caLamViec;
	@Column(name = "Luong")
    private double luong;
	@Column(name = "TrangThai")
    private String trangThai;
    
    @OneToMany(mappedBy = "nhanVien")
    private List<TaiKhoan> taiKhoan;
    @OneToMany(mappedBy = "nhanVien")
    private List<HoaDon> hoaDon;
    
    
    
	public NhanVien() {
		super();
	}

	

	public NhanVien(String maNV, ChucVu chucVu, String hoTen, Date ngaySinh, String soDT, boolean gioiTinh,
			String caLamViec, double luong, String trangThai, List<TaiKhoan> taiKhoan, List<HoaDon> hoaDon) {
		super();
		this.maNV = maNV;
		this.chucVu = chucVu;
		this.hoTen = hoTen;
		this.ngaySinh = ngaySinh;
		this.soDT = soDT;
		this.gioiTinh = gioiTinh;
		this.caLamViec = caLamViec;
		this.luong = luong;
		this.trangThai = trangThai;
		this.taiKhoan = taiKhoan;
		this.hoaDon = hoaDon;
	}

	


	public NhanVien(String maNV, ChucVu chucVu, String hoTen, Date ngaySinh, String soDT, boolean gioiTinh,
			String caLamViec, double luong, String trangThai) {
		super();
		this.maNV = maNV;
		this.chucVu = chucVu;
		this.hoTen = hoTen;
		this.ngaySinh = ngaySinh;
		this.soDT = soDT;
		this.gioiTinh = gioiTinh;
		this.caLamViec = caLamViec;
		this.luong = luong;
		this.trangThai = trangThai;
	}



	public NhanVien(String maNV, ChucVu chucVu, String hoTen, Date ngaySinh, String soDT, boolean gioiTinh,
			String caLamViec, double luong, String trangThai, List<HoaDon> hoaDon) {
		super();
		this.maNV = maNV;
		this.chucVu = chucVu;
		this.hoTen = hoTen;
		this.ngaySinh = ngaySinh;
		this.soDT = soDT;
		this.gioiTinh = gioiTinh;
		this.caLamViec = caLamViec;
		this.luong = luong;
		this.trangThai = trangThai;
	}



	public NhanVien(String maNV, String hoTen, Date ngaySinh, String soDT, boolean gioiTinh, String caLamViec,
			double luong, String trangThai) {
		super();
		this.maNV = maNV;
		this.hoTen = hoTen;
		this.ngaySinh = ngaySinh;
		this.soDT = soDT;
		this.gioiTinh = gioiTinh;
		this.caLamViec = caLamViec;
		this.luong = luong;
		this.trangThai = trangThai;
	}



	public String getMaNV() {
		return maNV;
	}



	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}



	public ChucVu getChucVu() {
		return chucVu;
	}



	public void setChucVu(ChucVu chucVu) {
		this.chucVu = chucVu;
	}



	public String getHoTen() {
		return hoTen;
	}



	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}



	public Date getNgaySinh() {
		return ngaySinh;
	}



	public void setNgaySinh(Date ngaySinh) {
		this.ngaySinh = ngaySinh;
	}



	public String getSoDT() {
		return soDT;
	}



	public void setSoDT(String soDT) {
		this.soDT = soDT;
	}



	public boolean isGioiTinh() {
		return gioiTinh;
	}



	public void setGioiTinh(boolean gioiTinh) {
		this.gioiTinh = gioiTinh;
	}



	public String getCaLamViec() {
		return caLamViec;
	}



	public void setCaLamViec(String caLamViec) {
		this.caLamViec = caLamViec;
	}



	public double getLuong() {
		return luong;
	}



	public void setLuong(double luong) {
		this.luong = luong;
	}



	public String getTrangThai() {
		return trangThai;
	}



	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}



	public List<TaiKhoan> getTaiKhoan() {
		return taiKhoan;
	}



	public void setTaiKhoan(List<TaiKhoan> taiKhoan) {
		this.taiKhoan = taiKhoan;
	}



	public List<HoaDon> getHoaDon() {
		return hoaDon;
	}



	public void setHoaDon(List<HoaDon> hoaDon) {
		this.hoaDon = hoaDon;
	}
    
    

    
}
