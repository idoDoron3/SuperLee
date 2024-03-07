package Stock.Service;

import Stock.Business.*;
import Stock.DataAccess.ProductDetailsDAO;
import Supplier_Module.Service.SupplierService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ProductService {

    private static final ProductManager productManager = ProductManager.getInstance();
    //private static final ReportsManager reportsManager = ReportsManager.getInstance();



    private static ProductService instance = null;
    private ProductService() {
        // private constructor
    }
    public static ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    // other methods and variables of the class
    // ------------ Case 1 in Product UI ------------
    public boolean addNewProduct(String categoryStr, String subCategoryStr, String subSubCategoryStr, String manufacturer,
                                 int quantity, int minQuantity, double weight, Date expirationDate,LocalDate localDate) {
        Product newProductIfSuccessfullyAdded = productManager.addNewProduct(categoryStr, subCategoryStr, subSubCategoryStr,
                manufacturer, quantity, minQuantity, weight, expirationDate);
        if (newProductIfSuccessfullyAdded != null) {
            HashMap<String, Integer> newProductToSupplier = new HashMap<>();
            newProductToSupplier.put(newProductIfSuccessfullyAdded.getCatalogNumber(), minQuantity);
            //functionToSupplierForNewProduct(newProductToSupplier);
        }
//        SupplierService.getSupplierService().updatePeriodOrders(ProductService.getInstance().sendToSupplierAllProductsQuantity(),localDate);
        return newProductIfSuccessfullyAdded != null;
    }

    // ------------ Helper function for Case 2.1 in Product UI ------------
    public Product getProductByCategories(String subCategoryStr, String subSubCategoryStr) {
        return productManager.getProductByCategories(subCategoryStr, subSubCategoryStr);
    }

    // ------------ Case 2.1 in Product UI ------------
    public void addMoreItemsToProduct(Product product, Date expDate, int quantity) {
        productManager.addMoreItemsToProduct(product, expDate, quantity);

    }

    // ------------ Case 2.2 in Product UI ------------
    public boolean sellProductsByUniqueCode(Product soldProduct, int quantitySold, LocalDate localDate) {
        if (productManager.sellProductsByUniqueCode(soldProduct, quantitySold)) {
            Shortages shortagesForSupplier = new Shortages();
            SupplierService.getSupplierService().lackReport(shortagesForSupplier.getMissing(),localDate);
            return true;
        }
        return false;
    }

    // ------------ Helper function for Case 3 in Product UI ------------
    public Product getProductByUniqueCode(String productCatalogNumber) {
        return productManager.getProductByUniqueCode(productCatalogNumber);
    }

    // ------------ Case 3 in Product UI ------------
    public boolean markAsDamaged(Product defectedProduct, int uniqueCode, String reason) {
        return productManager.markAsDamaged(defectedProduct, uniqueCode, reason);

    }

    // ------------ Case 4 in Product UI ------------
    public void printProductInformation(int productInformationCase, Product product) {
        productManager.printProductInformation(productInformationCase, product);
    }

    // ------------ Case 5 in Product UI ------------
    public void setMinimumQuantity(Product product, int newMinQuantity,LocalDate localDate) {
        if (productManager.setMinimumQuantity(product, newMinQuantity)) {
            HashMap<String, Integer> newMinimumForProductToSupplier = new HashMap<>();
            newMinimumForProductToSupplier.put(product.getCatalogNumber(), newMinQuantity);
            //functionToSupplierForUpdateMinimumToProduct(newMinimumForProductToSupplier);
            SupplierService.getSupplierService().updatePeriodOrders(ProductService.getInstance().sendToSupplierAllProductsQuantity(),localDate);

        }
    }

    public HashMap<String, Integer> sendToSupplierAllProductsQuantity() {
        HashMap<String, Integer> allProductsToSupplier = productManager.getAllProducts(),nameHash = new HashMap<>();
        if (allProductsToSupplier == null) {
            return null;
        }

        for(String catalogNumber: allProductsToSupplier.keySet()){
            String name = UniqueStringGenerator.convertBackToString(catalogNumber);
            int amount = allProductsToSupplier.get(catalogNumber);
            nameHash.put(name,amount);
        }
        return nameHash;
    }

    // ------------ Add more items to product function for supplier usage ------------
    public void addMoreItemsToProductsFromSupplier(HashMap<String, Integer> productsToAdd) {
        productManager.addMoreItemsToProductsFromSupplier(productsToAdd);
    }


    public void updateForNextDay(LocalDate date){
//        LocalDate date = LocalDate.now().plusDays(dayDiff);
        if (productManager.updateForNextDay(date.plusDays(2))) {
            Shortages shortagesForSupplier = new Shortages();
            if (shortagesForSupplier.getMissing().size() > 0) {
                SupplierService.getSupplierService().lackReport(shortagesForSupplier.getMissing(), date);
            }
        }
        ArrayList<String> productsCatalogNumber = new ArrayList<>(),nameList = new ArrayList<>();

        if (productManager.getAllProductsUnderMinimumQuantity() != null) {
            productsCatalogNumber = productManager.getAllProductsUnderMinimumQuantity();
            for(String cataltogNumber: productsCatalogNumber){
                nameList.add(UniqueStringGenerator.convertBackToString(cataltogNumber));
            }
        }
        SupplierService.getSupplierService().ConfirmPeriodOrders(nameList,date);
            //functionToSupplierForNextDay(productsCatalogNumber, shortagesForSupplier.getMissing());

    }

    public String productReport(String catalogNumber){
        return productManager.productReport(catalogNumber);
    }

    public void setProductManager(){
        ProductManager.setShortages(new Shortages());
        ProductManager.setStore(new Store(30));
        ProductManager.setStorage(new Storage(30));
    }

}
