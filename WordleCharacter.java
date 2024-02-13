import java.util.*;
public class WordleCharacter {
    private char character;
    private int puzzleWordCharCount;
    private List<Integer> locations;
    public WordleCharacter(char character){
        this.character = character;
        locations = new ArrayList<>();
        puzzleWordCharCount = 0;
    }

    public void addCharacter(int pos){
        puzzleWordCharCount++;
        locations.add(pos);
    }

    public void removeCharacter(int pos){
        // removes the pos integer from the arrayList if it finds it.
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i) == pos) {
                puzzleWordCharCount--;
                locations.remove(i);
                break;
            }
        }
    }

    public int getPuzzleWordCharCount(){
        return puzzleWordCharCount;
    }

    public char getCharacter(){
        return character;
    }

    public int getFirstPos(){
        return locations.get(0);
    }
}
