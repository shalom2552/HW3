import java.io.File;
import java.util.*;

public abstract class AbstractInvertedIndex {
    protected List<Map<String,HashSet<String>>> wordListMap;
    protected List<File> dataBase;

    // this is called from main
    public void buildInvertedIndex(File[] files) {
        dataBase.addAll(Arrays.asList(files));
    }



    // this is called from main
    // TODO this function is in construction
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

        // return files List
        return null;
    }

    protected void updateKeys(ArrayList<String> keys){
        for (String key : keys){
            Map<String,HashSet<String>> wordMap = new HashMap<>();
            wordMap.put(key,filesContained(key));
            wordListMap.add(wordMap);
        }
    }

    // returns the keys that never been queries
    protected ArrayList<String> checkKeys(ArrayList<String> keys){
        ArrayList<String> uncheckedKeys = new ArrayList<>();
        for ( String key : keys) {
            for (Map<String,HashSet<String>> map : wordListMap){
                if (map.containsKey(key)){
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
        } return filesContained;
    }


    // TODO substring between?
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

    // this is for caseSensitive override
    protected boolean compare(String key, String word){
        return key.equals(word);
    }

    // TODO should be in utils?
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

    // TODO should be in utils?
    // TODO if results needs to be sorted, then we will build merge sort
    protected HashSet<String> unionBetweenGroups(
            HashSet<String> list1, HashSet<String> list2){
        HashSet<String> results = new HashSet<>(list1);
        results.addAll(list2);
        return results;
    }

}
