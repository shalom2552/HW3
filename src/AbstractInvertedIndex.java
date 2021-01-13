import java.io.File;
import java.util.*;

public abstract class AbstractInvertedIndex implements Index{
    protected Map<String,Set<String>> keyListMap;

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
        Stack<Set<String>> stack = new Stack<>();

        for (String word : Utils.splitBySpace(query)){
            if (isOperator(word)){ // TODO operator includes brackets?
                stack = updateStack(word, stack);
                // TODO above line does not handle brackets cases!!
            }
            else {
                word = handleCase(word);
                Set<String> filesName = keyListMap.get(word);
                stack.push(filesName);
            }
        }
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

    // this is for caseSensitiv override
    // returns the lower case word if its a caseSensitive
    public String handleCase(String key){
        return key;
    }

    protected Stack<Set<String>> updateStack(
            String query, Stack<Set<String>> stack){

        Set<String> set1 = stack.pop();
        Set<String> set2 = stack.pop();
        switch (query){
            case "OR":
                set1.addAll(set2);
                stack.push(set1);
            case "AND":
                set1.retainAll(set2);
                stack.push(set1);
            case "NOT":
                set2.removeAll(set1);
                stack.push(set2);
            default:
                System.out.println(" is possible? ");
                System.exit(0);
        }
        return stack;
    }

    // return true if the word is a key
    protected boolean isOperator(String word){
        return (!word.equals("NOT") && !word.equals("OR") &&
                !word.equals("AND") && !word.equals("(") && !word.equals(")"));
    }

}
