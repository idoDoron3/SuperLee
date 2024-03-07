
import DBConnect.Connect;
import Stock.Business.*;
import Stock.DataAccess.ExpDateDAO;
import Stock.DataAccess.ProductDAO;
import Stock.DataAccess.ShortageDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ShortagesTest {

    private Shortages shortages;
    private Product product;
    String catalogNumber;


    @BeforeEach
    public void setUp() {
        Connect.emptyData();
        AProductCategory category = new AProductCategory("Milk Products");
        AProductCategory subCategory = new AProductCategory("Milk 3%");
        AProductSubCategory subSubCategory = new AProductSubCategory(1,"Litter");
        Location storeLocation = new Location(3,3);
        Location storageLocation = new Location(2,2);
        Date expDate = new Date(2023, Calendar.DECEMBER,12);

        product = new Product(category,subCategory,subSubCategory,storageLocation,storeLocation,
                "Yotvata",0,10,1.2,expDate);
        catalogNumber = product.getCatalogNumber();
        ProductDAO.getInstance().addNewProductToProducts(product);
        shortages = new Shortages();
        shortages.addProductToShortages(product);
        ProductDAO.getInstance().writeProducts();
        ExpDateDAO.getInstance().writeExpDates();
    }

    @Test
    void updateMissing() {
//        assertFalse(shortages.isMissing(product));
        assertTrue(shortages.isMissing(product));
        assertTrue(ShortageDAO.getInstance().isInShortage(catalogNumber));

    }



}