import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NQueens {

    private final int size;
    private final List<int[]> solutions;
    private final Set<Integer> cols;
    private final Set<Integer> diag1;
    private final Set<Integer> diag2;
    private int[] board;

    public NQueens(int size) {
        this.size = size;
        this.solutions = new ArrayList<>();
        this.cols = new HashSet<>();
        this.diag1 = new HashSet<>();
        this.diag2 = new HashSet<>();
        this.board = new int[size];
        for (int i = 0; i < size; i++) board[i] = -1;
    }

    public boolean isSafe(int row, int col) {
        return !cols.contains(col) &&
               !diag1.contains(row + col) &&
               !diag2.contains(row - col);
    }

    private void placeQueen(int row, int col) {
        board[row] = col;
        cols.add(col);
        diag1.add(row + col);
        diag2.add(row - col);
    }

    private void removeQueen(int row, int col) {
        board[row] = -1;
        cols.remove(col);
        diag1.remove(row + col);
        diag2.remove(row - col);
    }

    public List<int[]> solveAll() {
        reset();
        backtrack(0);
        return new ArrayList<>(solutions);
    }

    private void backtrack(int row) {
        if (row == size) {
            solutions.add(board.clone());
            return;
        }
        for (int col = 0; col < size; col++) {
            if (isSafe(row, col)) {
                placeQueen(row, col);
                backtrack(row + 1);
                removeQueen(row, col);
            }
        }
    }

    // For manual mode: try to place/remove a queen and return whether it was valid
    public boolean tryPlaceQueen(int row, int col) {
        if (board[row] == col) {
            // Remove existing queen
            removeQueen(row, col);
            return true;
        }
        if (board[row] != -1 || !isSafe(row, col)) {
            return false;
        }
        placeQueen(row, col);
        return true;
    }

    public int[] getBoard() {
        return board.clone();
    }

    public void setBoard(int[] newBoard) {
        // Reset tracking sets
        cols.clear();
        diag1.clear();
        diag2.clear();
        for (int r = 0; r < size; r++) {
            int c = newBoard[r];
            board[r] = c;
            if (c != -1) {
                cols.add(c);
                diag1.add(r + c);
                diag2.add(r - c);
            }
        }
    }

    public void reset() {
        for (int i = 0; i < size; i++) board[i] = -1;
        cols.clear();
        diag1.clear();
        diag2.clear();
        solutions.clear();
    }

    public int getSize() {
        return size;
    }

    public List<int[]> getSolutions() {
        return new ArrayList<>(solutions);
    }
}