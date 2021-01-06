import java.io.File;
import java.util.TreeSet;

public  interface AbstractInvertedIndex {

    public void buildInvertedIndex(File[] files);

    public TreeSet<String> runQuery(String query);

}
