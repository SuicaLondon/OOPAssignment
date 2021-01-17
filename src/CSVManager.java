import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVManager {
    public static ArrayList<String[]> readCSV(String path) throws FileNotFoundException {
        BufferedReader csvReader = new BufferedReader(new FileReader(path));
        String line = null;
        try {
            String row;
            String[] rowList;
            ArrayList<String[]> csvArrayList = new ArrayList<>();
            while ((row = csvReader.readLine()) != null) {

                String str;
                row += ",";
                Pattern pattern = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
                Matcher matcher = pattern.matcher(row);
                List rowData = new LinkedList();
                while (matcher.find()) {
                    str = matcher.group();
                    // proof the situation that String include ","
                    str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
                    str = str.replaceAll("(?sm)(\"(\"))", "$2");
                    rowData.add(str);
                }
                int length = rowData.size();
                rowList = new String[length];
                for (int i = 0; i < rowData.size(); i++) {
                    rowList[i] = rowData.get(i) == null ? "" : rowData.get(i).toString();
                }
                csvArrayList.add(rowList);
            }
            return csvArrayList;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean writeCSV(ArrayList<String> dataList, String fileName) {
        boolean isSuccess = false;
        File file = null;
        file = new File(fileName);
        FileOutputStream out = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            if (dataList != null && !dataList.isEmpty()) {
                for (String data : dataList) {
                    bw.append(data).append("\r");
                }
            }
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                    bw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (osw != null) {
                try {
                    osw.close();
                    osw = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }
}
