/*
@author - Yosef Spektor

the class is responsible for calculating dot product of two vectors which corresponds to the value of a cell in the
result multiplication matrix. The class extends Thread and overrides run().
 */
public class CalculateThread extends Thread {
    private Controller controller; //each thread communicates with the same Controller object which stores multiplication results
    private int row; //the row of the cell in the multiplication matrix to be calculated
    private int column; //the column of the cell in the multiplication matrix to be calculated

    /*
    constructor
     */
    public CalculateThread(Controller controller, int row, int column) {
        this.controller = controller;
        this.row = row;
        this.column = column;
    }

    /*
    the method contains all the logic required to calculate the dot product of two vectors represented by two 1d
    integer arrays. then the method stores the result in the result matrix in the Controller object
     */
    @Override
    public void run() {
        int number; //stores the result of dot product

        //helper variables (it's easier to subset shorter names)
        int[][] A = this.controller.getMatrixA();
        int[][] B = this.controller.getMatrixB();

        /*
        calculates the dot product of row's row of A and column's column of B
         */
        number = this.multiplyVectors(A[this.row],
                this.getMatrixBColumnAs1dArray(this.column));

        //sets the result of the dot product to the multiplicationMatrix in Controller.
        this.controller.setMultiplicationMatrixCell(number, this.row, column);
        try {
            System.out.print(number + Utils.DELIMITER); //prints the result
            /*
            once the thread finished its work load it signals that it's done. This is needed because other threads may be
            waiting for the thread to finish calculation.
             */
            this.controller.finished();

            //prints a newline after matrix row is done
            if(this.column == this.controller.getMultiplicationMatrix()[Utils.FIRST_ROW].length - 1) {
                System.out.println();
            }
        } catch(IllegalMonitorStateException e) {
            System.err.println(e.getStackTrace().toString());
            System.out.println("Fatal error. Exiting the app.\n");
            System.exit(Utils.BAD_EXIT);
        }



    }

    /*
    performs dot product on two vectors represented as one-dimensional arrays
     */
    private int multiplyVectors(int[] a, int[] b) {
        int i;
        int result = 0; //contains the result value of vector multiplication
        for(i = 0; i < a.length; i++) {
            result += a[i] * b[i];
        }
        return result;
    }

    /*
    received a 2d array and returns a 1d array containing the values of the column requested.
    When matrix multiplication is performed we're interested to present the columns of matrixB
    as 1d array hence the name of the method.
    */
    private int[] getMatrixBColumnAs1dArray(int column) {
        int i;
        int[][] m = this.controller.getMatrixB();
        int[] resultArray = new int[m.length];
        for(i = 0; i < m.length; i++) {
            resultArray[i] = m[i][column];
        }
        return resultArray;
    }
}
