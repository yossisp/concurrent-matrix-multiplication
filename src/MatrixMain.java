/*
@author - Yosef Spektor

the main class controls the execution of matrix multiplication and contains a static method
which parses user input from command line.

First Controller object is initialized.
Then we run a for-loop which creates new threads in order to calculate cell in the multiplication matrix.
Each time a thread finishes its job it prints out the result, if all the preceding cells had been calculated as well.

OVERVIEW:

Controller class contains most of the logic when to wait and when to notify waiting threads. It makes sure that the
relevant methods are synchronized so that the results of multi-threaded application are consistent. It also stores the
multiplication result matrix as well as takes user input for matrices dimensions.

CalculateThread class performs the calculation work for each cell.

Utils contains all the constants.
 */

public class MatrixMain {
    public static void main(String[] args) {
        int[] dimArray = parseCommandLineParams(args);
        Controller c = new Controller(Utils.MAX_THREADS, dimArray[Utils.MATRIX_A_ROWS],
                                                        dimArray[Utils.MATRIX_A_COLUMNS],
                                                        dimArray[Utils.MATRIX_B_COLUMNS]);

        int i, j; //indices for loops
        int[][] m = c.getMultiplicationMatrix(); //helper matrix

        /*
        waitForThread() method throws an exception so we handle it here
         */
        try {
            //loop through multiplication result matrix cells
            for(i = 0; i < m.length; i++) {
                for(j = 0; j < m[Utils.FIRST_ROW].length; j++) {
                    c.waitForThread(i, j);
                    (new CalculateThread(c, i, j)).start();
                }
            }
        } catch (InterruptedException e) {
            System.err.println(e.getStackTrace().toString());
            System.out.println("Fatal error. Exiting the app.\n");
            System.exit(Utils.BAD_EXIT);
        }
    }

    private static int[] parseCommandLineParams(String[] args) {
        int[] result = new int[Utils.NUMBERS_FOR_MATRIX_DIMENSIONS];
        int i;

        //if the user entered too many or too little arguments we exit the app
        if(args.length != Utils.NUMBERS_FOR_MATRIX_DIMENSIONS) {
            System.out.printf("Illegal parameter length.\nExiting the app.\n");
            System.exit(Utils.BAD_EXIT);
        }
        try {
            //set the result array
            for(i = 0; i < Utils.NUMBERS_FOR_MATRIX_DIMENSIONS; i++) {
                result[i] = Integer.parseInt(args[i]);
            }
        } catch(NumberFormatException e) {
            System.err.print(e.getStackTrace().toString());
            System.out.printf("\nNon-integer parameters entered.\nExiting the app.\n");
            System.exit(Utils.BAD_EXIT);
        }
        return result;
    }
}
