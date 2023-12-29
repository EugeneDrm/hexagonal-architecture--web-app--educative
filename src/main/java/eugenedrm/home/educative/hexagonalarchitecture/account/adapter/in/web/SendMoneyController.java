package eugenedrm.home.educative.hexagonalarchitecture.account.adapter.in.web;

import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in.SendMoneyCommand;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in.SendMoneyUseCase;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SendMoneyController {

    private final SendMoneyUseCase sendMoneyUseCase;

    @PostMapping(path = "/accounts/sendMoney/{sourceAccountId}/{targetAccountId}/{amount}")
    void sendMoney(
            @PathVariable("sourceAccountId") Long sourceAccount,
            @PathVariable("targetAccountId") Long targetAccount,
            @PathVariable("amount") Long amount) {
        SendMoneyCommand command = new SendMoneyCommand(
                new AccountId(sourceAccount),
                new AccountId(targetAccount),
                Money.of(amount));
        sendMoneyUseCase.sendMoney(command);
    }
}
