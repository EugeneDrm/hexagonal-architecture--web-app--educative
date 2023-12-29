package eugenedrm.home.educative.hexagonalarchitecture.account.adapter.out.persistence;

import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.LoadAccountPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.UpdateAccountStatePort;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

    private final AccountRepository accountRepository;
    private final ActivityRepository activityRepository;
    private final AccountMapper accountMapper;

    @Override
    public Account loadAccount(AccountId accountId, LocalDateTime baselineDate) {
        AccountJpaEntity account = accountRepository.findById(accountId.value())
                .orElseThrow(EntityNotFoundException::new);

        List<ActivityJpaEntity> activities =
                activityRepository.findByOwnerSince(accountId.value(), baselineDate);

        Long withdrawalBalance = activityRepository.getWithdrawalBalanceUntil(accountId.value(), baselineDate)
                .orElse(0L);
        Long depositBalance = activityRepository.getDepositBalanceUntil(accountId.value(), baselineDate)
                .orElse(0L);

        return accountMapper.mapToDomainEntity(
                account,
                activities,
                withdrawalBalance,
                depositBalance);
    }

    @Override
    public void updateActivities(Account account) {
        List<ActivityJpaEntity> entities = account.getActivityWindow().getActivities()
                .stream()
                .filter(activity -> activity.id() == null)
                .map(accountMapper::mapToJpaEntity)
                .toList();
        activityRepository.saveAll(entities);
    }
}
