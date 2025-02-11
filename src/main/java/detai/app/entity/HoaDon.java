package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "HoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HoaDon {
    @Id
    @Column(name = "MaHD", length = 255)
    private String maHD;

    @Column(name = "NgayBan")
    private LocalDateTime ngayBan;

    @ManyToOne
    @JoinColumn(name = "khachHang_MaKH")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "nhanVien_MaNv")
    private NhanVien nhanVien;
}