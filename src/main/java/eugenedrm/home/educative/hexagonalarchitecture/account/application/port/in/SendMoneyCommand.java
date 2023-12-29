package eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import eugenedrm.home.educative.hexagonalarchitecture.common.SelfValidating;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

    @NotNull
    private final AccountId sourceAccountId;
    @NotNull
    private final AccountId targetAccountId;
    @NotNull
    private final Money money;

    public SendMoneyCommand(AccountId sourceAccountId, AccountId targetAccountId, Money money) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.money = money;
        validateSelf();
    }

    @AssertTrue(message = "Money amount must be greater than zero")
    boolean isMoneyGreaterThanZero() {
        return money == null || money.isPositive();
    }
}
