package eugenedrm.home.educative.hexagonalarchitecture.account.domain;

import java.time.LocalDateTime;
import lombok.NonNull;

public record Activity(
        ActivityId id,
        @NonNull AccountId ownerAccountId,
        @NonNull AccountId sourceAccountId,
        @NonNull AccountId targetAccountId,
        @NonNull LocalDateTime timestamp,
        @NonNull Money money) {

    public Activity(
            AccountId ownerAccountId,
            AccountId sourceAccountId,
            AccountId targetAccountId,
            LocalDateTime timestamp,
            Money money) {
        this(null, ownerAccountId, sourceAccountId, targetAccountId, timestamp, money);
    }
}
