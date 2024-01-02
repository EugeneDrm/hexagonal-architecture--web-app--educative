package eugenedrm.home.educative.hexagonalarchitecture.account.domain;

import static eugenedrm.home.educative.hexagonalarchitecture.account.common.AccountTestData.defaultAccount;
import static eugenedrm.home.educative.hexagonalarchitecture.account.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void withdrawalSucceeds() {
        // given
        AccountId accountId = new AccountId(1L);
        Account account = defaultAccount()
                .withAccountId(accountId)
                .withBaselineBalance(Money.of(555L))
                .withActivityWindow(
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(999L))
                                .build(),
                        defaultActivity()
                                .withTargetAccount(accountId)
                                .withMoney(Money.of(1L))
                                .build())
                .build();

        // when
        boolean success = account.withdraw(Money.of(555L), new AccountId(99L));

        // then
        assertThat(success).isTrue();
        assertThat(account.getActivityWindow().getActivities()).hasSize(3);
        assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L));
    }

}