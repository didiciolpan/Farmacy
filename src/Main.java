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

        drugService.addOrUpdate("12ABC", "paracetamol", "terapia" , 10,5,false);
        drugService.addOrUpdate("462DE", "nurofen", "reckitt" , 20.5,10,false);
        drugService.addOrUpdate("678UI", "vitamina c", "bayer" , 13.5,10,false);
        drugService.addOrUpdate("6708CI", "zinat", "terapia" , 9.5,10,true);

        transactionService.addOrUpdate("346453", "12ABC", 12345,2,"14-03-2020", "10:59");
        transactionService.addOrUpdate("2465", "462DE", 5678,1,"12-03-2020", "15:43");
        transactionService.addOrUpdate("24067", "678UI", 8907,2,"14-03-2020", "18:43");

        Console console = new Console(drugService, transactionService);
        console.run();
    }

}
