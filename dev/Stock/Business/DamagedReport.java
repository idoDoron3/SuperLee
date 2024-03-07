//package Stock.Business;
//
//import Stock.DataAccess.DamagedProductDAO;
//import Stock.DataAccess.ProductDAO;
//
//import java.util.*;
//
//public class DamagedReport extends Report {
//
//    protected HashMap<String, Map<Integer, String>> products;
//    private DamagedProductDAO damagedProductDAO;
//    private ProductDAO productDAO;
//
//    /**
//     * Constructs a new Stock.Business.DamagedReport object based on the given Stock.Business.Stock object.
//     * @param allProducts all the products objects to generate the DamagedReport from.
//     */
//    public DamagedReport(HashMap<String,Product> allProducts) {
//        damagedProductDAO = DamagedProductDAO.getInstance();
//        productDAO = ProductDAO.getInstance();
//        products = new HashMap<>();;
//        this.date = new Date();
//        this.id = ++Report.reportsCounter;
////        HashMap<String,Product> allProducts = productDAO.getAllProducts();
//        for(Product product:allProducts.values()){
//            products.put(product.getName(),product.getDamagedProducts());
//        }
////        addProductsToDamagedReport(stock);
//    }
//
//    /**
//     * Adds the damaged products from the given Stock.Business.Stock object to the current Stock.Business.DamagedReport object.
//     * @param stock The Stock.Business.Stock object to extract damaged products from.
//     */
//    public void addProductsToDamagedReport(Stock stock) {
//        Map<Product, Integer []> itemsInStock = stock.getItemsInStock();
//        Map<Integer, String> newProduct = new HashMap<>();
//        Set<Product> allProducts = itemsInStock.keySet();
//        for (Product product : allProducts) {
//            if (!product.getDamagedProducts().isEmpty()) {
//                product.getDamagedProducts().forEach((damagedProductsId, value) -> {
//                    newProduct.put(damagedProductsId, value);
//                });
//            }
//            if(!newProduct.isEmpty()) {
//                products.put(product.getName(), newProduct);
//            }
//        }
//    }
//
//    public void updateDamagedReportFromDataBase(){
//        products = new HashMap<>();;
//        this.date = new Date();
//        this.id = ++Report.reportsCounter;
//        HashMap<String,Product> allProducts = productDAO.getAllProducts();
//        for(Product product:allProducts.values()){
//            products.put(product.getName(),product.getDamagedProducts());
//        }
//    }
//
//    /**
//     * Returns a string representation of the current Stock.Business.DamagedReport object.
//     * @return A string containing the list of products with their associated damaged products and causes, separated by new lines. If the Stock.Business.DamagedReport object doesn't contain any products, the function returns a string stating that there are no damaged items.
//     */
//    @Override
//    public String toString() {
//        StringBuilder stringBuilderStockReport = new StringBuilder();
//        String productInString = "";
//        String productDetails = "";
//        for (String productName : products.keySet()) {
//            productInString = "Product: " + productName;
//            if(!products.get(productName).isEmpty()) {
//                stringBuilderStockReport.append(productInString).append(System.lineSeparator());
//            }
//            Map<Integer, String> productNameData = products.get(productName);
//            for (Integer dataBarCode : productNameData.keySet()) {
//                String dataCause = productNameData.get(dataBarCode);
//                productDetails = "- " + dataBarCode + " : " + dataCause;
//                stringBuilderStockReport.append(productDetails).append(System.lineSeparator());
////                System.out.println("here");
//            }
//        }
//        if (stringBuilderStockReport.toString().isEmpty()){
//            stringBuilderStockReport.append("There are no damaged items").append(System.lineSeparator());
//        }
//        return stringBuilderStockReport.toString();
//    }
//
//}
package Stock.Business;

import Stock.DataAccess.DamagedProductDAO;
import Stock.DataAccess.ProductDAO;

import java.util.*;

public class DamagedReport extends Report {

    protected HashMap<String, Map<Integer, String>> products;
    private DamagedProductDAO damagedProductDAO;
    private ProductDAO productDAO;

    /**
     * Constructs a new Stock.Business.DamagedReport object based on the given Stock.Business.Stock object.
     * @param allProducts all the products objects to generate the DamagedReport from.
     */
    public DamagedReport(HashMap<String,Product> allProducts) {
        damagedProductDAO = DamagedProductDAO.getInstance();
        productDAO = ProductDAO.getInstance();
        products = new HashMap<>();;
        this.date = new Date();
        this.id = ++Report.reportsCounter;
//        HashMap<String,Product> allProducts = productDAO.getAllProducts();
        for(Product product:allProducts.values()){
            products.put(product.getName(),product.getDamagedProducts());
        }
//        addProductsToDamagedReport(stock);
    }

    /**
     * Adds the damaged products from the given Stock.Business.Stock object to the current Stock.Business.DamagedReport object.
     * @param stock The Stock.Business.Stock object to extract damaged products from.
     */
    public void addProductsToDamagedReport(Stock stock) {
        Map<Product, Integer []> itemsInStock = stock.getItemsInStock();
        Map<Integer, String> newProduct = new HashMap<>();
        Set<Product> allProducts = itemsInStock.keySet();
        for (Product product : allProducts) {
            if (!product.getDamagedProducts().isEmpty()) {
                product.getDamagedProducts().forEach((damagedProductsId, value) -> {
                    newProduct.put(damagedProductsId, value);
                });
            }
            if(!newProduct.isEmpty()) {
                products.put(product.getName(), newProduct);
            }
        }
    }

    public void updateDamagedReportFromDataBase(){
        products = new HashMap<>();;
        this.date = new Date();
        this.id = ++Report.reportsCounter;
        HashMap<String,Product> allProducts = productDAO.getAllProducts();
        for(Product product:allProducts.values()){
            products.put(product.getName(),product.getDamagedProducts());
        }
    }

    public HashMap<String, Map<Integer, String>> getProducts() {
        return products;
    }

    /**
     * Returns a string representation of the current Stock.Business.DamagedReport object.
     * @return A string containing the list of products with their associated damaged products and causes, separated by new lines. If the Stock.Business.DamagedReport object doesn't contain any products, the function returns a string stating that there are no damaged items.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilderStockReport = new StringBuilder();
        String productInString = "";
        String productDetails = "";
        for (String productName : products.keySet()) {
            productInString = "Product: " + productName;
            if(!products.get(productName).isEmpty()) {
                stringBuilderStockReport.append(productInString).append(System.lineSeparator());
            }
            Map<Integer, String> productNameData = products.get(productName);
            for (Integer dataBarCode : productNameData.keySet()) {
                String dataCause = productNameData.get(dataBarCode);
                productDetails = "           - " + dataBarCode + " : " + dataCause;
                stringBuilderStockReport.append(productDetails).append(System.lineSeparator());
//                System.out.println("here");
            }
        }
        if (stringBuilderStockReport.toString().isEmpty()){
            stringBuilderStockReport.append("There are no damaged items").append(System.lineSeparator());
        }
        return stringBuilderStockReport.toString();
    }

}