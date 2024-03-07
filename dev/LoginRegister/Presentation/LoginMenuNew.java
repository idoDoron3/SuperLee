package LoginRegister.Presentation;


import LoginRegister.Business.LoginManager;
import LoginRegister.Business.RegisterManager;
import Stock.Presentation.StockMainUI;
import Stock.Presentation.StockManagerUI;
import Supplier_Module.Presentation.UI;

import java.time.LocalDate;
import java.util.Scanner;

public class LoginMenuNew {

    private static LoginMenuNew instance = null;

    private static final RegisterManager registerManager = RegisterManager.getInstance();

    private static final LoginManager loginManager = LoginManager.getInstance();


    Scanner input = new Scanner(System.in);

    String username, password, role;

    static LocalDate localDate;

    private UI ui;



    private LoginMenuNew(){
        //private constructor
        localDate = LocalDate.now();
        ui= UI.getUser();
    }

    public static LoginMenuNew getInstance() {
        if (instance == null) {
            instance = new LoginMenuNew();
        }
        return instance;
    }

    public void begin(){
        String choice = "1";
        while (choice != "3") {
            System.out.println("Choose which option you want to enter");
            System.out.println("1) I want to register");
            System.out.println("2) I want to login");
            System.out.println("3) Exit");
            choice = input.nextLine();
            switch (choice) {
                case "1": {
                    receiveInput();
                    if (!registerManager.register(username, password, role)) {
                        System.out.println("This username already exist! ");
                    }
                    else {
                        System.out.println("Your registration was successful, You can login now! ");
                    }
                    break;
                }
                case "2": {
                    receiveInput();
                    String errorMessage = loginManager.login(username,password,role);
                    if (errorMessage.equals("")) {
                        if (role.equals("stock manager")) {
                            StockManagerUI stockManagerUi = new StockManagerUI(localDate);
                            stockManagerUi.startMenu();
                        }
                        else if (role.equals("supplier manager")) {
                            ui.beginSupplierMenu(localDate.plusDays(0));
                        }
                        // store manager
                        else {
                            StoreManagerMenu storeManagerMenu= StoreManagerMenu.getLoginMenu();
                            storeManagerMenu.begin();
                        }
                    }
                    else {
                        System.out.println(errorMessage);
                    }
                    break;
                }
                case "3":
                {
                    System.out.println("Thanks for using our system! Bye Bye");
                    System.exit(0);
                    break;
                }
                default: {
                    System.out.println("Wrong input");
                    break;
                }
            }
        }
    }

    private void receiveInput() {
        //username = input.nextLine();
        boolean running = true;
        while (running) {
            System.out.println("Please enter your username: ");
            username = input.nextLine();
            if (!checkIfUsernameValid(username)) {
                System.out.println("The username must contains only letters and numbers! ");
                continue;
            }
            System.out.println("Please enter your password: ");
            password = input.nextLine();
            System.out.println("Please enter your role: ");
            role = input.nextLine();
            role = role.toLowerCase();
            if (!checkIfRoleValid(role)) {
                System.out.println("This role does not exist! ");
                continue;
            }
            running = false;
        }

    }

    private Boolean checkIfUsernameValid(String username) {
        return username.matches("[a-zA-Z0-9]+");
    }

    private Boolean checkIfRoleValid(String role) {

        return role.equals("stock manager") || role.equals("supplier manager")
                || role.equals("store manager");
    }
    public static LocalDate getLocalDate()
    {
        return localDate;
    }

}
