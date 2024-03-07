package Supplier_Module.Business.Agreement.MethodSupply;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BySuperLee extends MethodSupply{

    /**constructor
     *
     */
    public BySuperLee(String x)
    {
        super(x);
    }

    /**
     * function that prints that this supplier depends on super lee transportation
     */
    @Override
    public void deliveryDays() {
        System.out.println("Transportation under SuperLee's responsibility");
    }

    /**
     * function that prints that this supplier depends on super lee transportation
     */
    @Override
    public void SetDeliveryDays() {
        System.out.println("Transportation under SuperLee's responsibility");
    }

    public int GetSupplyDate (LocalDate date)
    {
        return 1;
    }

    @Override
    public List<Integer> GetSupplyDays() {
        List<Integer> res= new ArrayList<>();
        res.add(1);
        return res;
    }
}
