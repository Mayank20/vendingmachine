package com.mycode.vendingmachine.collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import com.mycode.vendingmachine.exceptions.CoinInventoryError;
import com.mycode.vendingmachine.exceptions.NoSuchDenominationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@ContextConfiguration(locations = {"classpath:spring/server-test.xml"})
public class CoinDispenserTest {
    
    private CoinDispenser testCoinDispenser;
    
    @Before
    public void initialize() {
        testCoinDispenser = CoinDispenser.getInstance();
        testCoinDispenser.clear();
    }
    
    @Test
    public void testCoinDispenserIsSingleton() {
        CoinDispenser anotherCoinDispenser = CoinDispenser.getInstance();
        assertEquals(testCoinDispenser.hashCode(), anotherCoinDispenser.hashCode());
    }
    
    @Test
    public void testGetUnlimitedCoinsSuccessScenario() throws Exception {
        testCoinDispenser.setUnlimitedSupplyOfCoins(true);
        assertTrue(testCoinDispenser.getCoins(10, 10));
    }
    
    @Test(expected = NoSuchDenominationException.class)
    public void testGetUnlimitedCoinsNoDenominationFailureScenario() throws Exception {
        testCoinDispenser.setUnlimitedSupplyOfCoins(true);
        assertFalse(testCoinDispenser.getCoins(11, 1));
    }
    
    @Test
    public void testGetUnlimitedCoinsFailureScenario() throws Exception {
        testCoinDispenser.setUnlimitedSupplyOfCoins(true);
        assertFalse(testCoinDispenser.getCoins(10, 0));
    }
    
    @Test
    public void testGetLimitedCoinsSuccessScenario() throws Exception {
        testCoinDispenser.setUnlimitedSupplyOfCoins(false);
        assertTrue(testCoinDispenser.getCoins(10, 10));
    }
    
    @Test(expected = CoinInventoryError.class)
    public void testGetLimitedCoinsNoSuchCoinFailureScenario() throws Exception {
        testCoinDispenser.setUnlimitedSupplyOfCoins(false);
        assertFalse(testCoinDispenser.getCoins(11, 1));
    }
    
    @Test
    public void testGetLimitedCoinsOutOfCoinsFailureScenario() throws Exception {
        testCoinDispenser.setUnlimitedSupplyOfCoins(false);
        assertFalse(testCoinDispenser.getCoins(100, 10000));
    }
    
}
