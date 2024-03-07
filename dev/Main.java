import GUI.loginRegisterGui.loginRegisterGUI;
import LoginRegister.Presentation.LoginMenuNew;
import LoginRegister.Presentation.StoreManagerMenu;
import Stock.Presentation.StockManagerUI;
import Supplier_Module.Presentation.UI;

import javax.imageio.IIOException;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDate;


public class Main {
    static LocalDate localDate;

    public static void main(String[] args) {

        UI ui = UI.getUser();
        localDate = LocalDate.now();

        int numberOfArguments = args.length;
        if (numberOfArguments > 0 && numberOfArguments < 3) {
            String howToRun = args[0];
            switch (howToRun) {
                case "GUI":
                    // Call GUI function here
                    try {
                        loginRegisterGUI gui = new loginRegisterGUI();
                        if (numberOfArguments == 1) {
                            gui.run();
                            System.out.println("Running program through GUI in login register screen...");
                            break;
                        } else {
                            String whoLoggedIn = args[1];
                            switch (whoLoggedIn) {
                                case "StockManager":
                                    // Call stock manager GUI
                                    try {
                                        gui.setVisible(true);
                                        gui.openStockManager();
                                        System.out.println("Running program through stock manager GUI...");
                                        break;
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                case "SupplierManager":
                                    // Call supplier manager GUI
                                    try {
                                        gui.setVisible(true);
                                        gui.openSupplierManager();
                                        System.out.println("Running program through supplier manager GUI...");
                                        break;
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                case "StoreManager":
                                    // Call store manager GUI
                                    try {
                                        gui.setVisible(true);
                                        gui.openStoreManager();
                                        System.out.println("Running program through store manager GUI...");
                                        break;
                                    } catch (IOException e) {
                                        System.out.println(e.getMessage());
                                    }
                                default:
                                    System.out.println("Invalid manager in args[1], Please try again.");
                                    break;
                            }
                        }
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                case "CLI":
                    // Call CLI function here
                    LoginMenuNew cli = LoginMenuNew.getInstance();
                    if (numberOfArguments == 1) {
                        cli.begin();
                        System.out.println("Running program through CLI with login register options...");
                        break;
                    } else {
                        String whoLoggedIn = args[1];
                        switch (whoLoggedIn) {
                            case "StockManager":
                                // Call stock manager CLI
                                StockManagerUI stockManagerUi = new StockManagerUI(localDate);
                                stockManagerUi.startMenu();
                                break;
                            case "SupplierManager":
                                // Call supplier manager CLI
                                ui.beginSupplierMenu(localDate.plusDays(0));
                                break;
                            case "StoreManager":
                                // Call store manager CLI
                                StoreManagerMenu storeManagerMenu = StoreManagerMenu.getLoginMenu();
                                storeManagerMenu.begin();
                                break;
                            default:
                                System.out.println("Invalid manager in args[1], Please try again.");
                                break;
                        }
                    }
                default:
                    // if I get in here from GUI or CLI options that end so do not do the print
                    if (!howToRun.equals("CLI") && !howToRun.equals("GUI")) {
                        System.out.println("Invalid way to run in args[0], Please try again.");
                        break;
                    }
            }
        }
        else {
            System.out.println("Invalid args, please try again.");
        }
    }
}