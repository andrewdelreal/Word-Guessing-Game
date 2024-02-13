import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WordleDictionary {
    private static WordleDictionary instance = null;
    private Random random;
    private ArrayList<String> dailyWords;
    private ArrayList<String> guessWords;
    private Map<Integer, Integer> usedLocations;
    private WordleDictionary(){
        dailyWords = new ArrayList<>();
        guessWords = new ArrayList<>();
        random = new Random();
        usedLocations = new HashMap<>();
    }

    public static WordleDictionary getInstance(){
        if (instance == null) {
            instance = new WordleDictionary();
        }
        return instance;
    }

    // only daily words can be used as the puzzle word
    public void loadDailyWords(String pathToDictionary, int numOfCharacters) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(pathToDictionary));
        String s = fileReader.readLine();
        // read each word line by line until there are no words left
        while(s != null){
            if (s.length() == numOfCharacters){
                dailyWords.add(s);
            }
            s = fileReader.readLine();
        }
    }

    // Guess words are in a different file because guessWords can never be daily words
    public void loadGuessWords(String pathToGuessWords, int numOfCharacters) throws IOException{
        BufferedReader fileReader = new BufferedReader(new FileReader(pathToGuessWords));
        String s = fileReader.readLine();
        // read each word line by line until there are no words left
        while(s != null){
            if (s.length() == numOfCharacters){
                guessWords.add(s);
            }
            s = fileReader.readLine();
        }
    }

    public String getRandomDailyWord(){
        String retval;
        int pos = random.nextInt(dailyWords.size());
        // while the word in the daily words array is found in the hash map, keep randomizing
        while (usedLocations.containsKey(pos)) {
            pos = random.nextInt(dailyWords.size());
        }
        // Hash map is used to prevent two puzzles from getting the same word
        usedLocations.put(pos, pos);
        retval = dailyWords.get(pos);
        return retval;
    }

    // The files I have chosen are sorted alphabetically
    public boolean searchDailyWords(String s){
        boolean retval = false;
        int left = 0;
        int right = dailyWords.size() - 1;

        while (left <= right) {

            // Calculating middle
            int m = left + (right - left) / 2;

            int result = s.compareTo(dailyWords.get(m));

            // Check if x is present at mid
            if (result == 0) {
                retval = true;
                break;
            }
            // If x greater, ignore left half
            if (result > 0) {
                left = m + 1;
            }
                // If x is smaller, ignore right half
            else {
                right = m - 1;
            }
        }

        return retval;
    }

    // The files I have chosen are sorted alphabetically
    public boolean searchGuessWords(String s){
        boolean retval = false;
        int left = 0;
        int right = guessWords.size() - 1;

        while (left <= right) {

            // Calculatiing mid
            int m = left + (right - left) / 2;

            int result = s.compareTo(guessWords.get(m));

            // Check if x is present at mid
            if (result == 0) {
                retval = true;
                break;
            }
            // If x greater, ignore left half
            if (result > 0) {
                left = m + 1;
            }
            // If x is smaller, ignore right half
            else {
                right = m - 1;
            }
        }

        return retval;
    }
}
