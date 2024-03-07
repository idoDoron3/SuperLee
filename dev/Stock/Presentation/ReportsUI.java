package Stock.Presentation;

import Stock.DataAccess.ProductDAO;
import Stock.Service.ReportsService;

import java.util.Scanner;

public class ReportsUI {
    private static ReportsService reportsService = ReportsService.getInstance();
    Scanner input = new Scanner(System.in);



    public void startMenu(String numOfMarketToManagement) {
        boolean running = true;
        while (running) {
            System.out.println("-------- Welcome to the Reports menu of market number " + Integer.parseInt(numOfMarketToManagement) + " --------");
            System.out.println("1) Create stock report. ");
            System.out.println("2) Create stock report for category. ");
            System.out.println("3) Create order report. ");
            System.out.println("4) Create damaged products report. ");
            System.out.println("5) Create shortages report. ");
            System.out.println("6) Go back to Stock menu ");
            System.out.println("7) next day ");

            System.out.println("Select the number you would like to access ");
            System.out.println("-----------------------");
            String selection = input.nextLine();
            switch (selection) {
                case "1":
                    createStockReportCase1();
                    break;

                case "2":
                    createStockReportForCategoryCase2();
                    break;

                case "3":
                    createOrderReportCase3();
                    break;

                case "4":
                    createDamagedReportCase4();
                    break;

                case "5":
                    createShortageReportCase5();
                    break;
                case "6":
                    running = false;
                    break;
                case "7":
//                    ProductDAO.getInstance().updateForNextDay();
                    break;


                default:
                    System.out.println("Wrong input");
                    break;
            }
        }
    }
    private void createStockReportCase1(){
        if (!reportsService.createStockReport()) {
            System.out.println("-------- Error in creation of stock report --------");
        }
    }
    Boolean checkIfOnlyLetters(String str) {
        return str.matches("[a-zA-Z' ]+");
    }


    private void createStockReportForCategoryCase2(){
        String categoryStr,discount;
        System.out.println("Whats is the category for the stock report? ");
        categoryStr = input.nextLine();
        if (!checkIfOnlyLetters(categoryStr)) {
            System.out.println("your product's category is not a valid string ");
            return;
        }
        reportsService.createStockReportForCategory(categoryStr);
    }

    private void createOrderReportCase3(){
        if (!reportsService.createOrderReport()) {
            System.out.println("-------- Error in creation of order report --------");
        }
    }

    private void createDamagedReportCase4(){
        if (!reportsService.createDamagedReport()) {
            System.out.println("-------- Error in creation of damaged report --------");
        }
    }

    private void createShortageReportCase5(){
        if (reportsService.createShortageReport() == null) {
            System.out.println("-------- Error in creation of shortage report --------");
        }
    }




}
