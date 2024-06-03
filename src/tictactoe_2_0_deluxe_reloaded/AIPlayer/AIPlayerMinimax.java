
package tictactoe_2_0_deluxe_reloaded.AIPlayer;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;
import tictactoe_2_0_deluxe_reloaded.Board;
import tictactoe_2_0_deluxe_reloaded.Coordinates;
import tictactoe_2_0_deluxe_reloaded.Player;
import tictactoe_2_0_deluxe_reloaded.PlayerMove;
import tictactoe_2_0_deluxe_reloaded.Square;
import tictactoe_2_0_deluxe_reloaded.Square.SquareState;

public class AIPlayerMinimax extends AIPlayer {
    
    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    private final int[] winningPatterns = {
        0b111000000, 0b000111000, 0b000000111, // rows
        0b100100100, 0b010010010, 0b001001001, // cols
        0b100010001, 0b001010100               // diagonals
    };

    final int depth;
    
    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public AIPlayerMinimax(Player.PlayerType type, Board board, int depth) {
        super(type, board);
        this.depth = depth;
    }

    //---------------------------------------------------------
    // Move making methods
    //---------------------------------------------------------

    @Override
    public PlayerMove makeMoveInSection(Coordinates section) {
        
        if(section == null) {
            section = minimax(this.depth, type, board.globalBoardState).getValue();
        }
        
        Coordinates square = minimax(this.depth, type, board.boardState[section.row][section.column]).getValue();
        
        return new PlayerMove(section, square, this.type);
    }
    
    //---------------------------------------------------------
    // Private methods
    //---------------------------------------------------------

    private Pair<Integer, Coordinates> minimax(int depth, PlayerType player,Square[][] grid) {
        
        List<Coordinates> nextMoves = generateMoves(grid);

        int bestScore = (player == type) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int currentScore;
        Coordinates bestSquare = null;

        if (nextMoves.isEmpty() || depth == 0) {
            bestScore = evaluate(grid);
        } else {
            for (Coordinates move : nextMoves) {

                SquareState state = SquareState.fromPlayerType(type);
                grid[move.row][move.column].changeState(state);
                
                if (player == type) {  
                    // mySeed (computer) is maximizing player
                    currentScore = minimax(depth - 1, oppType, grid).getKey();
                    if (currentScore > bestScore) {
                        bestScore = currentScore;
                        bestSquare = move;
                    }
                } else {  
                    // oppSeed is minimizing player
                    currentScore = minimax(depth - 1, type, grid).getKey();
                    if (currentScore < bestScore) {
                        bestScore = currentScore;
                        bestSquare = move;
                    }
                }
                
                // Undo move
                grid[move.row][move.column].changeState(SquareState.Empty);
            }
        }
        return new Pair<>(bestScore, bestSquare);
    }
 
    private List<Coordinates> generateMoves(Square[][] grid) {
        
        List<Coordinates> nextMoves = new ArrayList<>();

        if (hasWon(type, grid) || hasWon(oppType, grid)) {
            return nextMoves;
        }

        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[row].length; ++col) {
                if (grid[row][col].getState() == SquareState.Empty) {
                    nextMoves.add(new Coordinates(row, col));
                }
            }
        }
        return nextMoves;
    }

    private int evaluate(Square[][] grid) {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2, grid);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2, grid);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2, grid);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0, grid);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1, grid);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2, grid);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2, grid);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0, grid);  // alternate diagonal
        return score;
    }

    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3, Square[][] grid) {
        int score = 0;

        // First cell
        SquareState myState = SquareState.fromPlayerType(type);
        SquareState oppState = SquareState.fromPlayerType(oppType);

        if (grid[row1][col1].getState() == myState) {
            score = 1;
        } else if (grid[row1][col1].getState() == oppState) {
            score = -1;
        }

        // Second cell
        if (grid[row2][col2].getState() == myState) {
            switch (score) {
                case 1:
                    // cell1 is mySeed
                    score = 10;
                    break;
                case -1:
                    // cell1 is oppSeed
                    return 0;
                default:
                    // cell1 is empty
                    score = 1;
                    break;
            }
        } else if (grid[row2][col2].getState() == oppState) {
            switch (score) {
                case -1:
                    // cell1 is oppSeed
                    score = -10;
                    break;
                case 1:
                    // cell1 is mySeed
                    return 0;
                default:
                    // cell1 is empty
                    score = -1;
                    break;
            }
        }

        // Third cell
        if (grid[row3][col3].getState() == myState) {
            
            if (score > 0) {  
                // cell1 and/or cell2 is mySeed
                score *= 10;
            } else if (score < 0) {  
                // cell1 and/or cell2 is oppSeed
                return 0;
            } else {  
                // cell1 and cell2 are empty
                score = 1;
            }
        } else if (grid[row3][col3].getState() == oppState) {
            
            if (score < 0) {    
                // cell1 and/or cell2 is oppSeed
                score *= 10;
            } else if (score > 1) {  
                // cell1 and/or cell2 is mySeed
                return 0;
            } else {  
                // cell1 and cell2 are empty
                score = -1;
            }
        }
        return score;
    }

    private boolean hasWon(PlayerType thePlayer, Square[][] grid) {
        int pattern = 0b000000000;
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[row].length; ++col) {
                SquareState state = SquareState.fromPlayerType(thePlayer);
                if (grid[row][col].getState() == state) {
                    pattern |= (1 << (row * grid[row].length + col));
                }
            }
        }
        for (int winningPattern : winningPatterns) {
            if ((pattern & winningPattern) == winningPattern) return true;
        }
        return false;
    }
}
