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
            sb.append("name:").append(String.format("%8s \t\t seen: %d times",keys.get(i),this.map.get(keys.get(i)).size()))
            .append("\n============= \t \t =============\n");
        }
        return sb.toString();
    }
}
