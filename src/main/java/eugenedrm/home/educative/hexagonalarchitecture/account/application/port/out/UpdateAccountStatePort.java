package eugenedrm.home.educative.hexagonalarchitecture.account.application.port.out;

import eugenedrm.home.educative.hexagonalarchitecture.account.domain.Account;

public interface UpdateAccountStatePort {
    void updateActivities(Account account);
}
