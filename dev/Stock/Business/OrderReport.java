package Stock.Business;

import java.util.*;

public class OrderReport extends Report {
    // <product name (subCategory) and size (subSubCategory), quantity>
    protected HashMap<String, Integer> products;

    /**
     * Constructs a new instance of an Stock.Business.OrderReport.
     * @param stock the Stock.Business.Stock object that contains the inventory information.
     */
    public OrderReport(HashMap<String,Product> stock) {
        products = new HashMap<>();;
        this.date = new Date();;
        this.id = ++reportsCounter;
        addProductsToOrderReport(stock);
    }

    public void addProductsToOrderReport(HashMap<String,Product> stock) {
//        Map<Product, Integer []> itemsInStock = stock.getItemsInStock();
//        Set<Product> allProducts = itemsInStock.keySet();
        int howMuchToOrder;
        for (Product product : stock.values()) {
            int quantity = product.getStorageQuantity()+product.getStoreQuantity();
            if (Integer.compare(quantity,product.getGreenLine()) < 0 ) {
                howMuchToOrder = product.getGreenLine() - (product.getStoreQuantity() + product.getStorageQuantity());
                products.put(product.getName(),howMuchToOrder);
            }
        }
    }

    /**
     * Returns a string representation of the Stock.Business.StockReport object. The string includes the names and quantities of all the products in the report.
     * @return a string containing the names and quantities of all the products in the report
     */
    @Override
    public String toString() {
        StringBuilder stringBuilderStockReport = new StringBuilder();
        String productInString = "";
        for (String productName : products.keySet()) {
            Integer dataQuantity = products.get(productName);
            productInString = "          - " + productName + ": " + dataQuantity;
            stringBuilderStockReport.append(productInString).append(System.lineSeparator());
        }
        return stringBuilderStockReport.toString();
    }
}
