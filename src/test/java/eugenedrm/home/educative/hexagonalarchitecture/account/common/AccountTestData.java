package eugenedrm.home.educative.hexagonalarchitecture.account.common;

import static eugenedrm.home.educative.hexagonalarchitecture.account.common.ActivityTestData.defaultActivity;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Activity;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.ActivityWindow;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import java.util.Arrays;

public class AccountTestData {

    public static AccountBuilder defaultAccount() {
        return new AccountBuilder()
                .withAccountId(new AccountId(42L))
                .withBaselineBalance(Money.of(999L))
                .withActivityWindow(
                        defaultActivity().build(),
                        defaultActivity().build()
                );
    }

    public static class AccountBuilder {

        private AccountId accountId;
        private Money baselineBalance;
        private ActivityWindow activityWindow;

        public AccountBuilder withAccountId(AccountId accountId) {
            this.accountId = accountId;
            return this;
        }

        public AccountBuilder withBaselineBalance(Money baselineBalance) {
            this.baselineBalance = baselineBalance;
            return this;
        }

        public AccountBuilder withActivityWindow(Activity... activities) {
            this.activityWindow = new ActivityWindow(Arrays.asList(activities));
            return this;
        }

        public Account build() {
            return Account.withId(this.accountId, this.baselineBalance, this.activityWindow);
        }

    }
}
