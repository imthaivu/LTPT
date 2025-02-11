package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChucVu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChucVu {
    @Id
    @Column(name = "MaCV", length = 255)
    private String maCV;

    @Column(name = "Luong")
    private Double luong;

    @Column(name = "TenCV", length = 50)
    private String tenCV;
}