

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

class ProductTest {

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
                "Yotvata",40,10,1.2,expDate);
        HashMap<Integer ,Date > expirationDates = new HashMap<>();
        ProductDetailsDAO.getInstance();

        for (int i = 0; i < 40; i++) {
            expirationDates.put(ProductDetailsDAO.getInstance().getProductId(), expDate);
        }

        catalogNumber = product.getCatalogNumber();
        ProductDAO.getInstance().addNewProductToProducts(product);
        ProductDAO.getInstance().writeProducts();
    }


    @Test
    void getName() {
        assertEquals(product.getName(),"Milk 3% 1.0 Litter");
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getName(), "Milk 3% 1.0 Litter");
    }

    @Test
    void getCategory() {
        assertEquals(product.getCategory().getName(),"Milk Products");
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getCategory().getName(), "Milk Products");
    }

    @Test
    void getSubCategoryName() {
        assertEquals(product.getSubCategoryName().getName(),"Milk 3%");
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getSubCategoryName().getName(), "Milk 3%");
    }

    @Test
    void getSubSubCategory() {
        assertEquals(product.getSubSubCategory().getName(),"1.0 Litter");
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getSubSubCategory().getName(), "1.0 Litter");
    }

    @Test
    void getStoreLocation() {
        assertEquals(product.getStoreLocation().toString(),"3 3");
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStoreLocation().toString(), "3 3");
    }

    @Test
    void setStoreLocation() {
        Location newLocation = new Location(11,11);
        product.setStoreLocation(newLocation);
        ProductDAO.getInstance().writeProducts();

        assertNotEquals(product.getStoreLocation().getLocation()[0],3);
        assertNotEquals(product.getStoreLocation().getLocation()[1],3);
        assertEquals(product.getStoreLocation().getLocation()[0],newLocation.getLocation()[0]);
        assertEquals(product.getStoreLocation().getLocation()[1],newLocation.getLocation()[1]);

        assertNotEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStoreLocation().getLocation()[0],3);
        assertNotEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStoreLocation().getLocation()[1],3);
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStoreLocation().getLocation()[0],11);
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStoreLocation().getLocation()[1],11);

        Location previousLocation = new Location(3,3);
        product.setStoreLocation(previousLocation);
        ProductDAO.getInstance().writeProducts();
    }

    @Test
    void getStorageLocation() {
        assertEquals(product.getStorageLocation().toString(),"2 2");
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStorageLocation().toString(), "2 2");
    }

    @Test
    void setStorageLocation() {
        Location newLocation = new Location(1,1);
        product.setStorageLocation(newLocation);
        ProductDAO.getInstance().writeProducts();

        assertNotEquals(product.getStorageLocation().getLocation()[0],2);
        assertNotEquals(product.getStorageLocation().getLocation()[1],2);
        assertEquals(product.getStorageLocation().getLocation()[0],newLocation.getLocation()[0]);
        assertEquals(product.getStorageLocation().getLocation()[1],newLocation.getLocation()[1]);

        assertNotEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStorageLocation().getLocation()[0],2);
        assertNotEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStorageLocation().getLocation()[1],2);
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStorageLocation().getLocation()[0],1);
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStorageLocation().getLocation()[1],1);

        Location previousLocation = new Location(2,2);
        product.setStorageLocation(previousLocation);
        ProductDAO.getInstance().writeProducts();
    }

    @Test
    void getManufacturer() {
        assertEquals(product.getManufacturer(),"Yotvata");
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getManufacturer(), "Yotvata");
    }

    @Test
    void setManufacturer() {
        product.setManufacturer("Tnuva");
        ProductDAO.getInstance().writeProducts();

        assertNotEquals(product.getManufacturer(),"Yotvata");
        assertEquals(product.getManufacturer(), "Tnuva");

        assertNotEquals(ProductDAO.getInstance().getProduct(catalogNumber).getManufacturer(),"Yotvata");
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getManufacturer(),"Tnuva");

        product.setManufacturer("Yotvata");
        ProductDAO.getInstance().writeProducts();
    }

    @Test
    void addToStorage() {
        product.addToStorage(30);
        ProductDAO.getInstance().writeProducts();

        assertNotEquals(product.getStorageQuantity() + product.getStoreQuantity(),40);
        assertEquals(product.getStorageQuantity(),40);
        assertEquals(product.getStorageQuantity() + product.getStoreQuantity(),70);

        assertNotEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStorageQuantity() +
                ProductDAO.getInstance().getProduct(catalogNumber).getStoreQuantity() ,40);
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStorageQuantity(),40);
        assertEquals(ProductDAO.getInstance().getProduct(catalogNumber).getStorageQuantity() +
                ProductDAO.getInstance().getProduct(catalogNumber).getStoreQuantity() ,70);
    }


    @Test
    void setMinimumQuantity() {
        product.setMinimumQuantity(5);
        ProductDAO.getInstance().writeProducts();

        assertNotEquals(product.getMinimumQuantity(),10);
        assertEquals(product.getMinimumQuantity(),5);

    }


    @Test
    void setWeight() {
        assertEquals(product.getWeight(),1.2);

        product.setWeight(1.5);
        ProductDAO.getInstance().writeProducts();

        assertNotEquals(product.getWeight(),1.2);
        assertEquals(product.getWeight(),1.5);
    }

    @Test
    void setDiscount() {
        assertEquals(product.getDiscount(),0);
        product.setDiscount(50);
        assertNotEquals(product.getDiscount(),0);
        assertEquals(product.getDiscount(),50);
    }





    @Test
    void setPrice() {
        product.setPrice(5.5);
//        ProductDAO.getInstance().writeProducts();

        assertEquals(product.getPrice(),5.5);
    }




    @Test
    void markAsDamaged() {
        product.markAsDamaged(23,"not good milk");
        String oldmap = product.getDamagedProducts().toString();

        product.markAsDamaged(32, "another not good milk");
        String newMap = product.getDamagedProducts().toString();

        assertNotEquals(oldmap.toString(),newMap.toString());


    }
}