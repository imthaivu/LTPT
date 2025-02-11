package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NhaCungCap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCap {
    @Id
    @Column(name = "MaNCC", length = 255)
    private String maNCC;

    @Column(name = "DiaChi", length = 200)
    private String diaChi;

    @Column(name = "SoDT", length = 255)
    private String soDT;

    @Column(name = "TenNCC", length = 100)
    private String tenNCC;
}