package entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.*;

@Entity
@IdClass(CT_HoaDon.class)
public class CT_HoaDon implements Serializable{
	@Id
	@ManyToOne
	@JoinColumn(name = "maHD")
	private HoaDon hoaDon;
	@Id
	@ManyToOne
	@JoinColumn(name = "maSP")
	private SanPham sanPham;
	@Column(name = "SoLuong")
	private int soLuong;
	
	public CT_HoaDon() {
	}

	public CT_HoaDon(HoaDon hoaDon, SanPham sanPham) {
		super();
		this.hoaDon = hoaDon;
		this.sanPham = sanPham;
	}
	
	

	public CT_HoaDon(HoaDon hoaDon, SanPham sanPham, int soLuong) {
		super();
		this.hoaDon = hoaDon;
		this.sanPham = sanPham;
		this.soLuong = soLuong;
	}

	public HoaDon getHoaDon() {
		return hoaDon;
	}

	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}

	public SanPham getSanPham() {
		return sanPham;
	}

	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	
	public double getThanhTien() {
		double thanhTien = sanPham.getGia() * soLuong;
		return thanhTien;
	}

	@Override
	public String toString() {
		return "CT_HoaDon [hoaDon=" + hoaDon + ", sanPham=" + sanPham + ", soLuong=" + soLuong + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(hoaDon, sanPham);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CT_HoaDon other = (CT_HoaDon) obj;
		return Objects.equals(hoaDon, other.hoaDon) && Objects.equals(sanPham, other.sanPham);
	}

	

	
	
}
