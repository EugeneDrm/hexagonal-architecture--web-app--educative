package eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NoOpAccountLockPort implements AccountLockPort {

    @Override
    public void lockAccount(AccountId sourceAccountId) {
        log.info("NOOP account locker: locking account {}", sourceAccountId.value());
    }

    @Override
    public void releaseAccount(AccountId sourceAccountId) {
        log.info("NOOP account locker: releasing account {}", sourceAccountId.value());
    }
}
