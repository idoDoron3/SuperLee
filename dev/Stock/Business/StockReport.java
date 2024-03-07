package Stock.Business;

import java.util.*;

public class StockReport extends Report{
    // < products category name, <product name (subCategory) and size (subSubCategory), quantity> >
    protected HashMap<String, Map<String, Integer>> products;

    public StockReport(Map<String,Product> stock, ArrayList <String> categories) {
        /**
         * Constructs a new Stock.Business.StockReport object that summarizes the current state of a given Stock.Business.Stock object
         * by category, as specified in the provided list of categories.
         *
         * @param stock the Stock.Business.Stock object to be summarized in the report
         * @param categories a list of categories by which to group the products in the report
         */
        products = new HashMap<>();;
        this.date = new Date();;
        this.id = ++reportsCounter;
        addProductsToStockReport(stock, categories);
    }

    public void addProductsToStockReport(Map<String,Product> stock, ArrayList <String> categories) {
        /**
         * Adds products from a given Stock.Business.Stock object to the current Stock.Business.StockReport object, grouped by the provided list of categories.
         *
         * @param stock the Stock.Business.Stock object to add products from
         * @param categories a list of categories by which to group the products in the report
         */
        for (String category : categories) {
            Map<String, Integer> newCategory = new HashMap<>();
            for (Product product : stock.values()) {
                if (product.getCategory().getName().equals(category)) {
                    newCategory.put(product.getName(), product.getStoreQuantity() + product.getStorageQuantity());
                }
            }
            products.put(category, newCategory);
        }
    }

    @Override
    public String toString() {
        /**
         * Returns a string representation of the current Stock.Business.StockReport object, displaying its contents in a readable format.
         *
         * @return a string containing the contents of the Stock.Business.StockReport object, grouped by category and product name/quantity
         */
        StringBuilder stringBuilderStockReport = new StringBuilder();
        String title = "";
        String productInString = "";
        for (String category : products.keySet()) {
            title = "Category: " + category;
            stringBuilderStockReport.append(title).append(System.lineSeparator());
            Map<String, Integer> categoryData = products.get(category);
            for (String dataName : categoryData.keySet()) {
                Integer dataQuantity = categoryData.get(dataName);
                productInString = "           - " + dataName + ": " + dataQuantity;
                stringBuilderStockReport.append(productInString).append(System.lineSeparator());
            }
        }
        return stringBuilderStockReport.toString();
    }

}
