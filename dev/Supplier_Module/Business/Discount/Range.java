package Supplier_Module.Business.Discount;

public class Range {
    private int min;
    private int max;

    /**constructor
     *
     */
    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /**
     * Getters
     */
    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
    public void setMax(int new_max){
        this.max = new_max;
    }
}
