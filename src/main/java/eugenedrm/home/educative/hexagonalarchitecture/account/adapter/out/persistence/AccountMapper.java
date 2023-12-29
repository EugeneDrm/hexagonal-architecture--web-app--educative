package eugenedrm.home.educative.hexagonalarchitecture.account.adapter.out.persistence;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Activity;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.ActivityId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.ActivityWindow;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account mapToDomainEntity(
            AccountJpaEntity account,
            List<ActivityJpaEntity> activities,
            Long withdrawalBalance,
            Long depositBalance) {
        Money baselineBalance = Money.of(depositBalance - withdrawalBalance);
        return Account.withId(
                new AccountId(account.getId()),
                baselineBalance,
                mapToActivityWindow(activities)
        );
    }

    private ActivityWindow mapToActivityWindow(List<ActivityJpaEntity> activities) {
        List<Activity> mappedActivities = activities.stream()
                .map(activity -> new Activity(
                        new ActivityId(activity.getId()),
                        new AccountId(activity.getOwnerAccountId()),
                        new AccountId(activity.getSourceAccountId()),
                        new AccountId(activity.getTargetAccountId()),
                        activity.getTimestamp(),
                        Money.of(activity.getAmount())
                ))
                .toList();
        return new ActivityWindow(mappedActivities);
    }

    public ActivityJpaEntity mapToJpaEntity(Activity activity) {
        return new ActivityJpaEntity(
                activity.id() == null ? null : activity.id().value(),
                activity.timestamp(),
                activity.ownerAccountId().value(),
                activity.sourceAccountId().value(),
                activity.targetAccountId().value(),
                activity.money().amount().longValue()
        );
    }
}
