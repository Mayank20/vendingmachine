package com.abbasdgr8.vendingmachine;

import com.abbasdgr8.vendingmachine.model.Coin;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertFalse;

@ContextConfiguration(locations = {"classpath:spring/server-test.xml"})
public class VendingMachineTest {
    
    private VendingMachine vendingMachine;
    
    @Before
    public void initialize() {
        vendingMachine = new VendingMachine();
        vendingMachine.emptyCoinDispenser();
    }
    
    @Test
    public void testGetOptimalChangeSuccess() throws Exception {
        Collection<Coin> coinsReturned = vendingMachine.getOptimalChangeFor(100);
        assertFalse(coinsReturned.isEmpty());
    }
    
    
}
