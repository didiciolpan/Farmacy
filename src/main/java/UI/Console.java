package UI;

import Domain.Drug;
import Domain.Transaction;
import Domain.ValidationException;
import Service.DrugService;
import Service.TransactionService;
import Service.UndoRedoService;

import java.security.KeyException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Console {

    private DrugService drugService;
    private TransactionService transactionService;
    private UndoRedoService undoRedoService;
    private Scanner scanner;

    public Console(DrugService drugService, TransactionService transactionService, UndoRedoService undoRedoService) {
        this.drugService = drugService;
        this.transactionService = transactionService;
        this.undoRedoService = undoRedoService;

        this.scanner = new Scanner(System.in);
    }

    private static String readInputString(String message) {
        System.out.println(message);
        try {
            Scanner scan = new Scanner(System.in);
            return scan.nextLine();
        } catch (Exception ex) {
            System.out.println("Invalid input! ");
            return readInputString(message);
        }
    }

    private static int readInputInt(String message) {
        System.out.println(message);
        try {
            Scanner scan = new Scanner(System.in);
            return scan.nextInt();
        } catch (Exception ex) {
            System.out.println("Invalid input! ");
            return readInputInt(message);
        }
    }

    private void showMenu() {
        System.out.println("1. Drug CRUD");
        System.out.println("2. Transaction CRUD");
        System.out.println("x. Exit");
    }

    public void run() {
        while (true) {
            showMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    runDrugCrud();
                    break;
                case "2":
                    runTransactionCrud();
                    break;
                case "x":
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void runDrugCrud() {
        while (true) {
            System.out.println();
            System.out.println("1. Add a drug");
            System.out.println("2. Update a drug");
            System.out.println("3. Remove a drug and its transactions");
            System.out.println("4. View all drugs");
            System.out.println("5. Search a drug after drug ID");
            System.out.println("6. Search for a drug");
            System.out.println("7. Increase price for specific drugs");
            System.out.println("8. Undo drug");
            System.out.println("9. Redo drug");
            System.out.println("10. Back");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    handleAddDrug();
                    break;
                case "2":
                    handleUpdateDrug();
                    break;
                case "3":
                    handleRemoveDrugAndTransaction();
                    break;
                case "4":
                    handleViewDrugs();
                    break;
                case "5":
                    handleSearchDrugById();
                    break;
                case "6":
                    handleSearchDrug();
                    break;
                case "7":
                    increasePriceForSpecificDrugs();
                    break;
                case "8":
                    handleUndo();
                    break;
                case "9":
                    handleRedo();
                    break;
                case "10":
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }


    private void runTransactionCrud() {
        while (true) {
            System.out.println();
            System.out.println("1. Add a transaction");
            System.out.println("2. Update a transaction");
            System.out.println("3. Remove a transaction");
            System.out.println("4. View all transactions");
            System.out.println("5. Search a transaction after transaction id ");
            System.out.println("6. View Drugs ordered by number of transactions ");
            System.out.println("7. View Transactions between 2 dates ");
            System.out.println("8. View Client Cards ordered number of transactions ");
            System.out.println("9. Remove transactions between 2 dates ");
            System.out.println("10. Back");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    handleAddTransaction();
                    break;
                case "2":
                    handleUpdateTransaction();
                    break;
                case "3":
                    handleRemoveTransaction();
                    break;
                case "4":
                    handleViewTransactions();
                    break;
                case "5":
                    handleSearchTransaction();
                    break;
                case "6":
                    displayDrugsOrderedDescByTransactionNumber();
                    break;
                case "7":
                    displayTransactionsBetweenDates();
                    break;
                case "8":
                    displayClientCardOrderedDescByTransactionNumber();
                    break;
                case "9":
                    deleteTransactionsBetweenDates();
                    break;
                case "10":
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void displayTransactionsBetweenDates() {

        String startDate = readInputString("Enter the start date: ");
        String endDate = readInputString("Enter the end date: ");

        List<Transaction> resultList = transactionService.getAllTransactionBetweenDates(startDate, endDate);
        for (Transaction result : resultList) {
            System.out.println(result);
        }
    }

    private void handleViewDrugs() {
        for (Drug drug : drugService.getAll()) {
            System.out.println(drug);
        }
    }

    private void handleSearchDrugById() {
        try {
            int drugIdToBeSearched = readInputInt("Enter the drug ID you are looking for: ");

            Drug searchedDrugId = drugService.searchById(drugIdToBeSearched);
            System.out.println("Search result = " + searchedDrugId.getDrugName() + ", drug price= " + searchedDrugId.getPrice() + " lei");

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("The drug you are looking for does not exist! " + ex.getMessage());
        }
    }

    private void handleSearchDrug() {

        String drugToBeSearched = readInputString("Enter search string: ");
        List<Drug> foundDrug = drugService.searchDrugKey(drugToBeSearched);

        if (foundDrug.isEmpty()) {
            System.out.println("Drug not found");
        } else {
            for (Drug found : foundDrug) {
                System.out.println("\n" + "Search result: " + "\n"
                        + "Drug name: " + found.getDrugName() + "\n"
                        + "Drug producer: " + found.getProducer() + "\n"
                        + "Drug price: " + found.getPrice() + " lei" + "\n"
                        + "No. of pieces available: " + found.getNoOfPieces() + " pieces" + "\n"
                        + "Drug needs prescription: " + found.isNeedsPrescription());
                System.out.println();
            }
        }
    }

    private void handleUndo() {
        try {
            if (this.undoRedoService.undo()) {
                System.out.println("Undo done.");
            } else {
                System.out.println("No more undo.");
            }
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }

    private void handleRedo() {
        try {
            if (this.undoRedoService.redo()) {
                System.out.println("Redo done.");
            } else {
                System.out.println("No more redo.");
            }
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }

    private void handleRemoveDrugAndTransaction() {
        try {
            int drugId = readInputInt("Enter the drugId to remove:");
          //    drugService.remove(drugId);
            transactionService.removeDrugAndTransaction(drugId);
            System.out.println("Drug removed!");
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Errors:\n" + ex.getMessage());
        }
    }

    private void handleAddDrug() {

        String drugName = readInputString("Drug name:");
        String prducer = readInputString("Drug producer:");

        double price;
        while (true) {
            System.out.println("Drug price?");
            price = scanner.nextDouble();
            try {
                drugService.validatePrice(price);
                break;
            } catch (RuntimeException ex) {
                System.out.println("Drug runtime exception: " + ex.getMessage());
            }
        }

        int noOfPieces = readInputInt("Number of pieces:");

        System.out.println("Drug needs prescription (true / false):");
        boolean needsPrescription = scanner.nextBoolean();
        scanner.nextLine();

        try {
            drugService.add(drugName, prducer, price, noOfPieces, needsPrescription);
        } catch (KeyException kex) {
            System.out.println("Exceptie ID: " + kex.getMessage());
        }

    }

    private void handleUpdateDrug() {

        int id = readInputInt("Enter the drug Id you want to update: ");

        Drug drugtoBeUpdated = drugService.searchById(id);

        String drugName = readInputString("New drug name(empty to not change): ");
        String prducer = readInputString("New drug producer(empty to not change):");

        double price;
        while (true) {
            System.out.println("New drug price (empty to not change):");
            price = scanner.nextDouble();
            try {
                drugService.validatePrice(price);
                break;
            } catch (RuntimeException ex) {
                System.out.println("Drug runtime exception: " + ex.getMessage());
            }
        }

        int noOfPieces = readInputInt("Number of pieces (empty to not change):");

        System.out.println("Drug needs prescription (true / false):");
        boolean needsPrescription = scanner.nextBoolean();
        scanner.nextLine();

        try {
            drugService.update(id, drugName, prducer, price, noOfPieces, needsPrescription);
        } catch (KeyException kex) {
            System.out.println("Exceptie ID: " + kex.getMessage());
        }
    }

    private void increasePriceForSpecificDrugs() {
        double percent = readInputInt("How much do you want to increase the price?(%)");
        System.out.println("What's the min price?  ");
        double value = scanner.nextDouble();
        drugService.increasePriceOfDrugsByPercent(percent, value);
    }

    private void handleUpdateTransaction() {

        int id = readInputInt("Enter the transaction ID you want to update: ");
        Transaction transactionToBeUpdated = transactionService.searchById(id);

        int transactionDrugId = readInputInt("New transaction drug ID(empty to not change): ");
        int card = readInputInt("New client card (empty to not change):");

        int noOfPiecesPerTransaction;
        while (true) {
            noOfPiecesPerTransaction = readInputInt("New quantity (empty to not change):");
            try {
                transactionService.validateQuantiry(noOfPiecesPerTransaction);
                break;
            } catch (RuntimeException ex) {
                System.out.println("Drug runtime exception: " + ex.getMessage());
            }
        }

        String date = readInputString("New date (DD.MM.YYYY) (empty to not change):");
        String time = readInputString("New time (HH.MM) (empty to not change):");


        try {
            transactionService.update(id, transactionDrugId, card, noOfPiecesPerTransaction, date, time);
        } catch (KeyException kex) {
            System.out.println("Exceptie ID: " + kex.getMessage());
        }
    }

    private void handleViewTransactions() {
        for (Transaction transaction : transactionService.getAll()) {
            System.out.println(transaction);
        }
    }

    public void displayDrugsOrderedDescByTransactionNumber() {
        Map<Drug, Integer> orderdDrugs = transactionService.getDrugsOrderedDescByTransactionNumber();
        for (Map.Entry<Drug, Integer> entry : orderdDrugs.entrySet()) {
            System.out.println("Drug: " + entry.getKey().getDrugName() + ", No. Transactions = " + entry.getValue());
        }
    }

    public void displayClientCardOrderedDescByTransactionNumber() {
        Map<Transaction, Integer> orderedCards = transactionService.getClientCardOrderedDescByTransactionNumber();
        for (Map.Entry<Transaction, Integer> entry : orderedCards.entrySet()) {
            System.out.println("Client card: " + entry.getKey().getCard() + ", No. Transactions = " + entry.getValue());
        }
    }

    public void deleteTransactionsBetweenDates() {
        try {
            String startDate = readInputString("Enter the start date: ");
            String endDate = readInputString("Enter the end date: ");

            List<Transaction> resultList = transactionService.getAllTransactionBetweenDates(startDate, endDate);
            for (Transaction result : resultList) {
                transactionService.removeTransaction(result.getId());
            }
        } catch (KeyException ex) {
            System.out.println("Errors:\n" + ex.getMessage());
        }
    }

    private void handleSearchTransaction() {

        String transactionSearch = readInputString("Enter search string: ");
        List<Transaction> foundTransaction = transactionService.searchTransactionKey(transactionSearch);
        if (foundTransaction.isEmpty()) {
            System.out.println("Transaction not found");
        } else {
            for (Transaction found : foundTransaction) {
                System.out.println("\n" + "Search result: " + "\n"
                        + "Transaction Drug ID: " + found.getTransactionDrugId() + "\n"
                        + "Client card no.: " + found.getCard() + "\n"
                        + "Transaction date: " + found.getDate() + "\n"
                        + "Transaction time: " + found.getTime() + "\n"
                        + "No. of pieces per transaction " + found.getNoOfPiecesPerTransaction());
            }
        }
    }

    private void handleRemoveTransaction() {
        try {
            int transactionId = readInputInt("Enter the transaction id to remove:");
            transactionService.removeTransaction(transactionId);
            //     Transaction transactionToRemove = transactionService.searchById(transactionId);
            //     transactionService.removeDrugAndTransaction(transactionId,transactionToRemove.getTransactionDrugId());

            System.out.println("Transaction removed!");
        } catch (Exception ex) {
            System.out.println("Errors:\n" + ex.getMessage());
        }
    }

    private void handleAddTransaction() {

        try {
            int transactionDrugId = readInputInt("Enter drug id: ");
            int card = readInputInt("Enter client card: ");

            int noOfPiecesPerTransaction;
            while (true) {
                noOfPiecesPerTransaction = readInputInt("Enter number of items: ");
                try {
                    transactionService.validateQuantiry(noOfPiecesPerTransaction);
                    break;
                } catch (ValidationException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            String date = readInputString("Enter date: ");
            String time = readInputString("Enter time: ");

            transactionService.add(transactionDrugId, card, noOfPiecesPerTransaction, date, time);
        } catch (Exception ex) {
            System.out.println("Errors:\n" + ex.getMessage());
        }
    }
}
