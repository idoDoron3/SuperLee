package Stock.Business;

public class Location {
    private int[] location;


    /**
     * Constructs a new Stock.Business.Location object with the given shelf number and index in shelf.
     * @param shelfNumber the shelf number of the location, where the shelf number is represented as an integer
     * @param indexInShelf the index in the shelf of the location, where the index in shelf is represented as an integer
     */
    public Location(int shelfNumber, int indexInShelf){
        this.location = new int[] { shelfNumber,indexInShelf};       // shelf number in index 0, index in shelf in index 1.
    }

    /**
     * Set the location of the product in the store.
     * @param shelfNumber The number of the shelf the product is located on.
     * @param indexInShelf The index of the product's location within the shelf.
     * @throws IndexOutOfBoundsException if either the shelfNumber or indexInShelf is negative.
     * @return None
     */
    public void setLocation(int shelfNumber, int indexInShelf){
        this.location[0] = shelfNumber;
        this.location[1] = indexInShelf;

    }

    /**
     * @return integer representing the shelf number.
     */
    public int getShelfNumber(){return location[0];}

    /**
     * @return integer representing the index in shelf.
     */
    public  int getIndexInShelf(){return location[1];}

    /**
     * Returns the location of an item as an array of two integers representing the shelf number and index in shelf, respectively.
     * @return an array of two integers representing the shelf number and index in shelf of the item's location.
     */
    public int[] getLocation(){
        return location;
    }

    /**
     * @return a string representation the shelf number and index in shelf, respectively.
     */
    public String toString(){
        return Integer.toString(getShelfNumber())+" "+Integer.toString(getIndexInShelf());
    }
}
