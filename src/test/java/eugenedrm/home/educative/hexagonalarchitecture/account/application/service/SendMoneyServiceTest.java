package eugenedrm.home.educative.hexagonalarchitecture.account.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in.SendMoneyCommand;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.AccountLockPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.LoadAccountPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.UpdateAccountStatePort;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SendMoneyServiceTest {

    @Mock
    private LoadAccountPort loadAccountPort;

    @Mock
    private AccountLockPort accountLockPort;

    @Mock
    private UpdateAccountStatePort updateAccountStatePort;

    @InjectMocks
    private SendMoneyService sendMoneyService;

    @Test
    void transactionSucceeds() {
        // given
        Account sourceAccount = givenSourceAccount();
        Account targetAccount = givenTargetAccount();

        AccountId sourceAccountId = sourceAccount.getId();
        AccountId targetAccountId = targetAccount.getId();

        givenWithdrawalWillSucceed(sourceAccount);
        givenDepositWillSucceed(targetAccount);

        Money money = Money.of(500L);

        // when
        SendMoneyCommand command = new SendMoneyCommand(sourceAccountId, targetAccountId, money);
        boolean success = sendMoneyService.sendMoney(command);

        // then
        assertThat(success).isTrue();

        then(accountLockPort).should().lockAccount(sourceAccountId);
        then(sourceAccount).should().withdraw(money, targetAccountId);
        then(accountLockPort).should().releaseAccount(sourceAccountId);

        then(accountLockPort).should().lockAccount(targetAccountId);
        then(targetAccount).should().deposit(money, sourceAccountId);
        then(accountLockPort).should().releaseAccount(targetAccountId);

        thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
    }

    private void thenAccountsHaveBeenUpdated(AccountId... accountIds) {
        ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);
        then(updateAccountStatePort).should(times(accountIds.length))
                .updateActivities(accountCaptor.capture());

        List<AccountId> updatedAccountIds = accountCaptor.getAllValues()
                .stream()
                .map(Account::getId)
                .collect(Collectors.toList());
        assertThat(updatedAccountIds).containsAll(Arrays.asList(accountIds));
    }

    private Account givenTargetAccount() {
        return givenAnAccountWithId(new AccountId(42L));
    }

    private Account givenSourceAccount() {
        return givenAnAccountWithId(new AccountId(41L));
    }

    private Account givenAnAccountWithId(AccountId id) {
        Account account = Mockito.mock(Account.class);
        given(account.getId())
                .willReturn(id);
        given(loadAccountPort.loadAccount(eq(account.getId()), any(LocalDateTime.class)))
                .willReturn(account);
        return account;
    }

    private void givenDepositWillSucceed(Account account) {
        given(account.deposit(any(Money.class), any(AccountId.class)))
                .willReturn(true);
    }

    private void givenWithdrawalWillSucceed(Account account) {
        given(account.withdraw(any(Money.class), any(AccountId.class)))
                .willReturn(true);
    }
}