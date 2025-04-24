/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 *
 * @author Knight Black
 */

//Entity loại sản phẩm
@Entity
@Table(name = "LoaiSP")
public class LoaiSP implements Serializable{
	public static final long serialVersionUID = 1L;
	@Id
	@Column(name = "MaLoaiSP")
    private String maLoaiSP;
	@Column(name = "TenLoaiSP", columnDefinition = "nvarchar(100)")
    private String tenLoaiSP;
	
	@OneToMany(mappedBy = "loaiSP")
	private List<SanPham> sanPham;

    /**
     * @contructor LoaiSP
     * @param String maLoaiSP, String tenLoaiSP
     */
    public LoaiSP(String maLoaiSP, String tenLoaiSP) {
        this.maLoaiSP = maLoaiSP;
        this.tenLoaiSP = tenLoaiSP;
    }
    
    public LoaiSP() {
		//TODO Auto-generated constructor stub
	}

    /**
     * return maLoaiSP;
     */
    public String getMaLoaiSP() {
        return maLoaiSP;
    }
    
    /**
     * @param String maLoaiSP
     */

    public void setMaLoaiSP(String maLoaiSP) {
        this.maLoaiSP = maLoaiSP;
    }

    /**
     * return tenLoaiSP;
     */
    public String getTenLoaiSP() {
        return tenLoaiSP;
    }

    /**
     * @param String maLoaiSP
     */
    public void setTenLoaiSP(String tenLoaiSP) {
        this.tenLoaiSP = tenLoaiSP;
    }
    
}
