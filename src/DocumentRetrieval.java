import java.io.File;

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

        for (String query : Utils.readLines(new File (args[1]))){
            System.out.println("######################################");
            System.out.println("Query: " + query);
            System.out.println("----NonCaseSensitiveIndex----");
            Utils.printList(caseInsensitiveIndex.runQuery(query));
            System.out.println("----CaseSensitiveIndex----");
            Utils.printList(caseSensitiveIndex.runQuery(query));
        }
        /********  YOUR CODE FOR PART 2 STARTS HERE ********/


        /********  YOUR CODE FOR PART 2 ENDS HERE ********/
    }
}
