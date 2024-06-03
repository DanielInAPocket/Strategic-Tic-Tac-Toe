
package tictactoe_2_0_deluxe_reloaded;

import java.util.Scanner;
import static tictactoe_2_0_deluxe_reloaded.Board.ARRAY_INDEX_OFFSET;

public class Player {
    
    //---------------------------------------------------------
    // Constants and Enumurations
    //---------------------------------------------------------

    public enum PlayerType {
        
        X, 
        O;
        
        public static PlayerType fromInt(int x) {
            switch(x) {
            case 0:
                return X;
            case 1:
                return O;
            }
            return null;
        }
    }

    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    public PlayerType type;
    private final Scanner in = new Scanner(System.in);
    
    //---------------------------------------------------------
    // Constructor
    //---------------------------------------------------------

    public Player(PlayerType type) {
        this.type = type;
    }
    
    //---------------------------------------------------------
    // Move making methods
    //---------------------------------------------------------

    public PlayerMove makeMoveInSection(Coordinates section) {  
        
        int row;
        int col;

        if(section == null) {
            System.out.print("Enter section to play in (row[1 - " + Board.BOARD_ROWS + "] column[1 - " + Board.BOARD_COLS + "]): ");
            
            row = getIntegerInput();
            col = getIntegerInput();
            
            section = new Coordinates(row - ARRAY_INDEX_OFFSET, col - ARRAY_INDEX_OFFSET);
        }

        System.out.print("Enter your move (row[1 - " + Board.ROWS + "] column[1 - " + Board.COLS + "]): ");

        row = getIntegerInput();
        col = getIntegerInput();

        Coordinates square = new Coordinates(row - ARRAY_INDEX_OFFSET, col - ARRAY_INDEX_OFFSET);

        return new PlayerMove(section, square, this.type);
    }
    
    //---------------------------------------------------------
    // Private / Utility methods
    //---------------------------------------------------------
    
    private int getIntegerInput() {  
        
        while (!in.hasNextInt()) {
            in.next();
        }
        
        int input = in.nextInt();

        return input;
    }
}
