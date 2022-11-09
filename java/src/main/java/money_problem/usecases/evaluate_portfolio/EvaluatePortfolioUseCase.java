package money_problem.usecases.evaluate_portfolio;

import io.vavr.control.Either;
import money_problem.domain.Bank;
import money_problem.domain.Error;
import money_problem.domain.Money;
import money_problem.usecases.common.Success;
import money_problem.usecases.common.UseCase;
import money_problem.usecases.common.UseCaseError;
import money_problem.usecases.ports.BankRepository;
import money_problem.usecases.ports.PortfolioRepository;

public class EvaluatePortfolioUseCase implements UseCase<EvaluatePortfolio, EvaluationResult> {
    private final BankRepository bankRepository;
    private final PortfolioRepository portfolioRepository;

    public EvaluatePortfolioUseCase(BankRepository bankRepository, PortfolioRepository portfolioRepository) {
        this.bankRepository = bankRepository;
        this.portfolioRepository = portfolioRepository;
    }

    @Override
    public Either<UseCaseError, Success<EvaluationResult>> invoke(EvaluatePortfolio command) {
        return bankRepository.getBank()
                .toEither(new Error("No bank defined"))
                .flatMap(bank -> evaluatePortfolio(bank, command))
                .map(money -> Success.of(mapToResult(money)))
                .mapLeft(error -> new UseCaseError(error.message()));
    }

    private EvaluationResult mapToResult(Money money) {
        return new EvaluationResult(money.amount(), money.currency());
    }

    private Either<Error, Money> evaluatePortfolio(Bank bank, EvaluatePortfolio command) {
        return portfolioRepository.get()
                .evaluate(bank, command.currency());
    }
}
