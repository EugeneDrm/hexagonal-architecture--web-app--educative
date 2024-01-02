package eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import eugenedrm.home.educative.hexagonalarchitecture.common.SelfValidating;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {

    @NotNull
    AccountId sourceAccountId;
    @NotNull
    AccountId targetAccountId;
    @NotNull
    Money money;

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
