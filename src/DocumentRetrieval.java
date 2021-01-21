import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * The main user class.
 */
public class DocumentRetrieval {

    /**
     * Pass two arguments: 1. The path of the directory of documents, 2. The path of the boolean query file
     * @param args
     */
    public static void main(String[] args) {

        AbstractInvertedIndexFactory caseInsensitiveFactory = new CaseInsensitiveFactory();
        AbstractInvertedIndexFactory caseSensitiveFactory = new CaseSensitiveFactory();
        AbstractInvertedIndex caseInsensitiveIndex = caseInsensitiveFactory.createInvertedIndex();
        AbstractInvertedIndex caseInsensitiveIndexTwo = caseInsensitiveFactory.createInvertedIndex();
        AbstractInvertedIndex caseSensitiveIndex = caseSensitiveFactory.createInvertedIndex();
        AbstractInvertedIndex caseSensitiveIndexTwo = caseSensitiveFactory.createInvertedIndex();


        caseInsensitiveIndex.buildInvertedIndex( (new File(args[0])).listFiles());
        caseSensitiveIndex.buildInvertedIndex( (new File(args[0])).listFiles());

//        /********  FOR PROGRAM ARGUMENTS ********/
//        caseInsensitiveIndex.buildInvertedIndex( (new File("/home/marshall" +
//                "/Documents/HW3/AP_Coll_Parsed").listFiles()));
//        caseSensitiveIndex.buildInvertedIndex( (new File("/home/marshall/Documents/HW3/AP_Coll_Parsed")).listFiles());
//
//        /********  FOR PROGRAM ARGUMENTS ********/

        for (String query : Utils.readLines(new File (args[1]))){
//        for (String query : Utils.readLines(new File ("/home/marshall" +
//                "/Documents/HW3/BooleanQueries.txt"))){ // TODO program-args


            System.out.println("######################################");
            System.out.println("Query: " + query);
            System.out.println("----NonCaseSensitiveIndex----");
            Utils.printList(caseInsensitiveIndex.runQuery(query));
            System.out.println("----CaseSensitiveIndex----");
            Utils.printList(caseSensitiveIndex.runQuery(query));
        }
        /********  YOUR CODE FOR PART 2 STARTS HERE ********/

        try {
            File file = new File("IntersectionsOutput.txt");
            if (!file.createNewFile()) System.out.println("file already exist");

            FileWriter writer = new FileWriter("IntersectionsOutput.txt");
            writer.write("######################################\n");
            writer.write("----Intersections----\n");



            SortedMap<String, ArrayList<String>> intersection =
                    AbstractInvertedIndex.intersectionMap(
                            (CaseSensitiveIndex) caseSensitiveIndex,
                            (CaseInsensitiveIndex) caseInsensitiveIndex);


            for (SortedMap.Entry<String, ArrayList<String>> entry :
                    intersection.entrySet()) {
                writer.write(entry.getKey() + " : " + entry.getValue().toString()+ "\n");
            }

            writer.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


        /********  YOUR CODE FOR PART 2 ENDS HERE ********/
    }
}
