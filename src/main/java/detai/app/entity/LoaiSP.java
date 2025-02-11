package detai.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "LoaiSP")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoaiSP {
    @Id
    @Column(name = "MaLoaiSP", length = 255)
    private String maLoaiSP;

    @Column(name = "TenLoaiSP", length = 100)
    private String tenLoaiSP;
}