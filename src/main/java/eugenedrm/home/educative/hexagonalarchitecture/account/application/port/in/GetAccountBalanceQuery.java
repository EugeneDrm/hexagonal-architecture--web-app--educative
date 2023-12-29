package eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;

public interface GetAccountBalanceQuery {
    Money getAccountBalance(AccountId accountId);
}
