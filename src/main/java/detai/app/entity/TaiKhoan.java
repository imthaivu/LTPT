package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoan {
    @Id
    @Column(name = "MaTK", length = 255)
    private String maTK;

    @Column(name = "MatKhau", length = 255)
    private String matKhau;

    @Column(name = "TaiKhoan", length = 255)
    private String taiKhoan;

    @Column(name = "VaiTro", length = 255)
    private String vaiTro;

    @ManyToOne
    @JoinColumn(name = "nhanVien_MaNv")
    private NhanVien nhanVien;
}