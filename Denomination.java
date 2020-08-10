package com.mycode.vendingmachine.constants;

import java.util.Map;
import java.util.TreeMap;

import com.mycode.vendingmachine.exceptions.NoSuchDenominationException;

public enum Denomination {
    
    ONE_POUND(100, "One pound"), 
    FIFTY_PENCE(50, "Fifty pence"), 
    TWENTY_PENCE(20, "Twenty pence"), 
    TEN_PENCE(10, "Ten pence"), 
    FIVE_PENCE(5, "Five pence"), 
    TWO_PENCE(2, "Two pence"), 
    ONE_PENNY(1, "One penny");
    
    static {
        for (Denomination currentDenomination : Denomination.values()) {
            denominationLookup.put(currentDenomination.getDenominationValue(), currentDenomination);
        }
    }
    
    public static Denomination get(int coinValue) throws NoSuchDenominationException {
        Denomination correspondingDenomination = denominationLookup.get(coinValue);
        if (correspondingDenomination == null) {
            throw new NoSuchDenominationException("A denomination of value-"
                    + coinValue + "does not exist.");
        }
        
        return correspondingDenomination;
    }
    
    
    private final int denominationValue;
    private final String denominationName;
    
    public int getDenominationValue() {
        return denominationValue;
    }

    public String getDenominationName() {
        return denominationName;
    }
    
    private Denomination(int denominationValue, String denominationName) {
        this.denominationValue = denominationValue;
        this.denominationName = denominationName;
    }
}
