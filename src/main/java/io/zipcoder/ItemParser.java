package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class ItemParser {
    Integer getExceptions() {
        return exceptions;
    }

    private Integer exceptions=0;

    public List<Item> parseItemList(String valueToParse) {
        List<String> itemStrings = divideList(valueToParse);
        List<Item> itemList = new ArrayList<>();

        for(String s: itemStrings){
            try{
                Item item = parseSingleItem(s);
                itemList.add(item);
            } catch (ItemParseException ipe){
                this.exceptions++;
                System.out.println("Exception # "+this.exceptions+": "+s);
            }
        }
        return itemList;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        Pattern p = Pattern.compile("[;(##)]");
        List<String> fields = Arrays.asList(p.split(singleItem));
        if (fields.size()!=4) throw new ItemParseException();
        String name="";
        Double price=0.0;
        String type="";
        String expiration="";
        p=Pattern.compile("[:@^*%]");
        Pattern nameP = Pattern.compile("name", CASE_INSENSITIVE);
        Pattern typeP = Pattern.compile("type", CASE_INSENSITIVE);
        Pattern expP = Pattern.compile("expiration", CASE_INSENSITIVE);
        Pattern priceP = Pattern.compile("price", CASE_INSENSITIVE);
        for(String s:fields){
            String[] field = p.split(s);
            if(field.length!=2) throw new ItemParseException();
            if(nameP.matcher(field[0]).matches()) name=Character.toUpperCase(field[1].charAt(0))+field[1].substring(1).toLowerCase();
            else if (typeP.matcher(field[0]).matches()) type=field[1].toLowerCase();
            else if (expP.matcher(field[0]).matches()) expiration=field[1];
            else if (priceP.matcher(field[0]).matches()) {
                try {
                    price = Double.parseDouble(field[1]);
                } catch (NumberFormatException nfe){
                    throw new ItemParseException();
                }
            }
            else throw new ItemParseException();
        }
        return new Item(name,price,type,expiration);
    }

    private List<String> divideList(String list) {
        Pattern p = Pattern.compile("##");
        return Arrays.asList(p.split(list));
    }
}
