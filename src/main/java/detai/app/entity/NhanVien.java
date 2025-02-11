package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "NhanVien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {
    @Id
    @Column(name = "MaNv", length = 255)
    private String maNv;

    @Column(name = "CaLamViec", length = 255)
    private String caLamViec;

    @Column(name = "GioiTinh")
    private Boolean gioiTinh;

    @Column(name = "HoTen", length = 200)
    private String hoTen;

    @Column(name = "Luong")
    private Double luong;

    @Column(name = "NgaySinh")
    private LocalDateTime ngaySinh;

    @Column(name = "SoDT", length = 255)
    private String soDT;

    @Column(name = "TrangThai", length = 200)
    private String trangThai;

    @ManyToOne
    @JoinColumn(name = "chucVu_MaCV")
    private ChucVu chucVu;
}