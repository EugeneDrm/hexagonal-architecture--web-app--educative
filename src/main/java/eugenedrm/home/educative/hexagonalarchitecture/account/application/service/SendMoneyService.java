package eugenedrm.home.educative.hexagonalarchitecture.account.application.service;

import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in.SendMoneyCommand;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in.SendMoneyUseCase;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.AccountLockPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.LoadAccountPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.UpdateAccountStatePort;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.common.UseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@UseCase
@Slf4j
public class SendMoneyService implements SendMoneyUseCase {

    private final AccountLockPort accountLockPort;
    private final LoadAccountPort loadAccountPort;
    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);
        AccountId sourceAccountId = command.getSourceAccountId();
        AccountId targetAccountId = command.getTargetAccountId();

        // Note: current locking mechanism can easily lead to deadlocks due to consequent locking of two accounts.
        // When same accounts are being locked in reverse order by a different thread at the same time --> deadlock.
        // Not suitable for PROD

        accountLockPort.lockAccount(sourceAccountId);
        Account sourceAccount = loadAccountPort.loadAccount(
                sourceAccountId,
                baselineDate);
        if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
            log.warn("Not enough money on account {} for withdrawal.", sourceAccountId);
            accountLockPort.releaseAccount(sourceAccountId);
            return false;
        }

        accountLockPort.lockAccount(targetAccountId);
        Account targetAccount = loadAccountPort.loadAccount(
                targetAccountId,
                baselineDate);
        if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
            log.warn("Unable to deposit money to account {}.", targetAccountId);
            accountLockPort.releaseAccount(sourceAccountId);
            accountLockPort.releaseAccount(targetAccountId);
            return false;
        }

        updateAccountStatePort.updateActivities(sourceAccount);
        updateAccountStatePort.updateActivities(targetAccount);

        accountLockPort.releaseAccount(sourceAccountId);
        accountLockPort.releaseAccount(targetAccountId);
        return true;
    }
}
