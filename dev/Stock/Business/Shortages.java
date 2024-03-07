package Stock.Business;

import Stock.DataAccess.ProductDAO;
import Stock.DataAccess.ShortageDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Shortages {
    private Set<Product> missing;
    private ShortageDAO shortageDAO;
    private ProductDAO productDAO;

    /**
     * Constructs a new Shortages object.
     */
    public Shortages() {
        shortageDAO = ShortageDAO.getInstance();
        productDAO = ProductDAO.getInstance();
        shortageDAO.loadToShortageMap();
        this.missing = new HashSet<>();
        for(String catalogNumber: shortageDAO.getShortageMap()){
            Product product = productDAO.getProduct(catalogNumber);
            missing.add(product);
        }
    }

    /**
     * Adds a missing product to the list of shortages.
     * This method adds the specified Product object to the list of missing items. If the specified product is already in the
     * list of missing items, it will not be added again.
     * @param product the Product object to add to the list of missing items
     */
    public void addProductToShortages(Product product){
        if(!missing.contains(product)){
            missing.add(product);
            shortageDAO.addToShortages(product.getCatalogNumber());
            shortageDAO.writeShortages();
        }
    }

    public void removeFromShortages(Product product){
        if(missing.contains(product)){
            missing.remove(product);
            shortageDAO.deleteFromShortages(product.getCatalogNumber());
//            shortageDAO.writeShortages();
        }
    }

    /**
     * Checks if a product is on the list of missing items.
     * @param product the Product object to check for in the list of missing items
     * @return true if the product is on the list of missing items, false otherwise
     */
    public boolean isMissing(Product product){
        return missing.contains(product);
    }

    public void updateMissing(ArrayList<Product> products){
        /**
         * Updates the list of missing items.
         *
         * This method updates the list of missing items based on the quantities of items in the store and storage. If an item
         * has a store quantity and a storage quantity of 0, it will be added to the list of missing items. The missing list is
         * then updated to only contain the new missing items found.
         *
         * @param products the list of Stock.Business.Product objects to check for missing items
         */
        Set<Product> newMissing = new HashSet<>();
        for(Product product: products){
            if(product.getStoreQuantity() == 0 && product.getStorageQuantity() == 0){
                newMissing.add(product);
            }
        }
        shortageDAO.resetShortages();
        for(Product product: missing){
            shortageDAO.addToShortages(product.getCatalogNumber());
        }
        shortageDAO.writeShortages();
        this.missing = newMissing;

    }

    public void updateMissingFromDataBase(){            // will be used after ProductDAO::updateForNextDay
        this.missing = new HashSet<>();
        for(String catalogNumber: shortageDAO.getShortageMap()){
            Product product = productDAO.getProduct(catalogNumber);
            missing.add(product);
        }
    }

    public HashMap<String, Integer> getMissing() {
        HashMap<String, Integer> productsInShortages = new HashMap<>();
        for(Product product: missing){
            productsInShortages.put(UniqueStringGenerator.convertBackToString(product.getCatalogNumber()), product.getMinimumQuantity() + 20);
        }
        return productsInShortages;
    }


    /**
     * Returns a String representation of the missing items list.
     * This method returns a String representation of the missing items list. Each missing item is listed on a new line.
     * If there are no missing items, the method returns the String "No shortages".
     * @return a String representation of the missing items list
     */
    @Override
    public String toString(){
        StringBuilder report = new StringBuilder();
        for(Product product: missing){
            report.append("          " +product.getName()).append('\n');
        }
        if(report.toString().isEmpty()){
            report.append("No shortages");
        }
        return report.toString();
    }
}
