package money_problem.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static money_problem.domain.Currency.EUR;
import static money_problem.domain.Currency.USD;
import static money_problem.domain.DomainUtility.*;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

class NewBankTest {
    public static final Currency PIVOT_CURRENCY = EUR;
    private final NewBank bank = NewBank.withPivotCurrency(PIVOT_CURRENCY);

    @Test
    @DisplayName("10 EUR -> USD = 12 USD")
    void convertInDollarsWithEURAsPivotCurrency() {
        assertThat(bank.add(createExchangeRate(1.2, USD))
                .flatMap(newBank -> newBank.convert(euros(10), USD)))
                .containsOnRight(dollars(12));
    }
}
