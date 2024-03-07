package Stock.Business;

import java.util.*;

/**
 * here is the stock.
 * functions:
 *       1.Integer getGreenLine(Stock.Business.Product)
 *       2.Integer getRedLine(Stock.Business.Product)
 *       3.Integer getBlackLine(Stock.Business.Product)
 *       4.void addNewProductToStock(Stock.Business.Product , Integer green, Integer red, Integer black)
 *       5.Integer getStatusInStock(Stock.Business.Product )
 *       6. HashMap<Stock.Business.Product ,Integer[] > getItemsInStock()
 */


public class Stock {
    private HashMap<Product ,Integer[] > stock;
    private  HashMap<String, Integer> categoriesAsStrings;

    private ArrayList<AProductCategory> categoriesAsInstances;

    public Stock() {
        /**
         * Constructs a new Stock.Business.Stock object.
         */
        this.stock = new HashMap<>();
        this.categoriesAsStrings = new HashMap<>();
        this.categoriesAsInstances = new ArrayList<>();
    }

    public Integer getGreenLine(Product product){
        /**
         * Returns the green line value for the specified product.
         *
         * This method returns the green line value for the specified Stock.Business.Product object. If the product is not found in the stock map,
         * the method returns -1.
         *
         * @param product the Stock.Business.Product object to get the green line value for
         * @return the green line value for the specified product, or -1 if the product is not found in the stock map
         */
        Integer[] val = stock.get(product);
        if (val == null){
            return  -1;     // if return -1 the item not in the Map
        }
        return val[0];
    }

    public Integer getRedLine(Product product){
        /**
         * Returns the red line value for the specified product.
         *
         * This method returns the red line value for the specified Stock.Business.Product object. If the product is not found in the stock map,
         * the method returns -1.
         *
         * @param product the Stock.Business.Product object to get the red line value for
         * @return the red line value for the specified product, or -1 if the product is not found in the stock map
         */
        Integer[] val = stock.get(product);
        if (val == null){
            return  -1;     // if return -1 the item not in the Map
        }
        return val[1];
    }

    public Integer getBlackLine(Product product){
        /**
         * Returns the black line value for the specified product.
         * @param product the Stock.Business.Product object to get the black line value for
         * @return the black line value for the specified product, or -1 if the product is not found in the stock map
         */
        Integer[] val = stock.get(product);
        if (val == null){
            return  -1;     // if return -1 the item not in the Map
        }
        return val[2];
    }

    public void addNewProductToStock(Product product, Integer green, Integer red, Integer black){
        /**
         * Adds a new product to the stock.
         *
         * This method adds a new product to the stock with the specified green, red, and black line values. If any of the line values
         * are invalid or the product is already in the stock, the method does nothing.
         *
         * @param product the Stock.Business.Product object to add to the stock
         * @param green the green line value for the product
         * @param red the red line value for the product
         * @param black the black line value for the product
         */
        if (green < red || red  < black || black < 1 || stock.get(product) != null){
            return;
        }
        String cat = product.getCategory().getName();
        if(categoriesAsStrings.containsKey(cat)){
            categoriesAsStrings.replace(cat,categoriesAsStrings.get(cat) + 1 );
        }
        else{
            categoriesAsStrings.put(cat,1);
            categoriesAsInstances.add(product.getCategory());
        }
        Integer[] val = {green,red,black};
        stock.put(product, val);

    }

    public ArrayList<String> getCategories(){
        /**
         * Returns a list of categories in the stock.
         * @return a list of categories as strings
         */
        ArrayList<String> lst = new ArrayList<>(categoriesAsStrings.keySet());
        return lst;
    }

    public int getStatusInStock(Product product){
        /**
         * Returns the status of the specified product in the stock.
         *
         * This method returns an integer representing the status of the specified product in the stock. The status is determined based on the quantity of the product in storage and store, and the red, green, and black lines of the product.
         *
         * @param product the product to check the status for
         * @return an integer representing the status of the product in the stock: 1 if the product quantity is below the black line, 2 if the product quantity is above the black line but below the red line, 3 if the product quantity is above the red line, and -1 if the product was not found in the stock
         */
        int status = 0;
        Integer[] val = stock.get(product);
        if(val == null){
            return -1;      // if -1 the item was not found in the stock
        }
        int quantity = product.getStorageQuantity() + product.getStoreQuantity();
        if(quantity <= val[2]){
            status = 1;
        }
        if(val[2] < quantity && quantity <= val[1])  {
            status = 2;
        }
        if(val[1] < quantity){
            status = 3;
        }
            return status;
    }

    public Product findProductByCatalogNumber(String catalogNumber) {
        /**
         * Finds a product in the stock by its catalog number.
         *
         * @param catalogNumber the catalog number of the product to find
         * @return the product with the given catalog number, or null if not found
         */
        for (Product product : stock.keySet()) {
            if (product.getCatalogNumber().equals(catalogNumber)) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> getListOfProducts(){
        ArrayList<Product> products = new ArrayList<>(stock.keySet());
        return products;
    }

    public HashMap<Product ,Integer[] > getItemsInStock() {
        return stock;
    }

    public boolean isCategoryExist(String categoryStr) {
        /**
         * Checks if a given category exists in the stock.
         *
         * @param categoryStr a string representing the name of the category to search for
         * @return true if the category exists in the stock, false otherwise
         */
        return categoriesAsStrings.containsKey(categoryStr);
    }

    public AProductCategory getAProductCategory(String categoryStr) {
        /**
         * Given a string representing a product category, returns an instance of
         * Bussiness.AProductCategory that corresponds to that category. Searches through the
         * list of categories currently in stock and returns the first match. If no
         * category matches the given string, returns null.
         *
         * @param categoryStr a string representing the name of the category to look for
         * @return an instance of Bussiness.AProductCategory representing the matching category,
         *         or null if no match is found
         */
        for (AProductCategory category : categoriesAsInstances) {
            if (category.getName().equals(categoryStr)) {
                return category;
            }
        }
        return null; // Category not found
    }
}
