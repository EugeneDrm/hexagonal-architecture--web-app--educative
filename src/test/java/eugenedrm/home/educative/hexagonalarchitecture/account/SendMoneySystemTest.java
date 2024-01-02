package eugenedrm.home.educative.hexagonalarchitecture.account;

import static org.assertj.core.api.BDDAssertions.then;

import eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out.LoadAccountPort;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.AccountId;
import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Money;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {

    private static final Money TRANSFERRED_AMOUNT = Money.of(500L);
    private static final AccountId SOURCE_ACCOUNT_ID = new AccountId(1L);
    private static final AccountId TARGET_ACCOUNT_ID = new AccountId(2L);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoadAccountPort loadAccountPort;

    @Test
    @Sql("SendMoneySystemTest.sql")
    void sendMoney() {
        // given
        Money initialSourceBalance = sourceAccount().calculateBalance();
        Money initialTargetBalance = targetAccount().calculateBalance();

        // when
        ResponseEntity<?> response = whenSendMoney(
                SOURCE_ACCOUNT_ID,
                TARGET_ACCOUNT_ID,
                TRANSFERRED_AMOUNT);

        // then
        then(response.getStatusCode())
                .isEqualTo(HttpStatus.OK);

        then(sourceAccount().calculateBalance())
                .isEqualTo(initialSourceBalance.minus(TRANSFERRED_AMOUNT));

        then(targetAccount().calculateBalance())
                .isEqualTo(initialTargetBalance.plus(TRANSFERRED_AMOUNT));
    }

    private Account sourceAccount() {
        return loadAccount(SOURCE_ACCOUNT_ID);
    }

    private Account targetAccount() {
        return loadAccount(TARGET_ACCOUNT_ID);
    }

    private Account loadAccount(AccountId accountId) {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now());
    }

    private ResponseEntity<?> whenSendMoney(AccountId sourceAccountId, AccountId targetAccountId, Money amount) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        return restTemplate.exchange(
                "/accounts/sendMoney/{sourceAccountId}/{targetAccountId}/{amount}",
                HttpMethod.POST,
                request,
                Object.class,
                sourceAccountId.value(),
                targetAccountId.value(),
                amount.amount());
    }
}
