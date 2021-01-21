import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.TreeSet;

public class Utils {

    /**
     * Read all the file lines into a list of string
     * @param file - An example of file creation: new File ("path to file on disk")
     * @return - The lines of the file as list
     */
    public static List<String> readLines (File file) {
        List<String> fileLines = null;
        try {
            fileLines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Error reading file: " + file.getAbsolutePath());
            e.printStackTrace();
            System.exit(1);
        }
        return fileLines;
    }

    /**
     * Split text by space and placed into an array of strings
     * exp: splitBySpace("a b c") -> [a,b,c]
     * @param row
     * @return
     */
    public static String[] splitBySpace (String row) { return row.split("\\s+");}

    /**
     * Gets the String that is nested in between two Strings. Only the first match is returned.
     * A null input String returns null. A null open/close returns null (no match). An empty ("") open and close returns an empty string.
     * @param str - the String containing the substring, may be null
     * @param open - the String before the substring, may be null
     * @param close - the String after the substring, may be null
     * @return the substring, null if no match
     * substringBetween("yabcz", "y", "z")   = "abc"
     */
    public static String substringBetween(String str, String open, String close) {
        if (str != null && open != null && close != null) {
            int start = str.indexOf(open);
            if (start != -1) {
                int end = str.indexOf(close, start + open.length());
                if (end != -1) {
                    return str.substring(start + open.length(), end);
                }
            }
            return null;
        } else {
            return null;
        }
    }

    /**
     * Print the retrieved results sorted in an Alphabetical Order
     * @param docRetrievedList - A sorted set of retrieved document names
     */
    public static void printList (TreeSet<String> docRetrievedList) {
        for (String doc : docRetrievedList) {
            System.out.print(doc + " ");
        }
        System.out.println();
    }


}
