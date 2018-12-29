package lunar_lander;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String loadFileAsString(String path) {
        StringBuilder builder = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null)
                builder.append(line + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public static int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void saveRecords(List<Double> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/record/record.txt"))) {
            for (Double e : list) {
                bw.write(String.valueOf(e));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<Double> loadRecords() {
        try (BufferedReader br = new BufferedReader(new FileReader("resources/record/record.txt"))) {
            String line;
            ArrayList<Double> tmp=new ArrayList<>();
            while ((line = br.readLine()) != null) {
                tmp.add(Double.parseDouble(line));
            }
            return tmp;
        } catch (IOException e) {
            ArrayList<Double> recordList = new ArrayList<Double>();
            for (int i = 0; i < 10; i++)
                recordList.add(9999d);
            return recordList;
        }

    }
}
