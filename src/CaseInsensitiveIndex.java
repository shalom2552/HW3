import java.io.File;
import java.util.TreeSet;

public class CaseInsensitiveIndex extends AbstractInvertedIndex{
    protected static CaseInsensitiveIndex caseInsensitiveIndex;
    protected CaseInsensitiveIndex(){}

    @Override
    public String handleCase(String key){
        return key.toLowerCase();
    }

}
