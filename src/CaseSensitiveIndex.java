import java.io.File;
import java.util.TreeSet;

public class CaseSensitiveIndex implements AbstractInvertedIndex{
    @Override
    public void buildInvertedIndex(File[] files) {

    }

    @Override
    public TreeSet<String> runQuery(String query) {
        return null;
    }
}
