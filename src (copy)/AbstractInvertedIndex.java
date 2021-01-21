import java.io.File;
import java.util.*;


public abstract class AbstractInvertedIndex implements Index{
    protected Map<String,Set<String>> keyListMap = new HashMap<>();
    public boolean isSensitive;

    // this is called from main
    public void buildInvertedIndex(File[] files) {
        for (File file : files) {
            String fileName = "";
            boolean text = false;
            List<String> lines = Utils.readLines(file);
            for (String line : lines){
                if (line.contains("<DOCNO>")){
                    fileName =Utils.substringBetween(line,"<DOCNO>","</DOCNO>");
                    continue; }
                if (line.equals("<TEXT>")){
                    text = true;
                    continue; }
                if (line.equals("</TEXT>")){
                    text = false;
                    continue; }
                if (text){
                    String[] keys = Utils.splitBySpace(line);
                    for (String key : keys)
                        updateKeys(key, fileName);
                }
            }
        }
    }

    // called from main
    public TreeSet<String> runQuery(String query) {
        Stack<Set<String>> reversedStack = new Stack<>();
//        Stack<String> operatorStack = new Stack<>();
        Stack<Set<String>> keyStack = new Stack<>();

        for (String word : Utils.splitBySpace(query)){
            if (!isOperator(word)) {
                word = handleCase(word);
                Set<String> filesName = this.keyListMap.get(word);
                reversedStack.push(filesName);
            }
        }

        while (reversedStack.size() != 0){
            keyStack.push(reversedStack.pop());
        }

        keyStack = updateStack();
        return new TreeSet<>(stack.pop());
    }

    // checks if the key has been checked before and if so then add it's file
    // name to it's compatible value list, else make a new key in the map.
    protected void updateKeys(String key, String fileName){
        key = handleCase(key); // return .toLowerCase if is caseSensitive
        // TODO is this abstract class knows about handleCase() @Override?
        if (keyListMap.containsKey(key)){
            keyListMap.get(key).add(fileName);
        }else {
            Set<String> filesName = new HashSet<>();
            filesName.add(fileName);
            keyListMap.put(key, filesName);
        }
    }


    public Set<String> function(Set<String> set1, Set<String> set2, String query){
        switch (query) {
            case "OR" -> {
                set1.addAll(set2);
                return set1;
            }
            case "AND" -> {
                set1.retainAll(set2); // remove set1 elements that not in set 2
                return set1;
            }
            case "NOT" -> {
                set1.removeAll(set2);
                return set1;
            }
            default -> {
                System.out.println(" is possible? ");
                System.exit(0);
            }
        }
    }


    // this is for caseSensitive override
    // returns the lower case word if its a caseSensitive
    public String handleCase(String key){
        return key;
    }

    protected Stack<Set<String>> updateStack(
            String query, Stack<Set<String>> stack){

        // TODO this throws an emptyStackException at first time call
        if (stack.size() < 2){
            System.out.println("stack is smaller then 2' do something");// TODO
        }else {
            Set<String> set1 = stack.pop();
            Set<String> set2 = stack.pop();
            switch (query) {
                case "OR" -> {
                    set1.addAll(set2);
                    stack.push(set1);
                }
                case "AND" -> {
                    set1.retainAll(set2);
                    stack.push(set1);
                }
                case "NOT" -> {
                    set2.removeAll(set1);
                    stack.push(set2);
                }
                default -> {
                    System.out.println(" is possible? ");
                    System.exit(0);
                }
            }
        }
        return stack;
    }

    // return true if the word is an operator
    protected boolean isOperator(String word){
        return (word.equals("NOT") || word.equals("OR") ||
                word.equals("AND") || word.equals("(") || word.equals(")"));
    }

    // return sortedMap which contains the cutting between the tow index's
    // keys - only letters, contains at most in 4 files
    // value - the files that contain the key
    public static SortedMap<String, Set<String>> intersectionMap(
            CaseSensitiveIndex caseSensitiveIndex,
            CaseInsensitiveIndex caseInsensitiveIndex
    ) {
        SortedSet<String> cutKeySet = new TreeSet<>();
        Set<String> sensitive = caseSensitiveIndex.keyListMap.keySet();
        Set<String> inSensitive = caseInsensitiveIndex.keyListMap.keySet();

        Map<String,Set<String>> keyListMap = caseSensitiveIndex.keyListMap;

        for (String key1 : sensitive){
            for (String key2 : inSensitive){
                if (key1.equals(key2)){
                    if (keyListMap.get(key1).size() <= 4){
                        cutKeySet.add(key1);
                    }
                }

            }
        }

        SortedSet<String> keys = new TreeSet<>(cutKeySet);
        SortedMap<String,Set<String>> cutMap = new TreeMap<>();
        for (String key : keys){
            cutMap.put(key, keyListMap.get(key));
        }

        return cutMap;
    }

}
