import java.io.File;
import java.util.TreeSet;

public interface Index {

    // TODO
    void buildInvertedIndex(File[] files);

    // TODO
    TreeSet<String> runQuery(String query);

}
