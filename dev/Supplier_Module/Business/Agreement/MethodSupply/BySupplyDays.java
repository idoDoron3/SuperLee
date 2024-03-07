package Supplier_Module.Business.Agreement.MethodSupply;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BySupplyDays extends MethodSupply {

    int days;
    Scanner input = new Scanner(System.in);

    /**constructor
     *
     */
    public BySupplyDays(String x,int days)
    {
        super(x);
        this.days=days;
    }

    /**
     * function that prints how many days it will take the supplier bring the order
     */
    @Override
    public void deliveryDays() {
        System.out.println("The delivery will ship in "+days + "Days");
    }

    /**function that set the days is required for supply
     * ask from a new supplier to enter his new info
     */
    @Override
    public void SetDeliveryDays() {
        System.out.println("Enter how many days is required for you to supply");
        int x=input.nextInt();
        this.days=x;
    }

    public int GetSupplyDate (LocalDate date)
    {
        return days;
    }

    @Override
    public List<Integer> GetSupplyDays() {
        List<Integer> res= new ArrayList<>();
        res.add(this.days);
        return res;
    }


}
