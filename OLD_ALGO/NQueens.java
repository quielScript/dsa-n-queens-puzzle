import java.util.ArrayList;
import java.util.List;

public class NQueens {

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
            return true; //We can stop looking for other solution
        }
        boolean res = false; // Indicate if we found a solution

        // Consider this column and try placing this queen in all columns one by one
        for (int col = 0; col < 4; col++) {
            // Check if the queen can be placed at board[row][col]
            if (isSafe(board, row, col)) {
                // Place this queen in board[row][col]
                board[row][col] = 'Q';

                // Recur to place rest of the queens
                res = solveNQueensUtil(board, row + 1) || res; //find other solution

                // If placing queen doesn't lead to a solution, then backtrack
                // and remove queen from board[row][col]
                board[row][col] = '.'; // backtrack
            }
        }

        // If the queen can not be placed in any column in this row, return false
        return res;
    }

    // This function solves the N Queen problem using Backtracking.  It mainly uses
    // solveNQueensUtil() to solve the problem. It returns false if queens cannot be
    // placed, otherwise, return true and prints placement of queens in the form of 1s.
    // Please note that there may be more than one solutions, this function prints one  of the
    // feasible solutions.
    public static void main(String[] args) {
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
    }
}