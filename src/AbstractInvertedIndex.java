import java.io.File;
import java.util.*;


public abstract class AbstractInvertedIndex implements Index{
    protected Map<String,ArrayList<String>> keyListMap = new HashMap<>();
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

    public TreeSet<String> runQuery(String query){
        Stack<ArrayList<String>> stack = new Stack<>();
        StringBuilder subString = new StringBuilder();
        subString.append(query);
        String subQuery;
        // handle brackets
        if(query.contains("(")) {
            if ((subString.lastIndexOf(")") != subString.length() - 1 && subString.charAt(0) == '(')) {
                while (query.contains("(")) {
                    subString.delete(0, 1);
                    subString.delete(subString.lastIndexOf(")"), subString.length());
                    subQuery = subString.toString();

                    stack.push(parenthesesHandler(subQuery));

                    query = clearQuery(query, 2);
                }
            } else {
                while (query.contains("(")) {
                    subQuery = Utils.substringBetween(subString.toString(), "(", ")");
                    stack.push(parenthesesHandler(subQuery));
                    subString.delete(subString.indexOf("("),
                            subString.indexOf(")")+2);
                    query = subString.toString();
                }
            }
        }
        String[] queryList = Utils.splitBySpace(query);
        if (queryList.length == 1 && isOperator(queryList[0])){
            handleOperator(stack, queryList[0]);
            return new TreeSet<>(stack.pop());
        }
        boolean usedWord = false;
        for (int i = 0; i < queryList.length; i++) {
            if (isOperator(queryList[i])) {
                stack.push(keyListMap.get(handleCase(queryList[i + 1])));
                usedWord = true;
            }
            if (!isOperator(queryList[i])) {
                if (usedWord) {
                    usedWord = false;
                }
                else //
                    stack.push(keyListMap.get(handleCase(queryList[i])));
            } else {
                handleOperator(stack, queryList[i]);
            }
        }
        return new TreeSet<>(stack.pop());
    }


    public String clearQuery(String query, int count){
        StringBuilder editString = new StringBuilder();
        editString.append(query);
        editString.delete(editString.indexOf("("),
                editString.lastIndexOf(")") + count);
        return editString.toString();
    }


    public void handleOperator(Stack<ArrayList<String>> stack,
                          String operator) {
        ArrayList<String> set2;
        ArrayList<String> set1;
        switch (operator) {
            case "AND" : {
                set2 = stack.pop();
                set1 = stack.pop();
                set2.retainAll(set1);
                stack.push(set2);
                break;
            }
            case "NOT" : {
                set2 = stack.pop();
                set1 = stack.pop();
                set1.removeAll(set2);
                stack.push(set1);
                break;
            }
            case "OR" : {
                set2 = stack.pop();
                set1 = stack.pop();
                set2.addAll(set1);
                stack.push(set2);
                break;
            }
        }
    }

    protected ArrayList<String> parenthesesHandler(String query) {
        Stack<ArrayList<String>> stack = new Stack<>();
        if (query.contains("(")) {
            String closedQuery = query + ")";
            String subQuery = Utils.substringBetween(closedQuery, "(", ")");
            stack.push(parenthesesHandler(subQuery));
            query = query.replace("(", "");
            query = query.replace(")", "");
            query = query.replaceAll(subQuery, "");

        }
        String[] queryList = Utils.splitBySpace(query);
        for (int i = 0; i < queryList.length; i++) {
            if (isOperator(queryList[i]) && i < (queryList.length - 1)) {
                if (isOperator(queryList[i + 1])) {
                    continue;
                }
                stack.push(keyListMap.get(handleCase(queryList[i + 1])));
            }
            if (!isOperator(queryList[i])) {
                stack.push(keyListMap.get(handleCase(queryList[i])));
            } else {
                handleOperator(stack, queryList[i]);
                break;
            }
        }
        return new ArrayList<>(stack.pop());
    }

    // checks if the key has been checked before and if so then add it's file
    // name to it's compatible value list, else make a new key in the map.
    protected void updateKeys(String key, String fileName){
        key = handleCase(key); // return .toLowerCase if is caseSensitive
        // TODO is this abstract class knows about handleCase() @Override?
        if (keyListMap.containsKey(key)){
            keyListMap.get(key).add(fileName);
        }else {
            ArrayList<String> filesName = new ArrayList<>();
            filesName.add(fileName);
            keyListMap.put(key, filesName);
        }
    }


    // this is for caseSensitive override
    // returns the lower case word if its a caseSensitive
    public String handleCase(String key){
        return key;
    }


    // return true if the word is an operator
    protected boolean isOperator(String word){
        return (word.equals("NOT") || word.equals("OR") || word.equals("AND"));
    }

    // return sortedMap which contains the cutting between the tow index's
    // keys - only letters, contains at most in 4 files
    // value - the files that contain the key
    public static SortedMap<String, ArrayList<String>> intersectionMap(
            CaseSensitiveIndex caseSensitiveIndex,
            CaseInsensitiveIndex caseInsensitiveIndex
    ) {
        Set<String> sensitive = caseSensitiveIndex.keyListMap.keySet();
        Set<String> inSensitive = caseInsensitiveIndex.keyListMap.keySet();

        sensitive.retainAll(inSensitive);

        Map<String,ArrayList<String>> keyListMap = caseSensitiveIndex.keyListMap;


        SortedSet<String> cutKeySet = new TreeSet<>();
        for (String key : sensitive){
            if (keyListMap.get(key).size() <= 4 && !notANumber(key) ){
                cutKeySet.add(key);
            }
        }



        SortedSet<String> keys = new TreeSet<>(cutKeySet);
        SortedMap<String,ArrayList<String>> cutMap = new TreeMap<>();
        for (String key : keys){
            cutMap.put(key, keyListMap.get(key));
        }

        return cutMap;
    }

    public static boolean notANumber(String key) {
        for (int i = 0; i <= 9; i++) {
            if (key.contains(Integer.toString(i))) {
                return true;
            }
        }
        return false;
    }

}


