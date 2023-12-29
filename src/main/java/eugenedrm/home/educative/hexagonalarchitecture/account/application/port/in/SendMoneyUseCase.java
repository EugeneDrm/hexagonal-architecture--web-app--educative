package eugenedrm.home.educative.hexagonalarchitecture.account.application.port.in;

public interface SendMoneyUseCase {
    boolean sendMoney(SendMoneyCommand command);
}
