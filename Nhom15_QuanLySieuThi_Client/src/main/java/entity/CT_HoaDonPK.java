package entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CT_HoaDonPK implements Serializable{
	public static final long serialVersionUID = 1L;
	private String maHD;
	private String maSP;
	
	public CT_HoaDonPK() {
	}

	public CT_HoaDonPK(String maHD, String maSP) {
		super();
		this.maHD = maHD;
		this.maSP = maSP;
	}

	public String getMaHD() {
		return maHD;
	}

	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maHD == null) ? 0 : maHD.hashCode());
		result = prime * result + ((maSP == null) ? 0 : maSP.hashCode());
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
		CT_HoaDonPK other = (CT_HoaDonPK) obj;
		if (maHD == null) {
			if (other.maHD != null)
				return false;
		} else if (!maHD.equals(other.maHD))
			return false;
		if (maSP == null) {
			if (other.maSP != null)
				return false;
		} else if (!maSP.equals(other.maSP))
			return false;
		return true;
	}
	
	
}
