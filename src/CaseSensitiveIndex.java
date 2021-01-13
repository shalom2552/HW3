import java.io.File;
import java.util.TreeSet;

public class CaseSensitiveIndex extends AbstractInvertedIndex{
    protected static CaseSensitiveIndex caseSensitiveIndex;
    protected CaseSensitiveIndex(){}

    @Override
    public String handleCase(String key){
        return key.toLowerCase();
    }
}
