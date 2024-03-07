package Stock.Business;

import Stock.DataAccess.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;


public class ProductManager {

    private static final ProductDAO productDAO = ProductDAO.getInstance();
    private static final ExpDateDAO expDateDAO = ExpDateDAO.getInstance();
    private static final ShortageDAO shortageDAO = ShortageDAO.getInstance();

    private static final DamagedProductDAO damagedProductDAO = DamagedProductDAO.getInstance();
    private static final ProductDetailsDAO productDetailsDAO = ProductDetailsDAO.getInstance();
    private static final CategoryDAO categoryDAO = CategoryDAO.getInstance();


    private static ProductManager instance = null;
    private static Store store;
    private static Storage storage;
    private static Shortages shortages;

    private ProductManager() {
        // private constructor

    }
    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }


    // Case 1 at Product's menu
    /**
     * Add a new product to the system.
     * @param categoryStr the name of the product category.
     * @param subCategoryStr the name of the product sub-category.
     * @param subSubCategoryStr the name of the product sub-sub-category.
     * @param manufacturer the name of the product manufacturer.
     * @param quantity the quantity of the new product to add.
     * @param minQuantity the minimum quantity of the new product to have in stock.
     * @param weight the weight of the new product.
     * @param expirationDate the expiration date of the new product.
     * @return true if the product was successfully added, false otherwise.
     */
    public Product addNewProduct(String categoryStr, String subCategoryStr, String subSubCategoryStr, String manufacturer,
                                 int quantity, int minQuantity, double weight, Date expirationDate) {
        // New one
        if (getProductByCategories(subCategoryStr, subSubCategoryStr) == null) {
            AProductCategory Ccategory = new AProductCategory(categoryStr);
            categoryDAO.writeNewCategory(categoryStr, 0);
            AProductCategory CsubCategoryStr = new AProductCategory(subCategoryStr);
            String[] parts = subSubCategoryStr.split(" ");
            double number = Double.parseDouble(parts[0]);
            String unit = parts[1];
            AProductSubCategory CsubSubCategoryStr = new AProductSubCategory(number, unit);
            Location storageLocation = new Location(-1, -1);
            Location storeLocation = new Location(-1, -1);
            Product product = new Product(Ccategory, CsubCategoryStr, CsubSubCategoryStr, storageLocation, storeLocation,
                    manufacturer, quantity, minQuantity, weight, expirationDate);
            productDAO.addNewProductToProducts(product);
            product.setStoreLocation(store.addProductToStore(product));
            product.setStorageLocation(storage.addProductToStorage(product));
            product.setCatalogNumber();
            productDAO.writeProducts();
            productDetailsDAO.saveDetails();
            return product;
        } else {
            return null;
        }
    }

    // ------------ Helper function for Case 2.1 in Product UI ------------
    public Product getProductByCategories(String subCategory,String subSubCategory){
        String[] subsubSplited = subSubCategory.split(" ");
        String name = subCategory + " " + Double.toString(Double.parseDouble(subsubSplited[subsubSplited.length - 2]))
                + " " + subsubSplited[subsubSplited.length - 1];
        String productCatalogNumber = UniqueStringGenerator.generateUniqueString(name);
        return productDAO.getProduct(productCatalogNumber);
    }

    // Case 2.1 at Product's menu
    public void addMoreItemsToProduct(Product product, Date expDate, int quantity){
        product.addMoreItemsToProduct(quantity,expDate);
        if (shortageDAO.isInShortage(product.getCatalogNumber())) {
            shortageDAO.deleteFromShortages(product.getCatalogNumber());
        }
        productDAO.writeProducts();
        expDateDAO.writeExpDates();
        shortageDAO.writeShortages();
        productDetailsDAO.saveDetails(); // Freshie check
    }

    // Case 2.2 at Product's menu
    public boolean sellProductsByUniqueCode(Product soldProduct, int quantitySold){
        int[] sold = soldProduct.sellMultipleItemsFromProduct(quantitySold);
        if(sold == null){
            return false;
        }
        boolean isProductOutOfStockNow = false;
        for(Integer qr : sold){
            expDateDAO.deleteExpDate(qr);
        }
        if (soldProduct.getStoreQuantity() == 0) {
            shortages.addProductToShortages(soldProduct);
//            shortageDAO.addToShortages(soldProduct.getCatalogNumber());
//            shortageDAO.writeShortages();
            isProductOutOfStockNow = true;
        }
        productDAO.writeProducts();
        expDateDAO.writeExpDates();
        productDetailsDAO.saveDetails(); // Freshie check
        return isProductOutOfStockNow;
    }


    // ------------ Helper function for Case 3 in Product UI ------------
    public Product getProductByUniqueCode(String productCatalogNumber) {
        return productDAO.getProduct(productCatalogNumber);
    }

    // Case 3 at Product's menu
    public boolean markAsDamaged(Product defectedProduct, int uniqueCode, String reason){
        if (expDateDAO.isQRfromCatalogNumber(defectedProduct.getCatalogNumber(),uniqueCode)) {
            defectedProduct.markAsDamaged(uniqueCode, reason);
            damagedProductDAO.writeDamagedProduct(uniqueCode,defectedProduct.getCatalogNumber(),reason);
            defectedProduct.getExpirationDates().remove(uniqueCode);
//            expDateDAO.deleteExpDate(uniqueCode);
            if(defectedProduct.getStorageQuantity() > 0){
                defectedProduct.storageQuantityMinus1();
            }
            else{
                if(defectedProduct.getStoreQuantity() > 0) {
                    defectedProduct.storeQuantityMinus1();
                }
            }
//            productDAO.writeProducts();
            damagedProductDAO.writeDamagedProducts();
            return true;

        }
        else {
            System.out.println("the QR is does not belong to this Catalog Number! ");
            return false;
        }
    }

    // Case 4 at Product's menu
    /**
     * Prints the specified information of the given product to the console.
     * @param productInformationCase an integer indicating which piece of information to print:
     *                               - 1 for the product catalog number
     *                               - 2 for the product name
     * @param product the product whose information to print
     */
    public void printProductInformation(int productInformationCase, Product product) {
        if (productInformationCase == 1) {
            System.out.println(product.getCatalogNumber());
        }
        if (productInformationCase == 2) {
            System.out.println(product.getName());
        }
    }

    // ------------ Case 5 in Product UI ------------
    public boolean setMinimumQuantity(Product product, int newMinQuantity) {
        product.setMinimumQuantity(newMinQuantity);
        System.out.println("The new minimum quantity of " + product.getName() + " is " + newMinQuantity);
        productDAO.writeProducts();
        return true;
    }


    public static void setStore(Store store) {
        ProductManager.store = store;
    }

    public static void setStorage(Storage storage) {
        ProductManager.storage = storage; }

    public static void setShortages(Shortages shortages) {
        ProductManager.shortages = shortages;
        shortageDAO.writeShortages();
    }

    public HashMap<String,Integer> getAllProducts() {
        HashMap<String, Integer> allProductsToSupplier = new HashMap<>();
        HashMap<String, Product> allProductsFromDB = productDAO.getAllProducts();
        if (allProductsFromDB.size() > 0) {
            for (String catalogNumber : allProductsFromDB.keySet()) {
                int minimumQuantity = allProductsFromDB.get(catalogNumber).getMinimumQuantity();
                allProductsToSupplier.put(catalogNumber, minimumQuantity);
            }
            return allProductsToSupplier;
        }
        return null;
    }

    public ArrayList<String> getAllProductsUnderMinimumQuantity() {
        ArrayList<String> allProductsToSupplier = new ArrayList<>();
        HashMap<String, Product> allProductsFromDB = productDAO.getAllProducts();
        if (allProductsFromDB.size() > 0) {
            for (String catalogNumber : allProductsFromDB.keySet()) {
                int minimumQuantity = allProductsFromDB.get(catalogNumber).getMinimumQuantity();
                Product currProduct = allProductsFromDB.get(catalogNumber);
                if (currProduct.getStoreQuantity() + currProduct.getStorageQuantity()>0 && currProduct.getStoreQuantity() + currProduct.getStorageQuantity() < minimumQuantity) {
                    allProductsToSupplier.add(catalogNumber);
                }
            }
            return allProductsToSupplier;
        }
        return null;
    }

    // ------------ Add more items to product function for supplier usage ------------
    public void addMoreItemsToProductsFromSupplier(HashMap<String, Integer> productsToAdd) {

        LocalDate currentDate = LocalDate.now();
        LocalDate twoWeeksLaterFromNow = currentDate.plus(2, ChronoUnit.WEEKS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String twoWeeksLaterFromNowInString = twoWeeksLaterFromNow.format(formatter);
        String[] parts = twoWeeksLaterFromNowInString.split("/");
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        LocalDate date = LocalDate.of(year, month, day);
        Date dateToAdd = Date.from(date.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant());
        for (String productName: productsToAdd.keySet()) {
            String catalogNumber = UniqueStringGenerator.generateUniqueString(productName);
            Product currProduct = productDAO.getProduct(catalogNumber);
            if(productsToAdd.get(productName)!= null){
                int quantityForCurrProduct = productsToAdd.get(productName);
                addMoreItemsToProduct(currProduct, dateToAdd, quantityForCurrProduct);
            }
        }
    }


    public boolean updateForNextDay(LocalDate futureDay){
        boolean thereIsProductOutOfStockNow = false;
        boolean written = false;
//        Date currentDay = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(currentDay);
//        calendar.add(Calendar.DAY_OF_YEAR, dayDiff); // Add day number from today
//        Date futureDay = calendar.getTime();
        HashMap<String, ArrayList<Integer>> expProducts = expDateDAO.expirationForDate(futureDay);
        for (String catalogNumber: expProducts.keySet()){
            Product product = productDAO.getProduct(catalogNumber);
            for(Integer qr: expProducts.get(catalogNumber)){
                markAsDamaged(product,qr,"Expired in " + futureDay.minusDays(1).toString());
                product.getExpirationDates().remove(qr);
                expDateDAO.deleteExpDate(qr);
                damagedProductDAO.writeDamagedProducts();
//                if(product.getStorageQuantity() > 0){
//                    product.storageQuantityMinus1();
//                }
//                else{
////                    if(product.getStoreQuantity() > 0) {
////                        product.storeQuantityMinus1();
////                    }
//                }
            }
            if(product.isShortage()){
                shortageDAO.addToShortages(product.getCatalogNumber());
                thereIsProductOutOfStockNow = true;
            }
        }
        ProductDAO.getInstance().writeProducts();
        ShortageDAO.getInstance().writeShortages();
        DamagedProductDAO.getInstance().writeDamagedProducts();
        return thereIsProductOutOfStockNow;
    }

    public String productReport(String catalogNumber){
        Product product = productDAO.getProduct(catalogNumber);
        if(product == null){
            return "";
        }
        return product.productInformation();
    }

}
