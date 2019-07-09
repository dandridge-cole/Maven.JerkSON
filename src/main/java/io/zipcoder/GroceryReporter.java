package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;

import java.util.*;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;

public class GroceryReporter {
    private final String originalFileText;
    private ItemParser ip = new ItemParser();
    List<Item> items = new ArrayList<>();
    Map<String,List<Double>> map = new LinkedHashMap<>();

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
    }

    public void processInput(){
        this.items = ip.parseItemList(this.originalFileText);
    }

    public void buildMap(){
        for(int i=0; i<items.size();i++){
      //      Pattern nameP = Pattern.compile(item.getName(), CASE_INSENSITIVE);
            String thisName = items.get(i).getName();
            if(this.map.containsKey(thisName)){
                List<Double> tempList= new ArrayList<>();
                tempList.addAll(map.get(thisName));
             //   Collections.copy(tempList,map.get(thisName));
                Double temp=items.get(i).getPrice();
                tempList.add(temp);
                this.map.replace(thisName,tempList);
            }
            else map.put(thisName,Arrays.asList(items.get(i).getPrice()));
        }
    }


    @Override
    public String toString() {
        processInput();
        buildMap();
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<>(this.map.keySet());
        for(int i=0; i<this.map.size();i++){
            String thisKey=keys.get(i);
            sb.append(String.format("name:%8s \t\t seen: %d time",thisKey,this.map.get(thisKey).size()));
            if(this.map.get(thisKey).size()!=1) sb.append("s");
            sb.append("\n============= \t \t =============\n");
            List<Double> prices = map.get(thisKey);
            Map<Double,Integer> thisMap = new LinkedHashMap<>();
            for(Double price:prices) {
                if (thisMap.containsKey(price)) thisMap.replace(price, thisMap.get(price) + 1);
                else thisMap.put(price,1);
            }
            List<Double> unqPrices = new ArrayList<>(thisMap.keySet());
            sb.append(String.format("Price: \t%5.2f\t\t seen: %d time",unqPrices.get(0),thisMap.get(unqPrices.get(0))));
            if(this.map.get(thisKey).size()!=1) sb.append("s");
            sb.append("\n-------------\t\t -------------\n");
            for(int j=1;j<thisMap.size()-1;j++){
                sb.append(String.format("Price: \t%5.2f\t\t seen: %d time",unqPrices.get(j),thisMap.get(unqPrices.get(j))));
                if(this.map.get(thisKey).size()!=1) sb.append("s");
                sb.append("\n-------------\t\t -------------\n");
            }
            if(thisMap.size()>1) {
                sb.append(String.format("Price: \t%5.2f\t\t seen: %d time",unqPrices.get(thisMap.size()-1),thisMap.get(unqPrices.get(thisMap.size()-1))));
                if(this.map.get(thisKey).size()!=1) sb.append("s");
                sb.append("\n");
            }
            sb.append("\n");
        }
        sb.append(String.format("Errors         \t \t seen: %d time",ip.getExceptions()));
        if(ip.getExceptions()!=1) sb.append("s");
        return sb.toString();
    }
}
