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
import java.util.Date;
import java.util.List;

/**
 *
 * @author Knight Black
 */

//Entity hóa đơn
@Entity
@Table(name = "HoaDon")
public class HoaDon implements Serializable{
	public static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "sinhMaTuDong")
	@GenericGenerator(name = "sinhMaTuDong",
						parameters = @Parameter(name="prefix", value = "HD"),
						strategy = "generator.SinhMaTuDong")
	@Column(name = "MaHD")
    private String maHD;
	@ManyToOne
    private NhanVien nhanVien;
	@ManyToOne
    private KhachHang khachHang;
	@Column(name = "NgayBan")
    private Date ngayBan;
    @OneToMany(mappedBy = "hoaDon")
    private List<CT_HoaDon> ctHoaDons;

	public HoaDon() {
	}
	

	public HoaDon(String maHD) {
		super();
		this.maHD = maHD;
	}


	

	public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang, Date ngayBan) {
		super();
		this.maHD = maHD;
		this.nhanVien = nhanVien;
		this.khachHang = khachHang;
		this.ngayBan = ngayBan;
	}


	public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang, Date ngayBan, List<CT_HoaDon> ctHoaDons) {
		super();
		this.maHD = maHD;
		this.nhanVien = nhanVien;
		this.khachHang = khachHang;
		this.ngayBan = ngayBan;
		this.ctHoaDons = ctHoaDons;
	}




	public String getMaHD() {
		return maHD;
	}


	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}


	public NhanVien getNhanVien() {
		return nhanVien;
	}


	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}


	public KhachHang getKhachHang() {
		return khachHang;
	}


	public void setKhachHang(KhachHang khachHang) {
		this.khachHang = khachHang;
	}


	public Date getNgayBan() {
		return ngayBan;
	}


	public void setNgayBan(Date ngayBan) {
		this.ngayBan = ngayBan;
	}
	
	public double getThanhTien(List<CT_HoaDon> ctHD) {
		double tongTien = 0;
		for(CT_HoaDon ct : ctHD) {
			tongTien += ct.getThanhTien();
		}
		return tongTien;
		
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maHD == null) ? 0 : maHD.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HoaDon other = (HoaDon) obj;
		if (maHD == null) {
			if (other.maHD != null)
				return false;
		} else if (!maHD.equals(other.maHD))
			return false;
		return true;
	}


	
    
   
    
    
}
