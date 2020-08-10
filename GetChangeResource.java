package com.mycode.vendingmachine.rest;

import java.util.Collection;
import java.util.Iterator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycode.vendingmachine.VendingMachine;
import com.mycode.vendingmachine.model.Coin;

import static com.mycode.vendingmachine.constants.VendingMachineConstants.HORIZONTAL_RULE;
import static com.mycode.vendingmachine.constants.VendingMachineConstants.HTML_LINE_BREAK;

public class GetChangeResource {
    
    VendingMachine vendingMachine = new VendingMachine();
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetChangeResource.class);
    
    @GET
    @Path(value = "/getChange")
    @Produces(MediaType.TEXT_HTML)
    public String processChange(@QueryParam("cashInsertedInPence") String cashInsertedInPence,
                                @QueryParam("productCostInPence") String productCostInPence) {
        
        if (!StringUtils.isBlank(inputsAreInvalid(cashInsertedInPence, productCostInPence))) {
            return inputsAreInvalid(cashInsertedInPence, productCostInPence);
        }
        
        StringBuilder responseString = new StringBuilder();
        responseString.append("A limited inventory of coins at - src/main/resources/META-INF/config/coin-inventory.properties")
                      .append(HORIZONTAL_RULE);
        Collection<Coin> coins = null;
        
        int changeToBeReturned = Integer.valueOf(cashInsertedInPence) - Integer.valueOf(productCostInPence);
        responseString.append("Change to be returned: ").append(changeToBeReturned).append(" pence")
                                                                                   .append(HTML_LINE_BREAK)
                                                                                   .append(HTML_LINE_BREAK);
        
        try {
            
            vendingMachine.emptyCoinDispenser();
            
            coins = vendingMachine.getChangeFor(changeToBeReturned);
            
            responseString.append("Coins in dispenser: ").append(coins.size())
                                                         .append(HTML_LINE_BREAK)
                                                         .append(HTML_LINE_BREAK)
                                                         .append(HORIZONTAL_RULE);
            Iterator<Coin> iterator = coins.iterator();
            while (iterator.hasNext()) {
                responseString.append(iterator.next()).append(HTML_LINE_BREAK);
            }
        } catch (Exception ex) {
            LOGGER.error("Exception from REST resource", ex);
            responseString.append("An internal error occured. Read logs to find out more.");
        }
        
        return responseString.toString();
    }
    private String inputsAreInvalid(String cashInsertedInPence, String productCostInPence) {
        
        StringBuilder responseString = new StringBuilder();
        
        if (StringUtils.isBlank(cashInsertedInPence)) {
            responseString.append("A numeric value for cashInsertedInPence is mandatory")
                          .append(HTML_LINE_BREAK);
        }
        
        if (StringUtils.isBlank(productCostInPence)) {
            responseString.append("A numeric value for productCostInPence is mandatory")
                          .append(HTML_LINE_BREAK);
        }
        
        if (!StringUtils.isNumeric(cashInsertedInPence)) {
            responseString.append("cashInsertedInPence must be numeric")
                          .append(HTML_LINE_BREAK);
        }
        
        if (!StringUtils.isNumeric(productCostInPence)) {
            responseString.append("productCostInPence must be numeric")
                          .append(HTML_LINE_BREAK);
        }
        
        if (cashInsertedInPence.length() > 5) {
            responseString.append("cashInsertedInPence must be between 0-9999")
                          .append(HTML_LINE_BREAK);
        }
        
        if (productCostInPence.length() > 5) {
            responseString.append("productCostInPence must be between 0-9999")
                          .append(HTML_LINE_BREAK);
        }
        
        return responseString.toString();
    }
}
