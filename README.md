# Word-Guessing-Game
This is my take on the popular online word game "Wordle" from the New York Times. My twist is that multiple games are linked together to the same input
via the Observer Pattern.

## Install
Create a project and include all source files given. In the driver file, update the path of the
dictionary files to the path of your files on your machine (This will be updated in the future).
The "wordle-La.txt" file contains all words that can be selected for the puzzle and can be used to make guesses.
The "wordle-Ta.txt" file contains all words that can't be chosen for the puzzle but can be used to make guesses (This file contains more obscure words). 

## How to Play
Once the file paths for the dictionary are updated, enter the number of puzzles to play simultaneously, the number of guesses you want, and 
the number of characters for words to use (currently locked at five). Once entered, use the JFrame title "Enter Guess" to type guess words and use
the "enter/return" key to submit a guess. If you haven't played the New York Times game "Wordle," the objective is to solve the puzzle/puzzles 
within the number of guesses you have chosen. The guess you submit will indicate on all puzzle boards which letters in the guess are part of the word you
are trying to guess. Green letters indicate if a letter in the guess is in the correct position. Yellow letters indicate a
letter is present in the word but in a different location. Grey/White letters indicate the letter is not present in the word. Solve all puzzles before the
number of guesses is 

## Future Updates
I plan to incorporate a keyboard object to display letters already used and a key to describe which
letters guessed are present in the puzzles. 

I also plan to update the file upload feature for dictionary uploads.

The game is also locked only to include five-letter words, as I haven't created or found suitable libraries
for other word lengths.
