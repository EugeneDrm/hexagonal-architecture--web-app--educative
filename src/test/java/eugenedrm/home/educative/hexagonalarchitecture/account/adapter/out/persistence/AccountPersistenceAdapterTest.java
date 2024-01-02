package eugenedrm.home.educative.hexagonalarchitecture.account.adapter.out.persistence;

import static eugenedrm.home.educative.hexagonalarchitecture.account.common.AccountTestData.defaultAccount;
import static eugenedrm.home.educative.hexagonalarchitecture.account.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import java.time.LocalDateTime;
import java.time.Month;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountMapper.class})
class AccountPersistenceAdapterTest {

    @Autowired
    private AccountPersistenceAdapter adapter;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    void loadsAccount() {
        // when
        Account account = adapter.loadAccount(
                new AccountId(1L),
                LocalDateTime.of(2018, Month.AUGUST, 10, 0, 0));

        // then
        assertThat(account.getActivityWindow().getActivities()).hasSize(2);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(500));
    }

    @Test
    void updatesActivities() {
        // given
        Account account = defaultAccount()
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(
                        defaultActivity()
                                .withId(null)
                                .withMoney(Money.of(1L)).build())
                .build();

        // when
        adapter.updateActivities(account);

        // then
        assertThat(activityRepository.count()).isEqualTo(1);

        ActivityJpaEntity savedActivity = activityRepository.findAll().get(0);
        assertThat(savedActivity.getAmount()).isEqualTo(1L);
    }
}
