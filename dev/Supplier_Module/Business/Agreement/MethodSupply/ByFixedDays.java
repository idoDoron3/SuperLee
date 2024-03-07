package Supplier_Module.Business.Agreement.MethodSupply;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ByFixedDays extends MethodSupply{

    private int[] supplydays = new int[7];
    Scanner input = new Scanner(System.in);

    /**constructor
     *
     */
    public ByFixedDays(String x, int[] arr)
    {
        super(x);
        for (int i = 0; i < arr.length; i++) {
            if(arr[i]==0)
                supplydays[i]=0;
            else
                supplydays[i]=1;
        }
    }


    /**
     * function that prints the delivery days of the supplier
     */
    @Override
    public void deliveryDays() {
        String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < supplydays.length; i++) {
            if(supplydays[i]==1)
            {
                System.out.println(dayNames[i]);
            }
        }
    }

    /**function that set the delivery Days
     * ask from a new supplier to enter his new supply days
     */
    @Override
    public void SetDeliveryDays() {
        int choice=9;
        for(int i=0; i<7; i++)
        {
            this.supplydays[i]=0;
        }
        while(choice!=0)
        {
            System.out.println("Choose new supply days");
            System.out.println("1.Sunday");
            System.out.println("2.Monday");
            System.out.println("3.Tuesday");
            System.out.println("4.Wednesday");
            System.out.println("5.Thursday");
            System.out.println("6.Friday");
            System.out.println("7.Saturday");
            choice=input.nextInt();

            if(choice>0 && choice<8)
            {
                this.supplydays[choice]=1;
            }
            else
                System.out.println("You insert invalid input");
        }

    }

    public int GetSupplyDate (LocalDate date)
    {
        int current= date.getDayOfWeek().getValue();
        int counter1=1;
        if(current<6)
        {
            for(int i=current+1; i<7 ; i++)
            {
                if(supplydays[i]==1)
                {
                    return counter1;
                }
                counter1++;
            }
        }

        for(int i =0; i<= current; i++)
        {
            if(supplydays[i]==1)
            {
                return counter1;
            }
            counter1++;
        }

        return counter1; // not suppose to return this
    }

    @Override
    public List<Integer> GetSupplyDays() {
        List<Integer> res= new ArrayList<>();
        for(int i=0;i<7;i++)
        {
           if( this.supplydays[i]==1)
               res.add(i+1);
        }
        return res;

    }

}