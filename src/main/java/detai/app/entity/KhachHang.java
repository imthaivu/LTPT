package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KhachHang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KhachHang {
    @Id
    @Column(name = "MaKH", length = 255)
    private String maKH;

    @Column(name = "DiaChi", length = 200)
    private String diaChi;

    @Column(name = "SDT", length = 255)
    private String sdt;

    @Column(name = "TenKH", length = 50)
    private String tenKH;
}