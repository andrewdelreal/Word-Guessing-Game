import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class WordlePuzzle extends JFrame implements Observer {
    private WordleGuess wordleGuess;
    private String puzzleWord;
    private int currentGuess;
    private boolean isSolved;
    private int guessSize;
    private List<List<JLabel>> grid;
    private JLabel supportText;
    private JPanel puzzlePanel;
    public WordlePuzzle(WordleGuess wordleGuess) {
        // attatching the puzzles to the WordleGuesser
        this.wordleGuess = wordleGuess;
        wordleGuess.attach(this);

        // only DailyWords can ever be the puzzle Word
        puzzleWord = WordleDictionary.getInstance().getRandomDailyWord();
        grid = new ArrayList<>(0);
        isSolved = false;
        currentGuess = 0;
        guessSize = 0;

        setLayout(new BorderLayout());

        // Size the JPanel based on the requested num of guesses and and word length
        puzzlePanel = new JPanel(new GridLayout(wordleGuess.NUM_OF_GUESSES, wordleGuess.WORD_LENGTH));
        supportText = new JLabel("");

        JLabel temp;
        ArrayList<JLabel> tempArray;
        // create the JLabel array and visuals
        for (int i = 0; i < wordleGuess.NUM_OF_GUESSES; i++) {
            tempArray = new ArrayList<>();
            for (int k = 0; k < wordleGuess.WORD_LENGTH; k++) {
                temp = new JLabel("", SwingConstants.CENTER);
                temp.setFont(new Font("Ariel", Font.BOLD, 40));
                // set opaque allows the backGround to change
                temp.setOpaque(true);
                Border border = new LineBorder(Color.BLACK);
                temp.setBorder(border);
                puzzlePanel.add(temp);
                tempArray.add(temp);
            }
            grid.add(tempArray);
        }

        add(puzzlePanel, BorderLayout.CENTER);
        add(supportText, BorderLayout.PAGE_END);

        setSize(65 * wordleGuess.WORD_LENGTH, 65 * wordleGuess.NUM_OF_GUESSES);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void update(KeyEvent e) {
        if (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z) {
            // update puzzle visual to match the new character sent in
            String temp = "" + e.getKeyChar();
            temp = temp.toUpperCase();
            grid.get(currentGuess).get(guessSize).setText(temp);
            guessSize++;
        }
        else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            String temp = "";
            // makes a lowercase string of the guess word
            for (int i = 0; i < wordleGuess.WORD_LENGTH; i++) {
                temp += grid.get(currentGuess).get(i).getText();
            }
            // updates the background colors
            updateCurrentGuessColors(temp.toLowerCase());
            // check if the puzzle is solved
            isSolved();
            if (isSolved) {
                // the puzzle no longer needs to listen to the observer
                wordleGuess.detach(this);
            }
            guessSize = 0;
            currentGuess++;
            // if the user fails to guess the word within the numer of guesses
            if (currentGuess == wordleGuess.NUM_OF_GUESSES && !isSolved) {
                //display support text.
                supportText.setText("The word was " + puzzleWord + ".");
            }
            // if they user ever solves the puzzle
            else if (isSolved) {
                // display support text
                supportText.setText("Great Job!");
            }
        }
        else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
            // remove the current character
            grid.get(currentGuess).get(guessSize - 1).setText("");
            guessSize--;
        }
    }

    private void updateCurrentGuessColors(String guessWord){
        char[] puzzle = puzzleWord.toCharArray();
        char[] guess = guessWord.toCharArray();

        List<WordleCharacter> wordleCharacters = new ArrayList<>(0);
        WordleCharacter temp;
        // creates wordle characters that hold the locations of where they are found
        for (int i = 0; i < wordleGuess.WORD_LENGTH; i++){
            // if the character already exists in the array
            boolean characterExists = false;
            for (int k = 0; k < wordleCharacters.size(); k++) {
                // if it already exist in the array, add the index location
                if (wordleCharacters.get(k).getCharacter() == puzzle[i]) {
                    characterExists = true;
                    wordleCharacters.get(k).addCharacter(i);
                    break;
                }
            }
            // if the character doesn't exist in the array, create a new character object with the location
            if (!characterExists) {
                temp = new WordleCharacter(puzzle[i]);
                wordleCharacters.add(temp);
                temp.addCharacter(i);
            }
        }

        // green characters
        for (int i = 0; i < wordleGuess.WORD_LENGTH; i++) {
            if (guess[i] == puzzle[i]) {
                // if the positions of the puzzleWord and guessWord are the same,
                // then the color must be green.
                grid.get(currentGuess).get(i).setBackground(Color.GREEN);
                for (int k = 0; k < wordleCharacters.size(); k++) {
                    // find the corresponding wordle character and remove its location in the array
                    wordleCharacters.get(k).removeCharacter(i);
                }
            }
        }

        // yellow characters
        for (int i = 0; i < wordleGuess.WORD_LENGTH; i++) {
            temp = null;
            // get the matching wordle character to the guess character
            for (int k = 0; k < wordleCharacters.size(); k++) {
                if (wordleCharacters.get(k).getCharacter() == guess[i]) {
                    temp = wordleCharacters.get(k);
                    break;
                }
            }
            // If a matching character was found
            if (temp != null) {
                // If there is a position remaining and the character isn't already green
                if (temp.getPuzzleWordCharCount() != 0 && !grid.get(currentGuess).get(i).getBackground().equals(Color.GREEN)) {
                    // get first position of the found character and set the background color yellow,
                    // because the character is found, but it is guaranteed to be in a different position.
                    int pos = temp.getFirstPos();
                    grid.get(currentGuess).get(i).setBackground(Color.YELLOW);
                    // remove the posion found
                    temp.removeCharacter(pos);
                }
            }
        }

        // white characters, do nothing
    }

    public void isSolved(){
        boolean retval = true;
        for (int i = 0; i < wordleGuess.WORD_LENGTH; i++) {
            // if the background color of the guess has any color other that green, then it isn't solved
            if (!(grid.get(currentGuess).get(i).getBackground().equals(Color.GREEN))){
                retval = false;
                break;
            }
        }
        isSolved = retval;
    }
}
