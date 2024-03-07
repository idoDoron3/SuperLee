package Supplier_Module.Business.Discount;

public class PrecentageDiscount{
    private Range amountRange;
    private double percentage;

    /**constructor
     *
     */
    public PrecentageDiscount(Range amountRange, double percentage) {
        this.amountRange = amountRange;
        this.percentage = percentage;
    }

    /**
     * Getters and Setters
     */
    public Range getAmountRange() {
        return amountRange;
    }
    public double getPercentage() {
        return percentage;
    }



    /**
     * function print discount info
     */
    public void printDiscount()
    {
        System.out.println("Range: "+amountRange.getMin()+" - " +amountRange.getMax()+" the discount is "+this.percentage+"%");
    }
    public String toString(){
        return amountRange.getMin()+"-"+amountRange.getMax()+":"+percentage;
    }

}
