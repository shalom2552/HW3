import java.io.File;
import java.util.*;

public abstract class AbstractInvertedIndex {
    protected List<Map<String,HashSet<String>>> keyListMap;
    protected List<File> dataBase;
//    <key, ["AP11", "SP22"]>,<key, ["AP11", "SP22"]>,<key, ["AP11", "SP22"]>

    // this is called from main
    public void buildInvertedIndex(File[] files) {
        dataBase.addAll(Arrays.asList(files));
    }

    // called from main
    public TreeSet<String> runQuery(String query) {
        // TODO use stack to save keys (or also operators?)
        // parse query and get only keys at first
        // make keys list, only those who never checked
        ArrayList<String> queryKeys = new ArrayList<>();
        for ( String word : Utils.splitBySpace(query)) {
            if (!isOperator(word)){
                queryKeys.add(word);
            }
        }

        // check if keys have checked before
        ArrayList<String> uncheckedKeys = checkKeys(queryKeys);

        // save results for the new keys
        updateKeys(uncheckedKeys);

        // parse query and collect all files witch need
        parseQuery(query);

        // return files List
        return null;
    }

    protected void updateKeys(ArrayList<String> keys){
        for (String key : keys){
            Map<String,HashSet<String>> wordMap = new HashMap<>();
            wordMap.put(key,filesContained(key));
            keyListMap.add(wordMap);
        }
    }

    // returns the keys that never been queries
    protected ArrayList<String> checkKeys(ArrayList<String> keys){
        ArrayList<String> uncheckedKeys = new ArrayList<>();
        for ( String key : keys) {
            for (Map<String,HashSet<String>> keyMap : keyListMap){
                if (!keyMap.containsKey(key)){
                    uncheckedKeys.add(key);
                }
            }
        }
        return uncheckedKeys;
    }

    protected void parseQuery(String query){
        // this if means - no brackets are in the query
        if (Utils.substringBetween(query, "(", ")")== null){
            String[] querySplit = Utils.splitBySpace(query);
            for (String word : querySplit){
                if (!isOperator(word)) {
                    // TODO keep going
                }
            }
        } else { // what to do if there is more then one brackets?

        }
    }

    // return true if the word is a key
    protected boolean isOperator(String word){
        return (!word.equals("NOT") && !word.equals("OR") &&
                !word.equals("AND") && !word.equals("(") && !word.equals(")"));
    }

    protected HashSet<String> filesContained(String key){
        HashSet<String> filesContained = new HashSet<>();
        for (File file : dataBase){
            String fileName = keyIsInFile(file, key);
            if (fileName != null){
                filesContained.add(fileName);
            }
        } return filesContained; // TODO can be null
    }

    // TODO check only what in <TEXT> (may be more then one)
    protected String keyIsInFile(File file, String key){
        List<String> fileLines = Utils.readLines(file);
        for (String row : fileLines){
            String[] words = Utils.splitBySpace(row);
            for (String word : words){
                if (compare(key, word)){
                    return file.getName();
                }
            }
        }
        return null;
    }

    // for caseSensitive override
    protected boolean compare(String key, String word){ return key.equals(word);
    }

    // TODO should be in util?
    protected HashSet<String> cuttingBetweenGroups(
            HashSet<String> list1, HashSet<String> list2){
        HashSet<String> results = new HashSet<>();
        for (String elem1 : list1){
            for (String elem2 : list2){
                if (elem1.equals(elem2)){
                    results.add(elem1);
                }
            }
        }
        return results;
    }

    // TODO should be in util?
    // TODO results needs to be sorted, (merge sort)
    protected HashSet<String> unionBetweenGroups(
            String[] list1, String[] list2){
        HashSet<String> results = new HashSet<>();

        int i=0, j=0;
        while (i < list1.length || j < list2.length) { // TODO check working
            if (i == list1.length){
                results.add(list2[j]);
                j++;
            } else if (j == list2.length){
                results.add(list1[i]);
                i++;
            }
            if (compareFilesName(list1[i], list2[j])) { // list1 < list2
                results.add(list1[i]);
                i++;
            }
            else {
                results.add(list2[j]);
                j++;
            }
        }
        return results;
    }

    // list1 subtract list2
    protected HashSet<String> subtractionBetweenGroups(
            HashSet<String> list1, HashSet<String> list2){
        for (String elem: list2) { list1.remove(elem); }
        return list1; // list1 subtract list2
    }

    // return true if first file name is smaller then second one
    protected boolean compareFilesName(String file1, String file2){
        String[] name1 = file1.substring(2).split("-");
        String[] name2 = file2.substring(2).split("-");
        if (Integer.parseInt(name1[0]) < Integer.parseInt(name2[0])){
            return true;
        } else if (Integer.parseInt(name1[0]) ==
                Integer.parseInt(name2[0])){
            return Integer.parseInt(name1[1]) <
                    Integer.parseInt(name2[1]);
        }else {
            return false;
        }
    }

}
