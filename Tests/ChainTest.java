
import Stock.Business.Market;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Stock.Business.Chain;

    class ChainTest {

        @Test
        void getMarketByIndex() {
            Market market = new Market(30);
            Chain chain = new Chain(3);
            Market[] marketArray = chain.getMarkets();
            for(int i = 0;i<3;i++){
                marketArray[i] = new Market(30);
            }
            assertEquals(chain.getNumberOfMarkets(),3);
            assertNotEquals(chain.getNumberOfMarkets(),2);
            assertEquals(chain.getMarketByIndex(0).getClass(),market.getClass());
            assertEquals(chain.getMarketByIndex(1).getClass(),market.getClass());
            assertEquals(chain.getMarketByIndex(2).getClass(),market.getClass());
            assertNull(chain.getMarketByIndex(3));
        }

    }

