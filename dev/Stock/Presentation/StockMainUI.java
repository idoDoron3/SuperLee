package Stock.Presentation;

import Stock.Business.*;
import Stock.DataAccess.ProductDetailsDAO;
import Stock.Service.ProductService;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;


/**
 * UI menu:
 * 1) Add a new product. ---done---
 * 2) Update quantity of existing product. ---done---
 * 3) sell/remove from stock a single product. ---done---
 * 4) change store and storage sizes. ---done---
 * 5) inform on a defected/expired product. ---done---
 * 6) Reports. ---done---
 * 7) get information on selected product. ---done---
 * 8) update the discounts: on category and on product. ---done---
 * 9) Show all the products that sold ---done---
 * 10) Change min quantity to product ---done---
 * 11) Show all shortages in stock ---done---
 * 12) Exit.
 * <p>
 * //
 */


/**
 * This class represents the user interface for managing a chain of markets and their stock.
 * The user can add and update products, change store and storage sizes, report defects and
 * expirations, generate reports, and more.
 */
public class StockMainUI {

    private final MarketUI marketUI = new MarketUI();
    private final ProductUI productUi = new ProductUI();
    private final ReportsUI reportsUI = new ReportsUI();
    private static final ProductService productService = ProductService.getInstance();



    public void startMenu(LocalDate localDate) {
        /**
         * Displays the initial menu for selecting the market to manage and entering the number of shelves.
         * Prompts the user to create a default market and then displays the stock menu for managing products.
         */
        Chain chain;
        Market market;
        String numOfMarkets;
        String numOfShelves;
        String numOfMarketToManagement;
        Scanner input = new Scanner(System.in);
        boolean running;

        if (ProductDetailsDAO.getNumberOfMarketsInChain() != 0 && ProductDetailsDAO.getManagedMarket() != 0 && ProductDetailsDAO.getNumOfShelves() != 0) {
            chain = new Chain(ProductDetailsDAO.getNumberOfMarketsInChain());
            market = new Market(ProductDetailsDAO.getNumOfShelves());
            numOfMarketToManagement = Integer.toString(ProductDetailsDAO.getManagedMarket());
            ProductManager.setStore(market.getStore());
            ProductManager.setStorage(market.getStorage());
            ProductManager.setShortages(market.getShortages());
            MarketManager.setMarket(market);
            productService.sendToSupplierAllProductsQuantity();
        } else {
            running = true;
            boolean validSubSubCategory;
            numOfMarkets = "0";
            numOfShelves = "0";
            numOfMarketToManagement = "0";
            input = new Scanner(System.in);
            while (running) {
                System.out.println("Enter the amount of markets in the chain: ");
                numOfMarkets = input.nextLine();
                if (checkIfPositiveNumber(numOfMarkets)) {
                    running = false;
                } else {
                    System.out.print("Your number of markets is not valid, ");
                }
            }
            chain = new Chain(Integer.parseInt(numOfMarkets));
            ProductDetailsDAO.setNumberOfMarketsInChain(Integer.parseInt(numOfMarkets));
            running = true;
            System.out.println("Enter the the number of the market you want to management: ");
            numOfMarketToManagement = input.nextLine();
            if (checkIfPositiveNumber(numOfMarketToManagement) && Integer.parseInt(numOfMarketToManagement) <= Integer.parseInt(numOfMarkets)) {
                running = false;
            }
            while (running) {
                System.out.println("The number of market is not valid, please enter a valid number of market: ");
                numOfMarketToManagement = input.nextLine();
                if (checkIfPositiveNumber(numOfMarketToManagement) && Integer.parseInt(numOfMarketToManagement) <= Integer.parseInt(numOfMarkets)) {
                    running = false;
                }
            }
            ProductDetailsDAO.setManagedMarket(Integer.parseInt(numOfMarketToManagement));
            running = true;
            System.out.println("Enter the number of shelves you have in store and in storage");
            numOfShelves = input.nextLine();
            if (checkIfPositiveNumber(numOfShelves)) {
                running = false;
            }
            while (running) {
                System.out.println("The number of shelves is not valid, please enter a valid number: ");
                numOfShelves = input.nextLine();
                if (checkIfPositiveNumber(numOfShelves)) {
                    running = false;
                }
            }
            //market = chain.getMarketByIndex(Integer.parseInt(numOfMarketToManagement) - 1);
            market = new Market(Integer.parseInt(numOfShelves));
            ProductDetailsDAO.setNumOfShelves(Integer.parseInt(numOfShelves));
            ProductDetailsDAO.getInstance().saveDetails();


            ProductManager.getInstance();
            ProductManager.setShortages(market.getShortages());
            ProductManager.setStorage(market.getStorage());
            ProductManager.setStore(market.getStore());
            MarketManager.setMarket(market);
        }
        running = true;

        while (running) {
            System.out.println("-------- Welcome to the Stock menu of market number " + Integer.parseInt(numOfMarketToManagement) + " --------");
            System.out.println("1) Product menu ");
            System.out.println("2) Reports menu ");
            System.out.println("3) Market menu ");
            System.out.println("4) Exit. ");
            System.out.println("Select the number you would like to access.");
            System.out.println("-----------------------");
            String selection = input.nextLine();
            switch (selection) {
                case "1":
                    productUi.startMenu(numOfMarketToManagement, localDate);
                    break;

                case "2":
                    reportsUI.startMenu(numOfMarketToManagement);
                    break;

                case "3":
                    marketUI.startMenu(numOfMarketToManagement);
                    break;

                case "4":
                    running = false;
                    break;

                default:
                    System.out.println("Wrong input");
                    break;
            }
        }
    }

    Boolean checkIfPositiveNumber(String number) {
        return number.matches("[0-9]+") && Integer.parseInt(number) > 0;
    }

    public void updateForNextDay(LocalDate day){
        productService.updateForNextDay(day);
    }
}
