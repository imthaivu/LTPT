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
import java.util.Objects;


/**
 *
 * @author Knight Black
 */

//Entity TaiKhoan
@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan implements Serializable{
	@Override
	public String toString() {
		return "TaiKhoan [maTK=" + maTK + ", taiKhoan=" + taiKhoan + ", matKhau=" + matKhau + ", vaiTro=" + vaiTro
				+ ", nhanVien=" + nhanVien + "]";
	}

	@Id
	@GeneratedValue(generator = "sinhMaTuDong")
	@GenericGenerator(name = "sinhMaTuDong",
						parameters = @Parameter(name="prefix", value = "TK"),
						strategy = "generator.SinhMaTuDong")
	@Column(name = "MaTK")
    private String maTK;
	@Column(name = "TaiKhoan")
    private String taiKhoan;
	@Column(name = "MatKhau")
    private String matKhau;
	@Column(name = "VaiTro")
    private String vaiTro;
    
    @ManyToOne
    private NhanVien nhanVien;

	public TaiKhoan(String maTK, String taiKhoan, String matKhau, String vaiTro) {
		super();
		this.maTK = maTK;
		this.taiKhoan = taiKhoan;
		this.matKhau = matKhau;
		this.vaiTro = vaiTro;
	}



	public TaiKhoan(String maTK, String taiKhoan, String matKhau, String vaiTro, NhanVien nhanVien) {
		super();
		this.maTK = maTK;
		this.taiKhoan = taiKhoan;
		this.matKhau = matKhau;
		this.vaiTro = vaiTro;
		this.nhanVien = nhanVien;
	}



	public TaiKhoan() {
	}

	public String getMaTK() {
		return maTK;
	}

	public void setMaTK(String maTK) {
		this.maTK = maTK;
	}

	public String getTaiKhoan() {
		return taiKhoan;
	}

	public void setTaiKhoan(String taiKhoan) {
		this.taiKhoan = taiKhoan;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public String getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(String vaiTro) {
		this.vaiTro = vaiTro;
	}

	public NhanVien getNhanVien() {
		return nhanVien;
	}

	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}



	@Override
	public int hashCode() {
		return Objects.hash(maTK);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaiKhoan other = (TaiKhoan) obj;
		return Objects.equals(maTK, other.maTK);
	}
    
    
    
}
