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

import net.bytebuddy.implementation.bind.annotation.Default;

//Entity chức vụ
@Entity
@Table(name = "ChucVu")
public class ChucVu implements Serializable{
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

	    public ChucVu(String maCV, String tenCV, double luong) {
        this.maCV = maCV;
        this.tenCV = tenCV;
        this.luong = luong;
    }
    
    public ChucVu(String maCV) {
		//TODO Auto-generated constructor stub
    	 this.maCV = maCV;
	}

	    public String getMaCV() {
        return maCV;
    }

        public void setMaCV(String maCV) {
        this.maCV = maCV;
    }

        public String getTenCV() {
        return tenCV;
    }

        public void setTenCV(String tenCV) {
        this.tenCV = tenCV;
    }

        public double getLuong() {
        return luong;
    }

        public void setLuong(double luong) {
        this.luong = luong;
    }
    
}
