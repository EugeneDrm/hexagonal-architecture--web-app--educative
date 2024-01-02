package eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;

public interface AccountLockPort {
    void lockAccount(AccountId sourceAccountId);
    void releaseAccount(AccountId sourceAccountId);
}
