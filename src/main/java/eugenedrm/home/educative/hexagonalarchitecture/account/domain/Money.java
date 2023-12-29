package eugenedrm.home.educative.hexagonalarchitecture.account.domain;

import java.math.BigInteger;
import lombok.NonNull;

public record Money(@NonNull BigInteger amount) {

    public static final Money ZERO = Money.of(0L);
    
    public static Money of(long amount) {
        return new Money(BigInteger.valueOf(amount));
    }

    public static Money add(Money a, Money b) {
        return new Money(a.amount.add(b.amount));
    }

    public static Money subtract(Money a, Money b) {
        return new Money(a.amount.subtract(b.amount));
    }

    public boolean isPositive() {
        return this.amount.compareTo(BigInteger.ZERO) > 0;
    }
}
