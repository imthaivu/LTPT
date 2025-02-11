package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@IdClass(CTHoaDonId.class)
@Table(name = "CT_HoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CTHoaDon {

    @Id
    @Column(name = "maSP", length = 255)
    private String maSP;

    @Id
    @Column(name = "maHD", length = 255)
    private String maHD;

    @Id
    @Column(name = "SoLuong")
    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "maSP", insertable = false, updatable = false)
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "maHD", insertable = false, updatable = false)
    private HoaDon hoaDon;
}