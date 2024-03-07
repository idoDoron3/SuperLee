package Supplier_Module.Business.Agreement.MethodSupply;

import java.time.LocalDate;
import java.util.List;

/**
 * abstract class that 3 class extends it: ByFixedDays, BySuperLee, BySupplyDays
 */
public abstract class MethodSupply {
    protected String kind;

    /**constructor
     *
     */
    public MethodSupply(String x) {
        this.kind=x;
    }

    /**
     * abstract function
     */
    public abstract void deliveryDays();

    /**
     * function that print the method supply
     */
    public String methodType()
    {
        return kind;
    }

    /**
     * abstract function
     */
    public abstract void SetDeliveryDays();

    public abstract int GetSupplyDate (LocalDate date);

    public abstract List<Integer> GetSupplyDays();

}
