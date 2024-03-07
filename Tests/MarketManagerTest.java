import DBConnect.Connect;
import Stock.Business.*;
import Stock.DataAccess.ProductDAO;
import Stock.DataAccess.ProductDetailsDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class MarketManagerTest {
    private Product product;
    String catalogNumber;

    @BeforeEach
    public void setUp() {
        Connect.emptyData();
        AProductCategory category = new AProductCategory("Milk Products");
        AProductCategory subCategory = new AProductCategory("Milk 3%");
        AProductSubCategory subSubCategory = new AProductSubCategory(1, "Litter");
        Location storeLocation = new Location(3, 3);
        Location storageLocation = new Location(2, 2);
        Date expDate = new Date(2023, Calendar.DECEMBER, 12);

        product = new Product(category, subCategory, subSubCategory, storageLocation, storeLocation,
                "Yotvata", 40, 10, 1.2, expDate);
        HashMap<Integer, Date> expirationDates = new HashMap<>();
        ProductDetailsDAO.getInstance();
        product.setCatalogNumber();

        for (int i = 0; i < 40; i++) {
            expirationDates.put(ProductDetailsDAO.getInstance().getProductId(), expDate);
        }

        catalogNumber = product.getCatalogNumber();
        ProductDAO.getInstance().addNewProductToProducts(product);
        ProductDAO.getInstance().writeProducts();
    }

    @Test
    void getProductByCategories() {
        String subCategory = product.getSubCategoryName().getName();
        String subSubCategory = product.getSubSubCategory().getName();

        assertEquals(MarketManager.getInstance().getProductByCategories(subCategory, subSubCategory), product);
    }

    @Test
    void setDiscountForProduct() {
//        public boolean setDiscountForProduct(String categoryStr, String subCategoryStr, String subSubCategory, double discount){
        String category = product.getCategory().getName();
        String subCategory = product.getSubCategoryName().getName();
        String subSubCategory = product.getSubSubCategory().getName();

        assertEquals(product.getDiscount(), 0);
        assertEquals(ProductDAO.getInstance().getProduct(product.getCatalogNumber()).getDiscount(), 0);
        MarketManager.getInstance().setDiscountForProduct(product.getCatalogNumber(), 20);

        assertNotEquals(product.getDiscount(), 0);
        assertNotEquals(ProductDAO.getInstance().getProduct(product.getCatalogNumber()).getDiscount(), 0);

        assertEquals(product.getDiscount(), 20);
        assertEquals(ProductDAO.getInstance().getProduct(product.getCatalogNumber()).getDiscount(), 20);

    }


}

