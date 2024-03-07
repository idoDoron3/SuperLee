package Stock.Service;

import Stock.Business.ReportsManager;
import Stock.Business.Shortages;

public class ReportsService {
    private final static ReportsManager reportsManager = ReportsManager.getInstance();
    private static ReportsService instance = null;

    private ReportsService(){
        // private constructor
    }
    public static ReportsService getInstance(){
        if(instance == null){
            instance = new ReportsService();
        }
        return instance;
    }

    public boolean createStockReport(){
        return reportsManager.createStockReport();
    }
    public String getStockReport(){
        return reportsManager.getStockReport();
    }

    public boolean createStockReportForCategory(String category){
        return reportsManager.createStockReportForCategory(category);
    }

    public String getStockReportForCategory(String Category){
        return reportsManager.getStockReportForCategory(Category);
    }

    public boolean createOrderReport(){
        return reportsManager.createOrderReport();
    }

    public String getOrderReport(){
        return reportsManager.getOrderReport();
    }
    public boolean createDamagedReport(){
        return reportsManager.createDamagedReport();
    }

    public String getDamagedReport(){return reportsManager.getDamagedReport();}
    public Shortages createShortageReport(){
        return reportsManager.createShortageReport();
    }

    public String getShortagesReport(){ return reportsManager.getShortagesReport(); }

}
