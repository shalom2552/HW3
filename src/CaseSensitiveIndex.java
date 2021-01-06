import java.io.File;
import java.util.TreeSet;

public class CaseSensitiveIndex extends AbstractInvertedIndex{
    protected static CaseSensitiveIndex caseSensitiveIndex;
    protected CaseSensitiveIndex(){}


    @Override
    protected boolean compare(String key, String word){
        key = key.toLowerCase();
        word = word.toLowerCase();
        return key.equals(word);
    }
}
