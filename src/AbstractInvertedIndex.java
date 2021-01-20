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
        TreeSet<String> result = new TreeSet<>();
        Stack<ArrayList<String>> toolStack = new Stack<>();
        StringBuffer editSubString = new StringBuffer();
        editSubString.append(query);

        String subQuery;
        if(query.contains("(")) {
            if ((editSubString.lastIndexOf(")") != editSubString.length() - 1 && editSubString.charAt(0) == '(')) {
                while (query.contains("(")) {
                    editSubString.delete(0, 1);
                    editSubString.delete(editSubString.lastIndexOf(")"), editSubString.length());
                    subQuery = editSubString.toString();
                    toolStack.push(parenthesesHandler(subQuery));
                    StringBuffer editString = new StringBuffer();
                    editString.append(query);
                    editString.delete(editString.indexOf("("), editString.lastIndexOf(")") + 2);
                    query = editString.toString();
                    if (editString.charAt(0) == ')')
                        while (query.contains(")")) {
                            editString.delete(0, 1);
                            query = editString.toString();
                        }
                }
            } else {
                while (query.contains("(")) {
                    StringBuffer tempString = new StringBuffer();
                    tempString.append(editSubString.substring(editSubString.indexOf("("),editSubString.indexOf(")")));
                    subQuery = tempString.toString();
                    toolStack.push(parenthesesHandler(subQuery));
                    editSubString.delete(editSubString.indexOf("("), editSubString.indexOf(")")+1);
                    query = editSubString.toString();
                }
            }
        }
        String[] orderQuery = Utils.splitBySpace(query);
        boolean isOperator = false, usedWord = false;
        ArrayList<String> operators = new ArrayList<>();
        operators.add("AND");
        operators.add("NOT");
        operators.add("OR");
        for (int i = 0; i < orderQuery.length; i++) {
            if (orderQuery[i].equals("")) {
                continue;
            }
            if (operators.contains(orderQuery[i])) {
                isOperator = true;
            }
            if (operators.contains(orderQuery[i]) && i < (orderQuery.length - 1)) {
                if (operators.contains(orderQuery[i + 1])) {
                    continue;
                }
                toolStack.push(keyListMap.get(handleCase(orderQuery[i + 1])));
                usedWord = true;

            }
            if (!isOperator) {
                if (usedWord) {
                    usedWord = false;
                }
                else //
                    toolStack.push(keyListMap.get(handleCase(orderQuery[i])));

            } else {
                handleOperator(toolStack, orderQuery[i]);
            }

        }
        result.addAll(toolStack.pop());
        return result;
    }


    protected ArrayList<String> parenthesesHandler(String query) {
        Stack<ArrayList<String>> toolStack = new Stack<>();
        if (query.contains("(")) {
            String closedQuery = query + ")";
            String subQuery = Utils.substringBetween(closedQuery, "(", ")");
            toolStack.push(parenthesesHandler(subQuery));
            query = query.replace("(", "");
            query = query.replace(")", "");
            query = query.replaceAll(subQuery, "");

        }
        String[] orderQuery = Utils.splitBySpace(query);
        for (int i = 0; i < orderQuery.length; i++) {
            if (isOperator(orderQuery[i]) && i < (orderQuery.length - 1)) {
                if (isOperator(orderQuery[i + 1])) {
                    continue;
                }
                toolStack.push(keyListMap.get(handleCase(orderQuery[i + 1])));
            }
            if (!isOperator(orderQuery[i])) {
                toolStack.push(keyListMap.get(handleCase(orderQuery[i])));
            } else {
                handleOperator(toolStack, orderQuery[i]);
                break;
            }
        }
        return new ArrayList<>(toolStack.pop());
    }

    public void handleOperator(Stack<ArrayList<String>> stack, String string){
        System.out.println("handleOperator!!!");
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
        SortedSet<String> cutKeySet = new TreeSet<>();
        Set<String> sensitive = caseSensitiveIndex.keyListMap.keySet();
        Set<String> inSensitive = caseInsensitiveIndex.keyListMap.keySet();

        Map<String,ArrayList<String>> keyListMap = caseSensitiveIndex.keyListMap;

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
        SortedMap<String,ArrayList<String>> cutMap = new TreeMap<>();
        for (String key : keys){
            cutMap.put(key, keyListMap.get(key));
        }

        return cutMap;
    }

}
