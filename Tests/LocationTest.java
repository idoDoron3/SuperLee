
import DBConnect.Connect;
import Stock.Business.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LocationTest {

    public void setUp() {
        Connect.emptyData();
    }

    @Test
    void setLocation() {
        Location location = new Location(1,5);
        assertEquals(location.getShelfNumber(),1);
        assertEquals(location.getIndexInShelf(),5);
        location.setLocation(2,6);
        assertNotEquals(location.getShelfNumber(),1);
        assertNotEquals(location.getIndexInShelf(),5);
        assertEquals(location.getShelfNumber(),2);
        assertEquals(location.getIndexInShelf(),6);

    }

    @Test
    void getShelfNumber() {
        Location location = new Location(1,5);
        assertEquals(location.getShelfNumber(),1);
        assertNotEquals(location.getShelfNumber(),6);
    }

    @Test
    void getIndexInShelf() {
        Location location = new Location(1,5);
        assertEquals(location.getIndexInShelf(),5);
        assertNotEquals(location.getShelfNumber(),6);

    }

    @Test
    void getLocation() {
        int[] loc = {1, 5};
        Location location = new Location(1,5);
        assertEquals(location.getLocation()[0],loc[0]);
        assertEquals(location.getLocation()[1],loc[1]);

    }
}