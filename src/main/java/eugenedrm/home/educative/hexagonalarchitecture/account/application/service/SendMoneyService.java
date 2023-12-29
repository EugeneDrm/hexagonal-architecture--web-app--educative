package eugenedrm.home.educative.hexagonalarchitecture.account.application.service;

import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in.SendMoneyCommand;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in.SendMoneyUseCase;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.AccountLockPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.LoadAccountPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.UpdateAccountStatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

    private final LoadAccountPort loadAccountPort;
    private final AccountLockPort accountLockPort;
    private final UpdateAccountStatePort updateAccountStatePort;

    @Override
    public boolean sendMoney(SendMoneyCommand command) {
        return false;
    }
}
