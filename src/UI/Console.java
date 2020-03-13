package UI;

import Domain.Drug;
import Domain.Transaction;
import Service.DrugService;
import Service.TransactionService;

import java.util.Scanner;

public class Console {

    private DrugService drugService;
    private TransactionService transactionService;
    private Scanner scanner;

    public Console(DrugService drugService, TransactionService transactionService) {
        this.drugService = drugService;
        this.transactionService = transactionService;

        this.scanner = new Scanner(System.in);
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
            System.out.println("1. Add or update a drug");
            System.out.println("2. Remove a drug");
            System.out.println("3. View all drugs");
            System.out.println("4. Back");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    handleAddUpdateDrug();
                    break;
                case "2":
                    handleRemoveDrug();
                    break;
                case "3":
                    handleViewDrugs();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void runTransactionCrud() {
        while (true) {
            System.out.println("1. Add or update a transaction");
            System.out.println("2. Remove a transaction");
            System.out.println("3. View all transactions");
            System.out.println("4. Back");

            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    handleAddUpdateTransaction();
                    break;
                case "2":
                    handleRemoveTransaction();
                    break;
                case "3":
                    handleViewTransactions();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option!");
                    break;
            }
        }
    }

    private void handleViewDrugs() {
        for (Drug drug : drugService.getAll()) {
            System.out.println(drug);
        }
    }

    private void handleRemoveDrug() {
        try {
            System.out.print("Enter the drugId to remove:");
            String drugId = scanner.nextLine();
            drugService.remove(drugId);

            System.out.println("Drug removed!");
        } catch (Exception ex) {
            System.out.println("Errors:\n" + ex.getMessage());
        }
    }

    private void handleAddUpdateDrug() {

        try {
            System.out.print("Enter durgId: ");
            String drugId = scanner.nextLine();
            System.out.print("Enter name (empty to not change for update): ");
            String drugName = scanner.nextLine();
            System.out.print("Enter the producer (empty to not change for update): ");
            String producer = scanner.nextLine();
            System.out.print("Enter price (0 to not change for update): ");
            double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter number of pieces (0 to not change for update): ");
            int noOfPieces = scanner.nextInt();
            System.out.print("Enter needs prescription (true / false): ");
            boolean needsPrescription = Boolean.parseBoolean(scanner.nextLine());

            drugService.addOrUpdate(drugId, drugName, producer, price, noOfPieces, needsPrescription);

            System.out.println("Drug added!");
        } catch (Exception ex) {
            System.out.println("Errors:\n" + ex.getMessage());
        }
    }

    private void handleViewTransactions() {
        for (Transaction transaction : transactionService.getAll()) {
            System.out.println(transaction);
        }
    }

    private void handleRemoveTransaction() {
        try {
            System.out.print("Enter the transaction id to remove:");
            String transactionId = scanner.nextLine();
            transactionService.remove(transactionId);

            System.out.println("Transaction removed!");
        } catch (Exception ex) {
            System.out.println("Errors:\n" + ex.getMessage());
        }
    }

    private void handleAddUpdateTransaction() {
        try {
            System.out.print("Enter transaction ID: ");
            String transactionId = scanner.nextLine();
            System.out.print("Enter drug id (empty to not change for update): ");
            String transactionDrugId = scanner.nextLine();
            System.out.print("Enter client card (empty to not change for update): ");
            int card = scanner.nextInt();
            System.out.print("Enter number of items (0 to not change for update): ");
            int noOfPiecesPerTransaction = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter date (empty to not change for update): ");
            String date = scanner.nextLine();
            System.out.print("Enter time (empty to not change for update): ");
            String time = scanner.nextLine();

            Transaction transaction = transactionService.addOrUpdate(transactionId, transactionDrugId, card, noOfPiecesPerTransaction, date, time);
            System.out.println(String.format("Added transaction id=%s,with drug id=%f, card=%f%%",
                    transaction.getTransactionId(), transaction.getTransactionDrugId(), transaction.getCard()));
        } catch (Exception ex) {
            System.out.println("Errors:\n" + ex.getMessage());
        }
    }
}
