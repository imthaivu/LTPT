package detai.app.entity;

import java.io.Serializable;
import java.util.Objects;

public class CTHoaDonId implements Serializable {
    private String maSP;
    private String maHD;
    private int soLuong;

    public CTHoaDonId() {}

    public CTHoaDonId(String maSP, String maHD, int soLuong) {
        this.maSP = maSP;
        this.maHD = maHD;
        this.soLuong = soLuong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CTHoaDonId)) return false;
        CTHoaDonId that = (CTHoaDonId) o;
        return soLuong == that.soLuong &&
                Objects.equals(maSP, that.maSP) &&
                Objects.equals(maHD, that.maHD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(maSP, maHD, soLuong);
    }

    // Getter/Setter (nếu cần)
}