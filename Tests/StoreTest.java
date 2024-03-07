
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

class StoreTest {

    String catalogNumber;
    Product product;
    Store store;
    Location firstItem;

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
                "Yotvata",40,10,1.2,expDate);
        HashMap<Integer ,Date > expirationDates = new HashMap<>();
        ProductDetailsDAO.getInstance();

        for (int i = 0; i < 40; i++) {
            expirationDates.put(ProductDetailsDAO.getInstance().getProductId(), expDate);
        }
        firstItem = new Location(0,0);
        store = new Store(30);
        catalogNumber = product.getCatalogNumber();
        ProductDAO.getInstance().addNewProductToProducts(product);
        ProductDAO.getInstance().writeProducts();
    }


    @Test
    void addProductToStore() {
        product.setStoreLocation(store.addProductToStore(product));
        assertEquals(store.getShelves()[0].getItems()[0], product);
        assertEquals(product.getStoreLocation().getLocation()[0], firstItem.getLocation()[0]);
        assertEquals(product.getStoreLocation().getLocation()[1], firstItem.getLocation()[1]);

        store.updateStoreShelvesNumber(40);
        assertEquals(store.getShelves()[0].getItems()[0], product);
    }
}