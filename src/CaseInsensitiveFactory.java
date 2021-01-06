
public class CaseInsensitiveFactory extends AbstractInvertedIndexFactory{

    @Override
    // single tone pattern
    public CaseInsensitiveIndex createInvertedIndex(){
        CaseInsensitiveIndex index;
        if (CaseInsensitiveIndex.caseInsensitiveIndex == null){
            CaseInsensitiveIndex caseInsensitiveIndex =
                    new CaseInsensitiveIndex();
            System.out.println("New CaseInsensitive index is created");
            index = caseInsensitiveIndex;
        }else {
            System.out.println("You already have CaseInsensitive index");
            index = CaseInsensitiveIndex.caseInsensitiveIndex;
        }
        return index;
    }
}
