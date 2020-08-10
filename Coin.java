package com.mycode.vendingmachine.model;

import com.mycode.vendingmachine.constants.Denomination;
import com.mycode.vendingmachine.exceptions.NoSuchDenominationException;


public final class Coin {
    
    private final int denomination;
    
    private final String name;

    
    public Coin(int denomination) throws NoSuchDenominationException {
        this.denomination = denomination;
        this.name = Denomination.get(denomination).getDenominationName();
    }
    
    
    public int getDenomination() {
        return denomination;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.denomination;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Coin other = (Coin) obj;
        if (this.denomination != other.denomination) {
            return false;
        }
        return true;
    }
    
}
