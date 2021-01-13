
public class CaseInsensitiveFactory extends AbstractInvertedIndexFactory{
    private static CaseInsensitiveIndex caseInsensitiveIndex = null;

    @Override
    // single tone pattern
    public AbstractInvertedIndex createInvertedIndex(){
        if (caseInsensitiveIndex == null) {
            caseInsensitiveIndex = new CaseInsensitiveIndex();
            System.out.println("New CaseInsensitive index is created");
        } else {
            System.out.println("You already have CaseInsensitive index");
        }
        return caseInsensitiveIndex;
    }
}
