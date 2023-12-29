package eugenedrm.home.educative.hexagonalarchitecture.account.application.service;

import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in.GetAccountBalanceQuery;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.LoadAccountPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAccountBalanceService implements GetAccountBalanceQuery {
    
    private final LoadAccountPort loadAccountPort;

    @Override
    public Money getAccountBalance(AccountId accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
                .calculateBalance();
    }
}
