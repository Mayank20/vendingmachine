package com.mycode.vendingmachine.collections;

import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycode.vendingmachine.exceptions.CoinInventoryError;
import com.mycode.vendingmachine.exceptions.NoSuchDenominationException;
import com.mycode.vendingmachine.model.Coin;


public final class CoinDispenser extends ArrayBlockingQueue<Coin> {
    
    private static final CoinDispenser theCoinDispenser = new CoinDispenser(100);
    
    private final CoinBank coinBank = CoinBank.getInstance();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CoinDispenser.class);
    
    public static CoinDispenser getInstance() {
        return theCoinDispenser;
    }
    
    public synchronized boolean getCoins(int coinDenomination, int numberOfCoinsToRelease) 
                                            throws NoSuchDenominationException, CoinInventoryError {
        
        return coinBank.withdraw(coinDenomination, numberOfCoinsToRelease);
    }
    
    public void setUnlimitedSupplyOfCoins(boolean unlimitedSupplyOfCoins) {
        coinBank.setUnlimitedSupplyOfCoins(unlimitedSupplyOfCoins);
    }
    
    private CoinDispenser(int capacity) {
        super(capacity);
    }
   
    private static class CoinBank {

        private boolean unlimitedSupplyOfCoins;
        
        public static CoinBank getInstance() {
            return theCoinBank;
        }

        public synchronized boolean withdraw(int coinDenomination, int numberOfCoinsToRelease) throws NoSuchDenominationException, CoinInventoryError {
            boolean withdrawSuccess = false;
            
            if (unlimitedSupplyOfCoins) {
                for (int i = 0; i < numberOfCoinsToRelease; i++) {
                    Coin coin = new Coin(coinDenomination);
                    theCoinDispenser.offer(coin);
                    withdrawSuccess = true;
                }
            } else {
                
                try {
                    coinInventory = new PropertiesConfiguration("src/main/resources/META-INF/config/coin-inventory.properties");
                } catch (ConfigurationException ex) {
                    LOGGER.error("There was a problem reading from the Coin inventory", ex);
                    throw new CoinInventoryError("The inventory could not be read", ex);
                }
                
                int coinsRemaining;
                
                try {
                    coinsRemaining = coinInventory.getInt(String.valueOf(coinDenomination));
                } catch (Exception ex) {
                    LOGGER.error("There was a problem fetching details from the Coin inventory", ex);
                    throw new CoinInventoryError("The inventory could not be read", ex);
                }
                
                
                if (coinsRemaining < numberOfCoinsToRelease) {
                    return withdrawSuccess;
                }
                
                for (int i = 0; i < numberOfCoinsToRelease; i++) {
                    Coin coin = new Coin(coinDenomination);
                    theCoinDispenser.offer(coin);
                    coinsRemaining--;
                    coinInventory.setProperty(String.valueOf(coinDenomination), coinsRemaining);
                    try {
                        coinInventory.save();
                    } catch (ConfigurationException ex) {
                        LOGGER.error("There was a problem saving details to the Coin inventory", ex);
                        throw new CoinInventoryError("The inventory could not be saved", ex);
                    }
                }
                
                withdrawSuccess = true;
            }

            return withdrawSuccess;
        }

       
        public synchronized boolean deposit(Collection<Coin> coinsToDeposit) {
            return false;
        }

       
        public void setUnlimitedSupplyOfCoins(boolean unlimitedSupplyOfCoins) {
            this.unlimitedSupplyOfCoins = unlimitedSupplyOfCoins;
        }

        private CoinBank() { }
        
    }
}
