package Stock.Business;

public class Chain {
    private int numberOfMarkets;
    private Market[] markets;

    /**
     * Creates an instance of the Stock.Business.Chain class with the given number of markets.
     * @param numberOfMarkets The number of markets in the chain as an integer.
     * @return None
     */
    public Chain(Integer numberOfMarkets) {
        this.numberOfMarkets = numberOfMarkets;
        markets = new Market[numberOfMarkets];
    }

    /**
     * Returns the market object at the specified index.
     * @param index The index of the market object to retrieve.
     * @return The market object at the specified index, or null if the index is out of range.
     */
    public Market getMarketByIndex(int index) {
        if(index < numberOfMarkets){
            return markets[index];
        }
        return null;
    }

    /**
     * Returns an array of all the market objects in the chain.
     * @return An array of market objects.
     */
    public Market[] getMarkets() {
        return markets;
    }


    /**
     * Returns the number of markets in the chain.
     * @return The number of markets in the chain as an integer.
     */
    public int getNumberOfMarkets() {
        return numberOfMarkets;
    }
}