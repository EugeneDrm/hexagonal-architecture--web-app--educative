package eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import java.time.LocalDateTime;

public interface LoadAccountPort {
    Account loadAccount(AccountId accountId, LocalDateTime baselineDate);
}
