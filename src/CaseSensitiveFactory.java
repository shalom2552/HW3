
public class CaseSensitiveFactory extends AbstractInvertedIndexFactory{

    @Override
    // single tone pattern
    public CaseSensitiveIndex createInvertedIndex(){
        CaseSensitiveIndex index;
        if (CaseSensitiveIndex.caseSensitiveIndex == null){
            CaseSensitiveIndex caseSensitiveIndex =
                    new CaseSensitiveIndex();
            System.out.println("New CaseSensitive index is created");
            index = caseSensitiveIndex;
        }else {
            System.out.println("You already have CaseSensitive index");
            index = CaseSensitiveIndex.caseSensitiveIndex;
        }
        return index;
    }
}
