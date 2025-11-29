import java.util.ArrayList;
import java.util.List;

public class NQueensWTime {

    // Function to print the board
    private static void printBoard(char[][] board) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println(); // Add a blank line between solutions
    }

    // Function to check if a queen can be placed at board[row][col]
    private static boolean isSafe(char[][] board, int row, int col) {
        int i, j;

        // Check the row on the left side
        for (i = 0; i < col; i++) {
            if (board[row][i] == 'Q') {
                return false;
            }
        }

        // Check upper diagonal on the left side
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q') {
                return false;
            }
        }

        // Check lower diagonal on the left side
        for (i = row, j = col; j >= 0 && i < 4; i++, j--) {
            if (board[i][j] == 'Q') {
                return false;
            }
        }

        return true;
    }

    // Recursive function to solve N Queen problem
    private static boolean solveNQueensUtil(char[][] board, int row) {
        // Base case: If all queens are placed, print the board
        if (row == 4) {
            printBoard(board);
            return true;
        }

        boolean res = false;

        // Try placing a queen in all columns
        for (int col = 0; col < 4; col++) {

            if (isSafe(board, row, col)) {
                board[row][col] = 'Q';

                res = solveNQueensUtil(board, row + 1) || res;

                board[row][col] = '.'; // backtrack
            }
        }

        return res;
    }

    public static void main(String[] args) {

        long start = System.nanoTime(); // Start timer

        char[][] board = new char[4][4];

        // Initialize the board with '.'
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                board[i][j] = '.';
            }
        }

        if (solveNQueensUtil(board, 0) == false) {
            System.out.println("Solution does not exist");
        }

        long end = System.nanoTime(); // End timer

        long duration = end - start;

        System.out.println("Execution time (nanoseconds): " + duration);
        System.out.println("Execution time (milliseconds): " + duration / 1_000_000.0);
    }
}
