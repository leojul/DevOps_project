import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Dataframe {
    private LinkedHashMap<String, ArrayList> map;

    public Dataframe(){
        map = new LinkedHashMap<>();
    }

    public Dataframe(String[] dataArray) throws Exception {
        map = new LinkedHashMap<>();
        int columnNumber = 0;
        ArrayList<String> names = new ArrayList<>();

        for (int col = 0; col <= columnNumber; col++) {

            String[] array = dataArray[col].split(",");
            //Delete spaces from column names
            for (int strnum = 0; strnum < array.length; strnum++) {
                array[strnum] = array[strnum].replaceAll(" ", "");
            }

            //Strip out the brackets from the elements to insert
            array[0] = array[0].replaceFirst("\\{", "");
            array[array.length - 1] = array[array.length - 1].replaceFirst("}", "");


            //make an ArrayList for each named column and count them
            if (col == 0) {
                for (String str : array) {
                    map.put(str, new ArrayList());
                    names.add(columnNumber, str);
                    columnNumber++;
                }
            } else {
                //Create an ArrayList, check the types, and insert into map
                ArrayList currentList = new ArrayList();
                if (!Collections.addAll(currentList, array)) {
                    throw new Exception("Could not add elements to Arraylist");
                }
                checkArraylistTypeAndInsert(names.get(col- 1), currentList);
            }
        }
    }

    public Dataframe(String filename) throws Exception {
        map = new LinkedHashMap<>();
        URL url = getClass().getClassLoader().getResource(filename);
        File csv = new File(url.getPath());
        BufferedReader br = new BufferedReader(new FileReader(csv));
        String line = "";
        while ((line = br.readLine()) != null) {
            line.replaceAll(" ", "");
            String[] col = line.split(",");
            String label = col[0];

            //We create an arraylist of the elements and check the types before inserting in the map
            ArrayList al = new ArrayList();
            if (!Collections.addAll(al, col)) {
                throw new Exception("Could not add elements to Arraylist");
            }
            al.remove(0);
            checkArraylistTypeAndInsert(label, al);

        }
    }

    //Checks if the elements are integers or doubles, if an element not in either set is found, throw exception.
    public void checkArraylistTypeAndInsert(String label, ArrayList al) throws Exception {

        boolean isIntArray = true;

        for (Object element : al) {
            Scanner scanner = new Scanner((String) element);
            if (!scanner.hasNextInt()) {
                isIntArray = false;
                if (!scanner.hasNextDouble()) {
                    throw new Exception("Only numbers are accepted in columns, and columns can't be empty");
                }
            }
        }
        ArrayList listToInsert;
        if (isIntArray){
            listToInsert = new ArrayList<Integer>();
            al.forEach(o -> listToInsert.add(Integer.parseInt((String) o)));
        }
        else{
            listToInsert = new ArrayList<Double>();
            al.forEach(o -> listToInsert.add(Double.parseDouble((String) o)));
        }
        map.put(label, listToInsert);
    }

    public ArrayList getColumn(String label) {
        return map.get(label);
    }

    public int getNumberOfColumns() {
        return map.values().size();
    }

    //Print dataframe lines from start(inclusive) to end(not inclusive)
    public void printDataframeLines(int start, int end) {
        //Check arguments for inconsistencies
        if (start > end || start < 1 || end > getNumberOfLines()+1) {
            throw new IllegalArgumentException("The requested lines cannot be printed, bad argument!");
        }

        //Print column names
        map.forEach((s, arrayList) -> {
            System.out.print(" |\t" + s);

        });

        //Add the last separator and change line
        System.out.println("\t|");
        for (int i = start; i < end; i++) {
            //Print line number
            System.out.print(i + "|");

            //Print line elements
            int line = i - 1;
            map.forEach((s, arrayList) -> {
                if (line < arrayList.size()) {
                    System.out.print("\t" + arrayList.get(line) + "\t|");
                } else System.out.print("\t\t|");
            });
            System.out.println();
        }
    }

    //Returns the number of lines in the dataframe, which is effectively the number of elements in its biggest column
    public int getNumberOfLines(){
        AtomicInteger max = new AtomicInteger();
        max.set(0);
        map.forEach((s, arrayList) -> {
            if(arrayList.size() > max.get()){
                max.set(arrayList.size());
            }
        });
        return max.get();
    }

    public void printFirstLines(int numLines){
        printDataframeLines(1, numLines+1);
    }

    public void printLastLines(int numLines){
        printDataframeLines(getNumberOfLines()-numLines+1,getNumberOfLines()+1);
    }

    public void printDataframe(){
        printDataframeLines(1, getNumberOfLines()+1);
    }

    //Create a new dataframe from the lines of the current one, from start(inclusive) to end(not inclusive)
    public Dataframe newDataframeFromLines(int start, int end){

        if (start > end || start < 1 || end > getNumberOfLines()+1) {
            throw new IllegalArgumentException("The requested lines cannot be printed, bad argument!");
        }

        Dataframe newDataframe = new Dataframe();

        for (Map.Entry<String, ArrayList> entry : map.entrySet()) {
            ArrayList al = new ArrayList();
            //Adjustment to have lines start from 1
            for (int i = start - 1; i < end-1; i++){
                if (i < entry.getValue().size()) {
                    al.add(entry.getValue().get(i));
                }
            }
            newDataframe.addColumn(entry.getKey(), al);
        }
        return newDataframe;
    }

    //Create a new dataframe from the columns whose labels are entered in the argument
    public Dataframe newDataframeFromColumns(String[] labels){
        Dataframe newDataframe = new Dataframe();
        for (int i = 0; i < labels.length; i++) {
            if(map.containsKey(labels[i])){
                newDataframe.addColumn(labels[i],map.get(labels[i]));
            }
        }
        return newDataframe;
    }

    public void addColumn(String label, ArrayList al){
        map.put(label, al);
    }

    public double columnMeanValue(String label){
        ArrayList col = map.get(label);
        double sum  = 0;
        for (int i = 0; i < col.size();i++) {
            sum = sum + Double.valueOf(col.get(i).toString());
        }
        return sum/col.size();
    }

    public double columnMinimumValue(String label) {
        ArrayList col = map.get(label);
        double min;

        if (!col.isEmpty()) {
            min = Double.valueOf(col.get(0).toString());

            for (int i = 0; i < col.size(); i++) {
                if (min > Double.valueOf(col.get(i).toString())) {
                    min = Double.valueOf(col.get(i).toString());
                }
            }
        }
        else throw new RuntimeException("The column is empty");
        return min;
    }

    public double columnMaximumValue(String label) {
        ArrayList col = map.get(label);
        double max;

        if (!col.isEmpty()) {
            max = Double.valueOf(col.get(0).toString());

            for (int i = 0; i < col.size(); i++) {
                if (max < Double.valueOf(col.get(i).toString())) {
                    max = Double.valueOf(col.get(i).toString());
                }
            }
        }
        else throw new RuntimeException("The column is empty");
        return max;
    }




}

