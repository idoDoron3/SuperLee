package Stock.Business;

import Stock.DataAccess.ProductDetailsDAO;

public class Storage {
    private Shelf[] shelves;
//    private int currShelf;
    private int amountOfShelves;
    private static final ProductDetailsDAO productDetailsDAO = ProductDetailsDAO.getInstance();

    public Storage(int numberOfShelves) {
        /**
         * Constructs a new Stock.Business.Storage object with the specified number of shelves.
         *
         * @param numberOfShelves the number of shelves to create in the Stock.Business.Storage object
         */
        this.shelves = new Shelf[numberOfShelves];
//        currShelf = 0;
        amountOfShelves = numberOfShelves;
        for(int i =0; i< numberOfShelves; i++){
            shelves[i] = new Shelf(30);    // DRAMATIC CHANGE !!! CHANGING FOR TEST SHOULD BE 30 -> test passed
        }
    }

    public Shelf[] getShelves() {
        return shelves;
    }

    public int getCurrShelf() {
        //ProductDetailsDAO.getInstance();
        return productDetailsDAO.getStorageShelfNumber();
    }

    public int getAmountOfShelves() {
        return amountOfShelves;
    }

    // Add new product from UI menu
    public Location addProductToStorage(Product product){
        /**
         * Adds a given Stock.Business.Product object to the first available location in the Stock.Business.Storage object.
         *
         * @param product the Stock.Business.Product object to add to the Stock.Business.Storage object
         * @return a Stock.Business.Location object representing the location where the product was added, or null if the Stock.Business.Storage object is full
         */
        Location loc = null;
        boolean running = true;
        //ProductDetailsDAO.getInstance();
        while (running) {

            int currShelf = productDetailsDAO.getStorageShelfNumber();
            if (currShelf == amountOfShelves - 1) {
                running = false;

            }
            else {
                int indexInShelf = shelves[currShelf].nextFreeIndex();
                if (indexInShelf != -1) {
                    loc = new Location(currShelf, indexInShelf);
                    product.setStoreLocation(loc);
                    shelves[currShelf].addItemToShelf(product, indexInShelf);
                    running = false;
                } else {

                    //reset indexInShelf
                    productDetailsDAO.resetIndexInShelf();
                    productDetailsDAO.updateStorageShelfNumber();
                }
            }
        }
//            int currShelf = ProductDetailsDAO.getStorageShelfNumber();
//            int indexInShelf = shelves[currShelf].nextFreeIndex();
//            if (indexInShelf != -1) {
//                loc = new Location(currShelf,indexInShelf);
//                product.setStorageLocation(loc);
//                shelves[currShelf].addItemToShelf(product,indexInShelf);
//                running = false;
//            }
//            else if (currShelf == amountOfShelves - 1) {
//                running = false;
//
//            }
//            else if (currShelf < amountOfShelves - 1){
////                currShelf++;
//                ProductDetailsDAO.resetIndexInShelf();
//
//                ProductDetailsDAO.updateStorageShelfNumber();
//            }
//        }

        return loc;
    }

    public void updateStorageShelvesNumber(int NumberOfShelvesToAdd){
        /**
         * Updates the number of shelves in the Stock.Business.Storage object by adding the specified number of new shelves.
         *
         * @param numberOfShelvesToAdd the number of new shelves to add to the Stock.Business.Storage object
         */
        Shelf[] newShelves = new Shelf[amountOfShelves + NumberOfShelvesToAdd];
        for(int i = 0; i < amountOfShelves + NumberOfShelvesToAdd; i++){
            newShelves[i] = newShelves[30];
        }
        for(int i = 0; i<this.shelves.length; i++){
            newShelves[i] = this.shelves[i];
        }
        this.shelves = newShelves;
        amountOfShelves = amountOfShelves + NumberOfShelvesToAdd;
    }
}
