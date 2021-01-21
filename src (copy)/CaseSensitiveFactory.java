
public class CaseSensitiveFactory extends AbstractInvertedIndexFactory{
    private static CaseSensitiveIndex caseSensitiveIndex = null;

    @Override
    // single tone pattern
    public AbstractInvertedIndex createInvertedIndex(){
        if (caseSensitiveIndex == null) {
            caseSensitiveIndex = new CaseSensitiveIndex();
            System.out.println("New CaseSensitive index is created");
        } else {
            System.out.println("You already have CaseSensitive index");
        }
        return caseSensitiveIndex;
    }
}
