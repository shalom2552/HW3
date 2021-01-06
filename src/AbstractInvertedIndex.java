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
        // parse query and get only keys at first
        // make keys list, only those ho never checked
        // TODO use stack to save keys
        String[] keys = {"make", "this"};

        // save results for the new keys
        for (String key : keys){
            Map<String,HashSet<String>> wordMap = new HashMap<>();
            wordMap.put(key,filesContained(key));
            wordListMap.add(wordMap);
        }
        // parse query and collect all files witch need

        // return files List
        return null;
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
