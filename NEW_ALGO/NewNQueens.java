public class NewNQueens {

    static final int N = 4;

    // Print the board
    private static void printBoard(char[][] board) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Optimized solver using boolean arrays
    private static boolean solveNQueensUtil(
            char[][] board,
            int row,
            boolean[] colUsed,
            boolean[] diag1Used,
            boolean[] diag2Used
    ) {
        if (row == N) {
            printBoard(board);
            return true;
        }

        boolean foundSolution = false;

        for (int col = 0; col < N; col++) {
            int d1 = row - col + (N - 1); // main diag index
            int d2 = row + col;           // anti diag index

            if (!colUsed[col] && !diag1Used[d1] && !diag2Used[d2]) {

                board[row][col] = 'Q';
                colUsed[col] = diag1Used[d1] = diag2Used[d2] = true;

                foundSolution = solveNQueensUtil(board, row + 1, colUsed, diag1Used, diag2Used)
                                || foundSolution;

                board[row][col] = '.';
                colUsed[col] = diag1Used[d1] = diag2Used[d2] = false;
            }
        }

        return foundSolution;
    }

    public static void main(String[] args) {
        char[][] board = new char[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = '.';
            }
        }

        boolean[] colUsed = new boolean[N];
        boolean[] diag1Used = new boolean[2 * N - 1];
        boolean[] diag2Used = new boolean[2 * N - 1];

        if (!solveNQueensUtil(board, 0, colUsed, diag1Used, diag2Used)) {
            System.out.println("Solution does not exist");
        }
    }
}
