package eugenedrm.home.educative.hexagonalarchitecture.account.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ActivityWindow {

    private final List<Activity> activities;

    public ActivityWindow(@NonNull List<Activity> activities) {
        this.activities = new ArrayList<>(activities);
    }

    public Money calculateBalance(AccountId accountId) {
        Money depositBalance = activities.stream()
                .filter(activity -> activity.targetAccountId().equals(accountId))
                .map(Activity::money)
                .reduce(Money.ZERO, Money::add);
        Money withdrawalBalance = activities.stream()
                .filter(activity -> activity.sourceAccountId().equals(accountId))
                .map(Activity::money)
                .reduce(Money.ZERO, Money::add);
        return Money.subtract(depositBalance, withdrawalBalance);
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }
}
