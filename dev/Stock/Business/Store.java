package Stock.Business;

import Stock.DataAccess.ProductDetailsDAO;

public class Store {

    private Shelf[] shelves;
//    private int currShelf;
    private int amountOfShelves;

    private static final ProductDetailsDAO productDetailsDAO = ProductDetailsDAO.getInstance();

    public Store(int numberOfShelves) {
        /**
         * Constructs a new Stock.Business.Store object with the specified number of shelves.
         *
         * @param numberOfShelves the number of shelves to create in the Stock.Business.Store object
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
        return productDetailsDAO.getStoreShelfNumber();
//        return currShelf;
    }

    public int getAmountOfShelves() {
        return amountOfShelves;
    }

    // Add new product from UI menu
    public Location addProductToStore(Product product){
        /**
         * Adds the specified product to the Stock.Business.Store object by placing it on the next available shelf.
         *
         * @param product the product to add to the Stock.Business.Store object
         * @return the Stock.Business.Location object representing the location where the product was placed in the Stock.Business.Store object, or null if no location was available
         */
        Location loc = null;
        boolean running = true;
        //ProductDetailsDAO.getInstance();
        while (running) {

            int currShelf = productDetailsDAO.getStoreShelfNumber();
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
                    productDetailsDAO.updateStoreShelfNumber();
                }
            }

//            else if (currShelf < amountOfShelves - 1){
////                currShelf++;
//                ProductDetailsDAO.updateStoreShelfNumber();
//
//            }
        }

        return loc;
    }
    
    
    public void updateStoreShelvesNumber(int NumberOfShelvesToAdd){
        /**
         * Updates the number of shelves in the store by adding the specified number of shelves.
         *
         * @param numberOfShelvesToAdd The number of shelves to add to the store.
         *                             Must be a positive integer.
         */
        Shelf[] newShelves = new Shelf[amountOfShelves + NumberOfShelvesToAdd];
        for (int i = 0 ; i < amountOfShelves + NumberOfShelvesToAdd; i++){
            newShelves[i] = new Shelf(30);
        }
        for(int i = 0; i<this.shelves.length; i++){
            newShelves[i] = this.shelves[i];
        }
        this.shelves = newShelves;
        amountOfShelves = amountOfShelves + NumberOfShelvesToAdd;
    }
}

