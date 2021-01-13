import java.io.File;
import java.util.TreeSet;

public class CaseInsensitiveIndex extends AbstractInvertedIndex{
    protected static CaseInsensitiveIndex caseInsensitiveIndex;

    public CaseInsensitiveIndex(){}
    // TODO this should be private. its a single tone!

    @Override
    public String handleCase(String key){
        return key.toLowerCase();
    }

}
