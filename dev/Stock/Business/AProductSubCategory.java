package Stock.Business;

public class AProductSubCategory extends AProductCategory {

    private double amount;
    private String unit;

    /**
     * Creates an instance of the Stock.Business.AProductSubCategory class with the given amount and unit.
     * @param amount The amount of the subcategory as a double.
     * @param unit The unit of measurement for the subcategory as a string.
     * @return None
     */
    public AProductSubCategory(double amount, String unit) {
        super((amount + " " + unit));
        this.amount = amount;
        this.unit = unit;
    }

    /**
     * Returns the amount of this product subcategory.
     * @return The amount of the subcategory as a double.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the unit of measurement for this product subcategory.
     * @return The unit of measurement as a string.
     */
    public String getUnit() {
        return unit;
    }

}
