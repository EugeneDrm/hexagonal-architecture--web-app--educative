package eugenedrm.home.educative.hexagonalarchitecture.account.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ActivityJpaEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private LocalDateTime timestamp;

    @Column
    private Long ownerAccountId;

    @Column
    private Long sourceAccountId;

    @Column
    private Long targetAccountId;

    @Column
    private Long amount;
}
