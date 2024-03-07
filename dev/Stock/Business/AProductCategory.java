package Stock.Business;

public class AProductCategory {

    protected String name;
    private double discount;

    /**
     * Creates an instance of the Bussiness.AProductCategory class with the given name.
     * @param name The name of the product category.
     */
    public AProductCategory(String name) {

        this.name = name;
    }


    /**
     * Returns the name of this product category.
     * @return The name of the product category.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the discount amount for this product category.
     * @return The discount amount as a double.
     */
    public double getDiscount() {
        return discount;
    }

    /**
     * Sets the discount amount for this product category.
     * @param discount The discount amount as a double.
     * @return None
     */
    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
