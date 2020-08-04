package com.abbasdgr8.vendingmachine;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abbasdgr8.vendingmachine.collections.CoinDispenser;
import com.abbasdgr8.vendingmachine.constants.Denomination;
import com.abbasdgr8.vendingmachine.exceptions.CoinInventoryError;
import com.abbasdgr8.vendingmachine.exceptions.NoSuchDenominationException;
import com.abbasdgr8.vendingmachine.model.Coin;

public class VendingMachine {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(VendingMachine.class);
    
    public Collection<Coin> getOptimalChangeFor(int pence) throws NoSuchDenominationException, 
                                                                  CoinInventoryError {
        
        CoinDispenser coinDispenser = CoinDispenser.getInstance();
        coinDispenser.setUnlimitedSupplyOfCoins(true);
        
        pence = (pence < 0) ? 0 : pence;
        int remainingChange = pence;
        
        Set<Integer> denominationKeySet = Denomination.denominationLookup.keySet();
        Integer[] sortedDenominationArray = denominationKeySet.toArray(new Integer[denominationKeySet.size()]);
        
        for(int i = sortedDenominationArray.length - 1; i >= 0; i--) {
            int currentDenominationValue = Denomination.get(sortedDenominationArray[i]).getDenominationValue();
            if (remainingChange >= currentDenominationValue) {
                int currentDenominationCoinCount = remainingChange / currentDenominationValue;
                if (coinDispenser.getCoins(currentDenominationValue, currentDenominationCoinCount))
                remainingChange = remainingChange - currentDenominationCoinCount * currentDenominationValue;
            }
        }
        
        return coinDispenser;
    }
    
    public Collection<Coin> getChangeFor(int pence) throws NoSuchDenominationException, CoinInventoryError {
        
        CoinDispenser coinDispenser = CoinDispenser.getInstance();
        coinDispenser.setUnlimitedSupplyOfCoins(false);
        
        pence = (pence < 0) ? 0 : pence;
        int remainingChange = pence;
        
        Set<Integer> denominationKeySet = Denomination.denominationLookup.keySet();
        Integer[] sortedDenominationArray = denominationKeySet.toArray(new Integer[denominationKeySet.size()]);
        
        for(int i = sortedDenominationArray.length - 1; i >= 0; i--) {
            int currentDenominationValue = Denomination.get(sortedDenominationArray[i]).getDenominationValue();
            if (remainingChange >= currentDenominationValue) {
                int currentDenominationCoinCount = remainingChange / currentDenominationValue;
                if (coinDispenser.getCoins(currentDenominationValue, currentDenominationCoinCount)) {
                    remainingChange = remainingChange - currentDenominationCoinCount * currentDenominationValue;
                }
            }
        }
        
        return coinDispenser;
    }

    public void emptyCoinDispenser() {
        CoinDispenser coinDispenser = CoinDispenser.getInstance();
        coinDispenser.clear();
    }
    
}
