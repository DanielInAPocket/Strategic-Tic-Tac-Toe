
package tictactoe_2_0_deluxe_reloaded;
 
import tictactoe_2_0_deluxe_reloaded.Player.PlayerType;
import tictactoe_2_0_deluxe_reloaded.Square.SquareState;

public class Board {
    
    //---------------------------------------------------------
    // Constants and Enumurations
    //---------------------------------------------------------

    public static final int BOARD_ROWS = 3;
    public static final int BOARD_COLS = 3;
    
    public static final int ROWS = 3;
    public static final int COLS = 3;
    
    public static final int ELEMENTS_TO_WIN = 3;                                        

    public static final int ARRAY_INDEX_OFFSET = 1;

    public enum BoardState {
        Playing, 
        X_Won, 
        O_Won,
        Tie 
    }

    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    public final Square[][][][] boardState = new Square[BOARD_ROWS][BOARD_COLS][ROWS][COLS];
    public final Square[][] globalBoardState = new Square[BOARD_ROWS][BOARD_COLS];
    private Coordinates currentSection;

    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public Board() {
        for (int board_row = 0; board_row < BOARD_ROWS; ++board_row) {
            for (int board_col = 0; board_col < BOARD_COLS; ++board_col) {

                this.globalBoardState[board_row][board_col] = new Square();

                for (int row = 0; row < ROWS; ++row) {
                    for (int col = 0; col < COLS; ++col) {
                        this.boardState[board_row][board_col][row][col] = new Square();
                    }
                }
            }
        }
    }

    //---------------------------------------------------------
    // Board state getters
    //---------------------------------------------------------

    public BoardState getGlobalBoardState() {
        
        BoardState state;
        
        if(hasWonInSection(this.globalBoardState, PlayerType.X)) {
            state = BoardState.X_Won;
        } else if (hasWonInSection(this.globalBoardState, PlayerType.O)) {
            state = BoardState.O_Won;
        } else if (isDrawInSection(this.globalBoardState)) {
            state = BoardState.Tie;
        } else {
            state = BoardState.Playing;
        }
        
        return state;
    }

    public boolean isPlayableSection(Coordinates section) {
        
        boolean rowIsInBounds = (section.row    >= 0  &&  section.row    < BOARD_ROWS);
        boolean colIsInBounds = (section.column >= 0  &&  section.column < BOARD_COLS);
        boolean isPlayableSection;

        if(rowIsInBounds && colIsInBounds) {
            isPlayableSection = globalBoardState[section.row][section.column].getState() == SquareState.Empty;
        } else {
            isPlayableSection = false;
        }

        return isPlayableSection;
    }
    
    public boolean isPlayableSquareInSection(Coordinates square, Coordinates section) {
                
        boolean rowIsInBounds = (square.row    >= 0  &&  square.row    < ROWS);
        boolean colIsInBounds = (square.column >= 0  &&  square.column < COLS);
        boolean isEmptyCell;

        if(rowIsInBounds && colIsInBounds) {
            isEmptyCell = boardState[section.row][section.column][square.row][square.column].getState() == SquareState.Empty;
        } else {
            isEmptyCell = false;
        }

        return isEmptyCell;
    }

    //---------------------------------------------------------
    // Board changing methods 
    //---------------------------------------------------------

    public boolean applyPlayerMove(PlayerMove move) {
        
        boolean sectionUpdated = updateCurrentSection(move.section);
        boolean squareStateUpdated = false;
        
        if(sectionUpdated) {
            squareStateUpdated = updateSquareStateInCurrentSection(move.square, move.playerType);
        }
        
        return squareStateUpdated;
    }
    
    private boolean updateCurrentSection(Coordinates section) {

        if(this.currentSection == null) {
            this.currentSection = section;
            return true;
        }
        
        if(this.currentSection.row == section.row && this.currentSection.column == section.column) {
            // No need to update -> Exit
            return true;
        }
        
        boolean sectionUpdated;
        
        if(isPlayableSection(section)) {
            this.currentSection = section;
            sectionUpdated = true;
        } else {
            sectionUpdated = false;
        }

        return sectionUpdated;
    }

    private void resetCurrentSection() {
        this.currentSection = null;
    }

    private boolean updateSquareStateInCurrentSection(Coordinates square, PlayerType playerType) {
   
        boolean moveMade;
        
        if(isPlayableSquareInSection(square, currentSection)) {
            SquareState state = SquareState.fromPlayerType(playerType);
            this.boardState[currentSection.row][currentSection.column][square.row][square.column].changeState(state);
            this.updateCurrentSectionState(playerType);
            
            if(isPlayableSection(square)) {
                updateCurrentSection(square);
            } else {
                resetCurrentSection();
            }
            
            moveMade = true;
        } else {
            moveMade = false;
        }

        return moveMade;
    }
    
    private void updateCurrentSectionState(PlayerType playerType) {

        Square[][] grid = this.boardState[currentSection.row][currentSection.column];

        if(this.hasWonInSection(grid, playerType)) {
            this.globalBoardState[currentSection.row][currentSection.column].changeState(SquareState.fromPlayerType(playerType));
        } else if(this.isDrawInSection(grid)) {
            this.globalBoardState[currentSection.row][currentSection.column].changeState(SquareState.Tie);
        }

        this.updateGlobalBoardState(playerType);
    }
        
    private void updateGlobalBoardState(PlayerType playerType) {

        if(this.hasWonInSection(this.globalBoardState, playerType)) {
            this.globalBoardState[currentSection.row][currentSection.column].changeState(SquareState.fromPlayerType(playerType));
        } else if(this.isDrawInSection(this.globalBoardState)) {
            this.globalBoardState[currentSection.row][currentSection.column].changeState(SquareState.Tie);
        }
    }

    //---------------------------------------------------------
    // State check methods
    //---------------------------------------------------------
    
    private boolean isDrawInSection(Square[][] grid) {
        
        for (int row = 0; row < grid.length; ++row) {
            for (int col = 0; col < grid[row].length; ++col) {
                if (grid[row][col].getState() == SquareState.Empty) {
                    return false;
                }
            }
        }

        return true;
    }
    
    private boolean hasWonInSection(Square[][] grid, PlayerType playerType) {
        
        boolean hasWonInRow = hasWonInRow(grid, playerType);
        boolean hasWonInCol = hasWonInCol(grid, playerType);
        boolean hasWonInDiag = hasWonInDiag(grid, playerType);

        return hasWonInRow || hasWonInCol || hasWonInDiag;
    }
    
    private boolean hasWonInRow(Square[][] grid, PlayerType playerType) {
        
        int counter;

        for (int row = 0; row < grid.length; ++row) {
            counter = 0;
            for(int col = 0; col < grid[row].length; ++col) {
                SquareState state = SquareState.fromPlayerType(playerType);
                if(grid[row][col].getState() == state) {
                    counter++;
                    if(counter == ELEMENTS_TO_WIN) {
                        return true;
                    }
                } else {
                    counter = 0;
                }
            }
        }

        return false;
    }
    
    private boolean hasWonInCol(Square[][] grid, PlayerType playerType) {
        
        int counter;

        for(int col = 0; col < grid[1].length; ++col) {
            counter = 0;
            for (int row = 0; row < grid.length; ++row) {
                SquareState state = SquareState.fromPlayerType(playerType);
                if(grid[row][col].getState() == state) {
                    counter++;
                    if(counter == ELEMENTS_TO_WIN) {
                        return true;
                    }
                } else {
                    counter = 0;
                }
            }
        }

        return false;
    }
    
    private boolean hasWonInDiag(Square[][] grid, PlayerType playerType) {
        int counter;

        // From right -> To left
        for(int col = 0; col < grid[1].length; ++col) {
            counter = 0;
            if(col + ELEMENTS_TO_WIN <= grid[1].length) {
                for (int row = 0; row < grid.length; ++row) {
                    if(row + ELEMENTS_TO_WIN <= grid.length) {
                        for(int i = 0; i < 3; i++) {
                            SquareState state = SquareState.fromPlayerType(playerType);
                            if(grid[row + i][col + i].getState() != state) {
                                counter = 0;
                                break;
                            } else {
                                counter++;
                                if(counter == ELEMENTS_TO_WIN) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }

        // To right <- From left
        for(int col = grid[1].length - ARRAY_INDEX_OFFSET; col >= 0; --col) {
            counter = 0;
            if(col - ELEMENTS_TO_WIN + ARRAY_INDEX_OFFSET >= 0) {
                for (int row = 0; row < grid.length; ++row) {
                    if(row + ELEMENTS_TO_WIN <= grid.length) {
                        for(int i = 0; i < 3; i++) {
                            SquareState state = SquareState.fromPlayerType(playerType);
                            if(grid[row + i][col - i].getState() != state) {
                                counter = 0;
                                break;
                            } else {
                                counter++;
                                if(counter == ELEMENTS_TO_WIN) {
                                    return true;
                                }
                            }
                        }
                    } else {
                        break;
                    }
                }
            } else {
                break;
            }
        }

        return false;
    }
    
    public Coordinates getCurrentSection() {
        return this.currentSection;
    }
    
    //---------------------------------------------------------
    // Board printing methods
    //---------------------------------------------------------

    public String printBoard() {
        
        String board = "";
        
        for(int boardRow = 0; boardRow < BOARD_ROWS; ++boardRow) {

            for(int boardCols = 0; boardCols < BOARD_COLS; ++boardCols)
            {
                board += " ";

                for(int cols = 0; cols < COLS; ++cols) {
                    board += "    ";
                }

                if(boardCols < BOARD_COLS - 1) {
                    board += "|";
                }
            }

            board += System.lineSeparator();

            for(int row = 0; row < ROWS; ++row) {

                for (int boardCol = 0; boardCol < BOARD_COLS; ++boardCol) {

                    board += " ";

                    for(int col = 0; col < COLS; ++col) {

                        board += boardState[boardRow][boardCol][row][col].getStateString();
                        if (col != COLS - 1) {
                            board += "|";
                        }
                    }

                    if(boardCol != BOARD_COLS - 1) {
                        board += " |";
                    }
                }

                if(row != ROWS - 1) {
                    board += System.lineSeparator();
                }

                for (int boardCol = 0; boardCol < BOARD_COLS; ++boardCol) {

                    board += " ";

                    if(row != ROWS - 1) {
                        for (int col = 0; col < COLS; ++col) {
                            board += "---";
                            if (col != COLS - 1) {
                                board += "+";
                            }
                        }
                        if(boardCol != BOARD_COLS - 1) {
                            board += " |";
                        }
                    }
                }

                board += System.lineSeparator();
            }

            for(int boardCols = 0; boardCols < BOARD_COLS; ++boardCols)
            {
                board += " ";

                for(int cols = 0; cols < COLS; ++cols) {
                    board += "    ";
                }

                if(boardCols < BOARD_COLS - 1) {
                    board += "|";
                }
            }

            board += System.lineSeparator();

            if(boardRow != BOARD_ROWS - 1) {

                for(int boardCols = 0; boardCols < BOARD_COLS; boardCols++) {

                    board += "-";

                    for(int cols = 0; cols < COLS; ++cols) {
                        board += "----";
                    }

                    if(boardCols < BOARD_COLS - 1) {
                        board += "+";
                    }
                }
            }

            board += System.lineSeparator();
        }
        
        board += printGlobalBoard();
        
        return board;
    }
    
    private String printGlobalBoard() {
        
        String board = "";
        
        for (int row = 0; row < BOARD_ROWS; ++row) {
            for (int col = 0; col < BOARD_COLS; ++col) {
                board += globalBoardState[row][col].getStateString();
                if (col != BOARD_COLS - 1) {
                    board += "|";
                }
            }

            board += System.lineSeparator();

            if (row != BOARD_ROWS - 1) {
                for (int col = 0; col < BOARD_COLS; ++col) {
                    board += "---";
                    if (col != BOARD_COLS - 1) {
                        board += "+";
                    }
                }
            }

            board += System.lineSeparator();
        }
                
        return board;
    }
}
