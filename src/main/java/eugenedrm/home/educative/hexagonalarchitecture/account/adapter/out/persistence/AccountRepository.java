package eugenedrm.home.educative.hexagonalarchitecture.account.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountJpaEntity, Long> {
}
