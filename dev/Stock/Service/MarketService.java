package Stock.Service;

import Stock.Business.Market;
import Stock.Business.MarketManager;

public class MarketService {

    private static MarketManager marketManager = MarketManager.getInstance();
    private static MarketService instance = null;

    private MarketService(){
        //private constructor
    }

    public static MarketService getInstance(){
        if(instance == null){
            instance = new MarketService();
        }
        return instance;
    }

    public void appendMarket(int extraShelves){
        marketManager.addShelves(extraShelves);
    }

    public boolean setDiscountForProduct(String categoryStr, String subCategoryStr, String subSubCategoryStr, double discount){
        return marketManager.setDiscountForProduct(categoryStr,subCategoryStr,subSubCategoryStr,discount);
    }
    public boolean setDiscountForProduct(String catalogNumber, double discount){
        return marketManager.setDiscountForProduct(catalogNumber,discount);
    }
    public boolean setDiscountForCategory(String categoryStr, double discount){
        return marketManager.setDiscountForCategory(categoryStr,discount);
    }






    }
