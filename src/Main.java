import Domain.DrugValidator;
import Domain.TransactionValidator;
import Repository.DrugRepository;
import Repository.TransactionRepository;
import Service.DrugService;
import Service.TransactionService;
import UI.Console;

public class Main {

    public static void main(String[] args) {

        DrugValidator drugValidator = new DrugValidator();
        TransactionValidator transactionValidator = new TransactionValidator();

        DrugRepository drugRepository = new DrugRepository(drugValidator);
        TransactionRepository transactionRepository = new TransactionRepository(transactionValidator);

        DrugService drugService = new DrugService(drugRepository);
        TransactionService transactionService = new TransactionService(transactionRepository, drugRepository);

        Console console = new Console(drugService, transactionService);
        console.run();
    }

}
