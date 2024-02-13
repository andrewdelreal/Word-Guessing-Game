import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.Timer;

public class WordleGuess extends JFrame implements Subject{
    private List<Observer> observers;
    private int guessSize;
    private int currentGuess;
    private List<JLabel> guess;
    private JPanel guessPanel;
    private JLabel supportText;
    public final int WORD_LENGTH;
    public final int NUM_OF_GUESSES;
    public WordleGuess(int wordLength, int numOfGuesses){
        // Constants
        if (wordLength > 0) {
            WORD_LENGTH = wordLength;
        }
        else{
            WORD_LENGTH = 5;
        }

        if (numOfGuesses > 0) {
            NUM_OF_GUESSES = numOfGuesses;
        }
        else{
            NUM_OF_GUESSES = 6;
        }

        guessSize = 0;
        currentGuess = 0;

        guess = new ArrayList<>(0);
        observers = new ArrayList<>(0);
        guessPanel = new JPanel();
        supportText = new JLabel("");

        guessPanel.setLayout(new GridLayout(1, WORD_LENGTH));
        setLayout(new BorderLayout());

        // setting up the Wordle Guess GUI
        JLabel temp;
        for (int i = 0; i < WORD_LENGTH; i++){
            temp = new JLabel("", SwingConstants.CENTER);
            temp.setFont(new Font("Ariel", Font.BOLD, 40));
            Border border = new LineBorder(Color.BLACK);
            temp.setBorder(border);
            guess.add(temp);
            guessPanel.add(temp);
        }

        add(guessPanel, BorderLayout.CENTER);
        add(supportText, BorderLayout.PAGE_END);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // If all the puzzles are not solved and if there are guesses remaining
                if (observers.size() != 0 && currentGuess != NUM_OF_GUESSES) {
                    // The key pressed was a character key
                    if (e.getKeyCode() >= KeyEvent.VK_A && e.getKeyCode() <= KeyEvent.VK_Z) {
                        // Preventing adding a letter if the number of letter is the same as the word length
                        if (guessSize < WORD_LENGTH) {
                            // add the capitalized character to the guess panel
                            String temp = "" + e.getKeyChar();
                            temp = temp.toUpperCase();
                            guess.get(guessSize).setText(temp);
                            // notify puzzles
                            notifyObservers(e);
                            // change current character (guessSize)
                            guessSize++;
                        }
                    }
                    else if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        // Only consider the enter button if a whole word is entered
                        if (guessSize == WORD_LENGTH) {
                            String s = "";
                            for (int i = 0; i < guessSize; i++) {
                                s += guess.get(i).getText();
                            }
                            // If the guess is a recognized word
                            if (WordleDictionary.getInstance().searchDailyWords(s.toLowerCase()) || WordleDictionary.getInstance().searchGuessWords(s.toLowerCase())) {
                                currentGuess++;
                                // reset guess text
                                for (int i = 0; i < guessSize; i++) {
                                    guess.get(i).setText("");
                                }
                                // reset guess size
                                guessSize = 0;
                                // notify the puzzles
                                notifyObservers(e);
                            }
                            else {
                                // text underneath WordleGuess will display the help message
                                supportText.setText("Not a real word");
                                // used to get rid of the help text after an arbitrary amount of time
                                new Timer().schedule(
                                        new TimerTask() {
                                            @Override
                                            public void run() {
                                                supportText.setText("");
                                            }
                                        },
                                        1500
                                );
                            }
                        }
                    }
                    else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                        // backspace is only considered as long as the current guess isn't empty
                        if (guessSize != 0) {
                            guess.get(guessSize - 1).setText("");
                            guessSize--;
                            notifyObservers(e);
                        }
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        setSize(300, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(KeyEvent e) {
        // Notifying from the back is necessary due to the ability of the observers to detatch themselves
        // during the update call, hence changing the observers list size
        for (int i = observers.size() - 1; i >= 0; i--) {
            observers.get(i).update(e);
        }
    }
}
