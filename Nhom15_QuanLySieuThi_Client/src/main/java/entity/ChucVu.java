/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

//Entity chức vụ
@Entity
@Table(name = "ChucVu")
public class ChucVu implements Serializable{
	/**
	 *
	 */
	public static final long serialVersionUID = 1L;
	@Id
	@Column(name = "MaCV")
    private String maCV;
	@Column(name = "TenCV", columnDefinition = "nvarchar(50)")
    private String tenCV;
	@Column(name = "Luong")
    private double luong;
	@OneToMany(mappedBy = "chucVu")
	private List<NhanVien> nhanVien;
	
    
    public ChucVu() {
		super();
	}

	/**
    * @contructor
    * @param maCV, tenCV, luong
    */
    public ChucVu(String maCV, String tenCV, double luong) {
        this.maCV = maCV;
        this.tenCV = tenCV;
        this.luong = luong;
    }
    
    public ChucVu(String maCV) {
		//TODO Auto-generated constructor stub
    	 this.maCV = maCV;
	}

	/**
     * return maCV
    */
    public String getMaCV() {
        return maCV;
    }

    /**
    * @param maCV
    */
    public void setMaCV(String maCV) {
        this.maCV = maCV;
    }

    /**
     * return tenCV
    */
    public String getTenCV() {
        return tenCV;
    }

    /**
    * @param tenCV
    */
    public void setTenCV(String tenCV) {
        this.tenCV = tenCV;
    }

    /**
     * return String luong
    */
    public double getLuong() {
        return luong;
    }

    /**
    * @param luong
    */
    public void setLuong(double luong) {
        this.luong = luong;
    }
    
}
