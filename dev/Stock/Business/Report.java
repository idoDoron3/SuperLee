package Stock.Business;

import java.util.Date;
public abstract class Report {
    static int reportsCounter = 0;
    protected Date date;
    protected int id;

    // The function returns the id of the stock report
    public int getId() {
        return this.id;
    }
}
