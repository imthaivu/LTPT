package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPham {
    @Id
    @Column(name = "MaSP", length = 255)
    private String maSP;

    @Column(name = "Anh", length = 255)
    private String anh;

    @Column(name = "DonVi", length = 200)
    private String donVi;

    @Column(name = "Gia")
    private Double gia;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "TenSP", length = 200)
    private String tenSP;

    @ManyToOne
    @JoinColumn(name = "loaiSP_MaLoaiSP")
    private LoaiSP loaiSP;

    @ManyToOne
    @JoinColumn(name = "nhaCungCap_MaNCC")
    private NhaCungCap nhaCungCap;
}