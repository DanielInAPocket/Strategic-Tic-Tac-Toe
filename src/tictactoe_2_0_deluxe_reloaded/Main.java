
package tictactoe_2_0_deluxe_reloaded;

import java.util.Scanner;
import tictactoe_2_0_deluxe_reloaded.Game.OpponentType;

public class Main {

    //---------------------------------------------------------
    // Class variables
    //---------------------------------------------------------

    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {       
        
        boolean validInput = false;
        OpponentType opponent = null;
        
        do
        {
            System.out.println("Choose you opponent:");
            System.out.println("1 - Local player");
            System.out.println("2 - Host network game");
            System.out.println("3 - Join network game");
            System.out.println("4 - Idiot CPU");
            System.out.println("5 - Easy CPU");
            System.out.println("6 - Medium CPU");
            System.out.println("7 - Hard CPU");

            int choise = getIntegerInput();            

            switch (choise) {
                case 1:
                    opponent = OpponentType.LocalHuman;
                    validInput = true;
                    break;
                case 2:
                    opponent = OpponentType.NetworkHumanHost;
                    validInput = true;
                    break;
                case 3:
                    opponent = OpponentType.NetworkHumanClient;
                    validInput = true;
                    break;
                case 4:
                    opponent = OpponentType.IdiotCPU;
                    validInput = true;
                    break;
                case 5:
                    opponent = OpponentType.EasyCPU;
                    validInput = true;
                    break;
                case 7:
                    opponent = OpponentType.MediumCPU;
                    validInput = true;
                    break;
                case 8:
                    opponent = OpponentType.HardCPU;
                    validInput = true;
                    break;
                default:
                    System.out.println("Your choise is unsupported!");
                    validInput = false;
                    break;
            }
        } while (validInput == false);

        Game game = new Game(opponent);              
        game.play();
    }
    
    //---------------------------------------------------------
    // Private / Utility methods
    //---------------------------------------------------------

    private static int getIntegerInput() {  
        
        while (!in.hasNextInt()) {
            in.next();
        }
        
        int input = in.nextInt();

        return input;
    }

}
