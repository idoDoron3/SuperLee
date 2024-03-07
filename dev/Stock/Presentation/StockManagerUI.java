package Stock.Presentation;

import Supplier_Module.Presentation.UI;

import java.time.LocalDate;
import java.util.Scanner;

public class StockManagerUI {
    private UI orderUi ;
    private StockMainUI stockMainUI;
    private LocalDate date;

    public StockManagerUI(LocalDate date) {
        orderUi = UI.getUser();
        stockMainUI = new StockMainUI();
        this.date = date;
    }

    public void startMenu(){

        Scanner scanner = new Scanner(System.in);
        String choice;

        do {
            // Display the menu options
            System.out.println("-------- Welcome to the Stock Manager menu of market number 1 --------");
            System.out.println("Menu:");
            System.out.println("1) Run Product Menu");
            System.out.println("2) Run Orders Menu");
            System.out.println("0) Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Call CLI function here
                    stockMainUI.startMenu(date);
//                    System.out.println("Running program through GUI...");
                    break;
                case "2":
                    // Call CLI function here
                    orderUi.beginOrderMenu(date);
//                    System.out.println("Running program through CLI...");
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (!choice.equals("0"));
    }
}
