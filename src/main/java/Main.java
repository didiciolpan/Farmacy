import Domain.Drug;
import Domain.DrugValidator;
import Domain.Transaction;
import Domain.TransactionValidator;
import Repository.IRepositiory;
import Repository.InMemoryRepository;
import Service.DrugService;
import Service.TransactionService;
import Service.UndoRedoService;
import UI.Console;

import java.security.KeyException;

public class Main {

    public static void main(String[] args) throws KeyException {

        IRepositiory<Drug> drugIRepository = new InMemoryRepository<>();
        DrugValidator drugValidator = new DrugValidator();
        UndoRedoService undoRedoService = new UndoRedoService();
        DrugService drugService = new DrugService(drugIRepository, drugValidator, undoRedoService);


        IRepositiory<Transaction> transactionIRepository = new InMemoryRepository<>();
        TransactionValidator transactionValidator = new TransactionValidator();
        TransactionService transactionService = new TransactionService(transactionIRepository, drugIRepository, transactionValidator, undoRedoService);

        drugService.add("paracetamol", "terapia", 10, 5, false);
        drugService.add("nurofen", "reckitt", 20.5, 10, false);
        drugService.add("vitamina c", "bayer", 13.5, 10, false);
        drugService.add("zinat", "terapia", 9.5, 10, true);

        transactionService.add(1, 13434, 5, "12.01.2020", "10:10");
        transactionService.add(2, 2445, 1, "20.02.2020", "14:27");
        transactionService.add(2, 2445, 1, "10.03.2020", "18:23");
        transactionService.add(3, 963426, 2, "20.05.2020", "17:13");


//        drugService.add(
//                "paracetamol",
//                "blabla",
//                15,
//                548,
//                false
//
//        );
//
//        drugService.add(
//                "algocalmin",
//                "ksbczjb",
//                19,
//                555,
//                false
//
//        );
//
//        drugService.add(
//                "oxacilina",
//                "ksbvsczjb",
//                25,
//                555,
//                true
//        );
//
//        transactionService.add(
//                0,
//                154841,
//                25,
//                "12.02.2020",
//                "12:25");
//
//        transactionService.add(
//                1,
//                15156,
//                15,
//                "12.05.2020",
//                "12:20"
//        );
//
//        transactionService.add(
//                1,
//                48631,
//                54,
//                "12.08.2019",
//                "14:20"
//        );
//
//        transactionService.add(
//                1,
//                165168,
//                2,
//                "15.06.2019",
//                "8:20"
//        );
//
//        transactionService.add(
//                0,
//                154,
//                16,
//                "26.04.2019",
//                "11:20"
//        );
//
//        transactionService.add(
//                1,
//                154,
//                23,
//                "26.04.2019",
//                "11:25"
//        );
//
//        transactionService.add(
//                2,
//                1546,
//                45,
//                "2.12.2019",
//                "8:15"
//        );

        Console console = new Console(drugService, transactionService, undoRedoService);
        console.run();

    }

}
