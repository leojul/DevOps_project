import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class Dataframe {
    private LinkedHashMap<String, ArrayList> map;


    public Dataframe(String[] dataArray) {
        map = new LinkedHashMap<>();
        int columnNumber = 0;
        ArrayList<String> names = new ArrayList<>();

        for (int col = 0; col <= columnNumber; col++){

            String[] array = dataArray[col].split(",");
            //Delete spaces from column names
            for (int strnum = 0; strnum < array.length; strnum++) {
                array[strnum] = array[strnum].replaceAll(" ", "");
            }

            //Strip out the brackets from the elements to insert
            array[0] = array[0].replaceFirst("\\{", "");
            array[array.length - 1] = array[array.length - 1].replaceFirst("}", "");

//            if (array[0].isEmpty() || array[array.length - 1].isEmpty()) {
//                throw new IllegalArgumentException("Illegal syntax, column is empty");
//            }

            //make an ArrayList for each named column and count them
            if(col == 0){
                for (String str : array) {
                    map.put(str, new ArrayList());
                    names.add(columnNumber,str);
                    columnNumber++;
                }
            }
            else {
                //TODO:Add type inference in order to make lists of the correct type
                ArrayList currentList = map.get(names.get(col- 1));
                if (!Collections.addAll(currentList, array)) {
                    throw new RuntimeException("Could not add elements to respective column");
                }

            }
        }
    }

    public ArrayList getColumn(String name){
        return map.get(name);
    }
    public int getNumberOfColumns(){
        return map.values().size();
    }
    //Print dataframe lines from start(inclusive) to end(not inclusive)
    public void printDataframeLines(int start, int end){
        //Check arguments for inconsistencies
        if (start > end || start < 1){
            throw new IllegalArgumentException("The requested lines cannot be printed, bad argument!");
        }

        //Print column names
        map.forEach((s, arrayList) -> {
            System.out.print(" |\t" + s);

        });

        //Add the last separator and change line
        System.out.println("\t|");
        for(int i = start; i < end;i++){
            //Print line number
            System.out.print(i + "|");

            //Print line elements
            int line = i-1;
            map.forEach((s, arrayList) -> {
                if (line < arrayList.size()) {
                    System.out.print("\t" + arrayList.get(line)+"\t|");
                }
                else System.out.print("\t\t|");
            });
            System.out.println();
        }
    }



}

