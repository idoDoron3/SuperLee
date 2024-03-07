package Stock.Business;

import Stock.DataAccess.ProductDetailsDAO;

import java.util.ArrayList;
import java.util.Date;

public class Market {
    Store store;
    Storage storage;
    //Sales sales;
    Shortages shortages;
    //Stock stock;
    ArrayList<Report> allReports;

    /**
     * Creates a new Stock.Business.Market object with a specified number of shelves.
     * @param numberOfShelves The number of shelves in the Stock.Business.Market.
     */
    public Market(int numberOfShelves) {
        store = new Store(numberOfShelves);
        storage = new Storage(numberOfShelves);
        //sales = new Sales();
        shortages = new Shortages();
        //stock = new Stock();
        allReports = new ArrayList<>();
    }

    // Case 1 in UI
//    public boolean addNewProduct(String categoryStr, String subCategoryStr, String subSubCategoryStr, String manufacturer,
//                                 int quantity, int minQuantity, double weight, Date expirationDate) {
//        /**
//         * Add a new product to the system.
//         * @param categoryStr the name of the product category.
//         * @param subCategoryStr the name of the product sub-category.
//         * @param subSubCategoryStr the name of the product sub-sub-category.
//         * @param manufacturer the name of the product manufacturer.
//         * @param quantity the quantity of the new product to add.
//         * @param minQuantity the minimum quantity of the new product to have in stock.
//         * @param weight the weight of the new product.
//         * @param expirationDate the expiration date of the new product.
//         * @return true if the product was successfully added, false otherwise.
//         */
//        if (getProductByCategories(categoryStr, subCategoryStr, subSubCategoryStr) == null) {
//            AProductCategory Ccategory = new AProductCategory(categoryStr);
//            AProductCategory CsubCategoryStr = new AProductCategory(subCategoryStr);
//            String[] parts = subSubCategoryStr.split(" ");
//            double number = Double.parseDouble(parts[0]);
//            String unit = parts[1];
//            AProductSubCategory CsubSubCategoryStr = new AProductSubCategory(number, unit);
//            Location storageLocation = new Location(-1, -1);
//            Location storeLocation = new Location(-1, -1);
//            Product product = new Product(Ccategory, CsubCategoryStr, CsubSubCategoryStr, storageLocation, storeLocation,
//                    manufacturer, quantity, minQuantity, weight, expirationDate);
//            //stock.addNewProductToStock(product, minQuantity + 100, minQuantity + 30, minQuantity);
//            product.setStoreLocation(store.addProductToStore(product));
//            product.setStorageLocation(storage.addProductToStorage(product));
//            product.setCatalogNumber();
//            System.out.println(product.getName() + " : " + (ProductDetailsDAO.getInstance().getProductIdNoUpdate() - quantity + 1) + "-" + ProductDetailsDAO.getInstance().getProductIdNoUpdate());
//            return true;
//        } else {
//            return false;
//        }
//    }


    // Case 2 in UI
//    public Product getProductByCategories(String categoryStr, String subCategoryStr, String subSubCategoryStr) {
//        /**
//         * Returns a product object matching the specified category, sub-category, and sub-sub-category.
//         * @param categoryStr the category of the product as a string.
//         * @param subCategoryStr the sub-category of the product as a string.
//         * @param subSubCategoryStr the sub-sub-category of the product as a string.
//         * @return the product object matching the specified categories, or null if no such product is found.
//         * @throws NullPointerException if any of the input parameters are null.
//         */
//        String name = subCategoryStr + " " + subSubCategoryStr;
//        String catalogNumber = UniqueStringGenerator.generateUniqueString(name);
//        return stock.findProductByCatalogNumber(catalogNumber);
//    }

//    public boolean sellProductsByID(String productCatalogNumber, int quantitySold) {
//        /**
//         * Sells a specific quantity of a product by its catalog number.
//         * @param productCatalogNumber the catalog number of the product being sold
//         * @param quantitySold the quantity of the product being sold
//         * @return true if the sale was successful, false otherwise
//         * @throws IllegalArgumentException if the quantitySold is negative
//         */
//        Product product = getByProductID(productCatalogNumber);
//        if (product == null) {
//            return false;
//        }
//        int[] sold = product.sellMultipleItemsFromProduct(quantitySold);
////        for (int i = 0; i < quantitySold; i++) {
////            sales.addSale(product, sold[i]);
////        }
//        if (product.getStoreQuantity() == 0) {
//            shortages.addProductToShortages(product);
//        }
//
//        return true;
//    }

    /**
     * Increase the number of shelves in the storage area by the given amount.
     * @param addedShelves the number of shelves to be added to the storage area.
     * @return true if the operation was successful, false otherwise.
     */
    public boolean appendStorage(int addedShelves) {
        storage.updateStorageShelvesNumber(addedShelves);
        return true;
    }

    /**
     * Increases the number of shelves in the store by the specified amount.
     * @param addedShelves the number of shelves to add to the store
     * @return true, indicating that the shelves have been successfully added
     */
    public boolean appendStore(int addedShelves) {
        store.updateStoreShelvesNumber(addedShelves);
        return true;
    }

    // Case 5 in UI
//    public Product getByProductID(String productCatalogNumber) {
//        /**
//         * Retrieves a product from the stock based on its product ID.
//         * @param productCatalogNumber the product ID of the desired product
//         * @return the product associated with the given ID, or null if no such product is found
//         */
//        return stock.findProductByCatalogNumber(productCatalogNumber);
//    }

    // Case 6 in UI
//    public boolean createStockReport() {
//        /**
//         * Generates a stock report and adds it to the list of all reports.
//         * @return {@code true} if the stock report is successfully created and added to the list of all reports,
//         *         {@code false} otherwise
//         */
//        try {
//            StockReport stockReport = new StockReport(stock, stock.getCategories());
//            stockReport.addProductsToStockReport(stock, stock.getCategories());
//            allReports.add(stockReport);
//            System.out.println("-------- Stock.Business.Stock Stock.Business.Report: --------");
//            System.out.println(stockReport);
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

//    public boolean createOrderReport() {
//        /**
//         * Generates an order report and adds it to the list of all reports.
//         * @return {@code true} if the order report is successfully created and added to the list of all reports,
//         *         {@code false} otherwise
//         */
//        try {
//            OrderReport orderReport = new OrderReport(stock);
//            orderReport.addProductsToOrderReport(stock);
//            allReports.add(orderReport);
//            System.out.println("-------- Order Stock.Business.Report: --------");
//            System.out.println(orderReport);
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

//    public boolean createDamagedReport() {
//        /**
//         * Generates a damaged report and adds it to the list of all reports.
//         *
//         * @return {@code true} if the damaged report is successfully created and added to the list of all reports,
//         *         {@code false} otherwise
//         */
//        try {
//            DamagedReport damagedReport = new DamagedReport(stock);
//            damagedReport.addProductsToDamagedReport(stock);
//            allReports.add(damagedReport);
//            System.out.println("-------- Damaged Stock.Business.Report: --------");
//            System.out.println(damagedReport);
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

    // Case 7 in UI
//    public void printProductInformation(int productInformationCase, Product product) {
//        /**
//         * Prints the specified information of the given product to the console.
//         * @param productInformationCase an integer indicating which piece of information to print:
//         *                               - 1 for the product catalog number
//         *                               - 2 for the product name
//         * @param product the product whose information to print
//         */
//        if (productInformationCase == 1) {
//            System.out.println(product.getCatalogNumber());
//        }
//        if (productInformationCase == 2) {
//            System.out.println(product.getName());
//        }
//    }

    // Case 8 in UI
//    public boolean isCategoryExist(String categoryStr) {
//        /**
//         * Checks if a category with the specified name exists in the stock.
//         * @param categoryStr the name of the category to check for
//         * @return {@code true} if a category with the specified name exists in the stock, {@code false} otherwise
//         */
//        return stock.isCategoryExist(categoryStr);
//    }

//    public void setDiscountToCategory(String categoryStr, double discount) {
//        /**
//         * Sets a discount to the products of the specified category.
//         * @param categoryStr the name of the category to set the discount for
//         * @param discount the discount percentage to apply to the products of the specified category
//         */
//        AProductCategory aProductCategory = stock.getAProductCategory(categoryStr);
//        aProductCategory.setDiscount(discount);
//    }


    // case 9
//    public void printSold(){
//        /**
//         * Prints the list of sold products to the console.
//         */
//        System.out.println("-----Sold-----");
//        System.out.println(sales.toString());
//    }

    /**
     * Prints the list of products that are currently in shortage to the console.
     */
    public void printShortages(){
        System.out.println("-----Shortages-----");
        System.out.println(shortages.toString());
    }

    /**
     * @return Store representing the store of the market.
     */
    public Store getStore() {       // freshie change
        return store;
    }

    /**
     * @return Storage representing the storage of the market.
     */
    public Storage getStorage() {       // freshie change
        return storage;
    }

    /**
     * @return Shortages representing the shortages in the market.
     */
    public Shortages getShortages() { return shortages; }
}