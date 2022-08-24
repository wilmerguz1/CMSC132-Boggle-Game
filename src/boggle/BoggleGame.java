package boggle;

import java.util.Stack;

import utils.LetterGrid;
import utils.SimpleGrid;

public class BoggleGame {
	
	/**
	 * The grid that contains all the letters. @see boggle.LetterGrid
	 */
	LetterGrid grid;
	
	/**
	 * The stack that stores the path when you search the word path
	 */
	Stack<String> path;
	
	/**
	 * A boolean array to mark any location (row,col) as visited
	 */
	boolean[][] visited;
	
	//Count frequency of words.
	int countFrequency = 0;
	
	public BoggleGame(LetterGrid g) {
		this.grid = g;
	}
	
	/**
	 * Finds word in boggle grid.
	 * @param word
	 * @return true if "word" can be found in grid, false otherwise
	 */
	public boolean findWord(String word) {
		int maxRow = this.grid.getNumRows();
		int maxCol = this.grid.getNumCols();
		this.visited = new boolean[maxRow][maxCol];
		this.path = new Stack();
		if (word == null || word.equals("")) {			
			return false;
		}
		for (int row = 0; row < maxRow; row++) {
			for (int col = 0; col < maxCol; col++) {
				if (this.grid.getLetter(row, col) == word.charAt(0)) {
					this.path.clear(); //Helps rid stack overflow error.
					if (findWordHelper(row, col, 0, word)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * Recursively iterates through letter by letter. Returns true if word is in
	 * the grid, false otherwise.
	 */
	
	private boolean findWordHelper(int row, int col, int index, String word) {
		if (index == word.length()) {
			return true;
		}
		if (row < 0 || row >= this.grid.getNumRows() || col < 0 || 
				col >= this.grid.getNumCols() || 
				word.charAt(index) != this.grid.getLetter(row, col) || 
				this.visited[row][col]) {
			return false;
		}
		this.visited[row][col] = true;
		if (findWordHelper(row + 1, col + 1, index + 1, word) || 
				findWordHelper(row + 1, col, index + 1, word) || 
				findWordHelper(row + 1, col - 1, index + 1, word) || 
				findWordHelper(row, col + 1, index + 1, word) || 
				findWordHelper(row, col - 1, index + 1, word) ||
				findWordHelper(row - 1, col + 1, index + 1, word) || 
				findWordHelper(row - 1, col, index + 1, word) || 
				findWordHelper(row - 1, col - 1, index + 1, word)) {
			this.path.push("(" + row + "," + col + ")");
			return true;
		}
		this.visited[row][col] = false;
		return false;
	}

	/**
	 * @param word
	 * @return the path (cell index) of each letter
	 */
	public String findWordPath(String word) {
		String answer = "";
		if (findWord(word) == false) {
			return answer;
		}
		while (this.path.isEmpty() == false) {
			answer += this.path.pop();
		}
		return answer;
	}
	
	/**
	 * Determines the frequency of a word on the Boggle board. For simplicity,
	 * assume palindromes count twice.
	 * @param word
	 * @return the number of occurrences of the "word" in the grid
	 */
	public int frequency(String word) {
		//If word is palindrome.
		if (isPalindrome(word)) {
			int maxRow = this.grid.getNumRows();
			int maxCol = this.grid.getNumCols();
			this.visited = new boolean[maxRow][maxCol];
			for (int row = 0; row < maxRow; row++) {
				for (int col = 0; col < maxCol; col++) {
					frequencyHelpIsPalindrome(word, row, col, 0);
				}
			}
			return this.countFrequency;
		//Not a palindrome
		} else {
			frequencyHelpNotPalindrome(word);
			return this.countFrequency;
		}
	}
	
	//Checks to see if word is palindrome or not.
	private static boolean isPalindrome(String str) {
		Stack<Character> stack = new Stack<>();
		String strBackwards = "";
		str = str.replace(" ", "");
		for (int i = 0; i < str.length(); i++) {
			stack.push(str.charAt(i));
		}
		while (!stack.isEmpty()) {
			strBackwards += stack.pop();
		}
		if (str.equals(strBackwards)) {
			return true;
		}
		return false;
	}
	
	/*
	 * Count frequency differently since word is a palindrome.
	 */
	private void frequencyHelpIsPalindrome(String word, int row, int col, 
			int index) {
		char temp; //Check if duplicate.
		//If out of bounds, return.
		if (row < 0 || col < 0 || row >= this.grid.getNumRows() || 
				col >= this.grid.getNumCols()) {
			return;
		}
		temp = this.grid.getLetter(row, col);
		//If not a match.
		if (temp != word.charAt(index)) {
			return;
		}
		if (index == word.length() - 1) {
			this.countFrequency++;
			return;
		}
		//Check each direction.
		this.grid.setLetter(row, col, '?');
		frequencyHelpIsPalindrome(word, row + 1, col + 1, index + 1);
		frequencyHelpIsPalindrome(word, row + 1, col, index + 1);
		frequencyHelpIsPalindrome(word, row + 1, col - 1, index + 1);
		frequencyHelpIsPalindrome(word, row, col + 1, index + 1);
		frequencyHelpIsPalindrome(word, row, col - 1, index + 1);
		frequencyHelpIsPalindrome(word, row - 1, col + 1, index + 1);
		frequencyHelpIsPalindrome(word, row - 1, col, index + 1);
		frequencyHelpIsPalindrome(word, row - 1, col - 1, index + 1);
		this.grid.setLetter(row, col, temp);
		return;
	}
	
	/*
	 * Count frequency differently since word is not a palindrome, similar to
	 * findWord.
	 */
	private void frequencyHelpNotPalindrome(String word) {
		int maxRow = this.grid.getNumRows();
		int maxCol = this.grid.getNumCols();
		this.visited = new boolean[maxRow][maxCol];
		if (word == null || word.equals("")) {
			return;
		}
		for (int row = 0; row < maxRow; row++) {
			for (int col = 0; col < maxCol; col++) {
				if (this.grid.getLetter(row, col) == word.charAt(0)) {
					if (findWordHelperFreq(row, col, 0, word)) {
						this.countFrequency++;
					}
				}
			}
		}
	}
	
	/*
	 * Similar to findWordHelper except this method isn't pushing items in the
	 * stack.
	 */
	private boolean findWordHelperFreq(int row, int col, int index, 
			String word) {
		if (index == word.length()) {
			return true;
		}
		//If out of bounds, return. 
		if (row < 0 || row >= this.grid.getNumRows() || col < 0 || 
				col >= this.grid.getNumCols() || 
				word.charAt(index) != this.grid.getLetter(row, col) || 
				this.visited[row][col]) {
			return false;
		}
		//Check each direction.
		this.visited[row][col] = true;
		if (findWordHelperFreq(row + 1, col + 1, index + 1, word) || 
				findWordHelperFreq(row + 1, col, index + 1, word) || 
				findWordHelperFreq(row + 1, col - 1, index + 1, word) || 
				findWordHelperFreq(row, col + 1, index + 1, word) || 
				findWordHelperFreq(row, col - 1, index + 1, word) ||
				findWordHelperFreq(row - 1, col + 1, index + 1, word) || 
				findWordHelperFreq(row - 1, col, index + 1, word) || 
				findWordHelperFreq(row - 1, col - 1, index + 1, word)) {
			return true;
		}
		this.visited[row][col] = false;
		return false;
	}
}