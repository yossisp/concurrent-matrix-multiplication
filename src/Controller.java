/*
@author - Yosef Spektor

The class controls multiplication process of matrices A, B. It stores the matrices A, B, gets user input of matrices dimensions,
initializes the result matrix and stores the calculations from worker threads.
Each worker thread receives a Controller object and calculates one cell in the multiplicationMatrix.
 */



import java.security.SecureRandom;

public class Controller {
    private int maxThreads; //max amount of threads to be used
    private int activeThreads; //the amount of currently active threads

    private int[][] matrixA;
    private int[][] matrixB;
    private int[][] multiplicationMatrix; //holds the result of matrix multiplication A*B

    /*
    Constructor: initializes matrixA and matrixB as well as the multiplicationMatrix which holds the result of
    A*B matrix multiplication. The constructor also receives user input for matrix dimensions and fills the matrices
    A, B with random numbers
     */
    public Controller(int maxThreads, int matrixArows, int matrixAcolumns, int matrixBcolumns) {
        this.maxThreads = maxThreads;
        this.activeThreads = Utils.ZERO;
        this.matrixA = new int[matrixArows][matrixAcolumns];
        this.matrixB = new int[matrixAcolumns][matrixBcolumns];
        this.fillMatricesWithRandomNumbers(); //fills matrices A, B with random numbers
        this.multiplicationMatrix = new int[matrixA.length][matrixB[Utils.FIRST_ROW].length];

        //before any calculations all the values of the multiplicationMatrix will be Utils.DEFAULT_CELL_VALUE
        for(int i = 0; i < matrixA.length; i++) {
            for(int j = 0; j < matrixB[Utils.FIRST_ROW].length; j++) {
                this.multiplicationMatrix[i][j] = Utils.DEFAULT_CELL_VALUE;
            }
        }
    }
    //-----GETTERS----
    public int[][] getMultiplicationMatrix() {
        return this.multiplicationMatrix;
    }

    public int[][] getMatrixA() {
        return this.matrixA;
    }

    public int[][] getMatrixB() {
        return this.matrixB;
    }

    /*
    sets a cell given by row and column in multiplicationMatrix
     */
    public void setMultiplicationMatrixCell(int number, int row, int column) {
        this.multiplicationMatrix[row][column] = number;
    }

    /*
    the method checks whether we reached maxThreads number of threads or whether there're still preceding
    uncalculated numbers. if yes then we wait. At the end we increment the amount of activeThreads because we woke up.
    the method is synchronized so that the method is not interrupted in somewhere in order to guarantee
    consistent results.
     */
    public synchronized void waitForThread(int row, int column) throws InterruptedException {
        while(this.activeThreads == this.maxThreads) {
            this.wait();
        }
        while(this.uncalculatedCellsExistBefore(row, column)) {
            this.wait();
        }
        this.activeThreads++;
    }

    /*
    When the worker thread finishes its work we signal that the amount of working threads decreases and we call
    notifyAll so that the waiting threads awaken and perform their work.
    the method is synchronized so that the method is not interrupted in somewhere in order to guarantee
    consistent results.
     */
    public synchronized void finished() throws IllegalMonitorStateException {
        this.activeThreads--;
        this.notifyAll();
    }

    /*
    checks if there're any cells in the result multiplication matrix which still have default values before
    the cell this.multiplicationMatrix[row][column]. Each worker thread
    uses the method in order to determine whether it can print the calculation result (if the method returns true
    then the given worker thread must wait).
     */
    private boolean uncalculatedCellsExistBefore(int row, int column) {
        int i, j;
        int[][] m = this.multiplicationMatrix;

        /*
        we check all cells preceding matrix[row][column] if any
        of them is Utils.DEFAULT_CELL_VALUE.
        This value cannot be a valid value because the legal values range is between 0 and 10.
         */
        for(i = 0; i < m.length; i++) {
            for(j = 0; j < m[Utils.FIRST_ROW].length; j++) {
                if(i == row && j == column) //if we arrived here then we return false
                    return false;

                if(m[i][j] == Utils.DEFAULT_CELL_VALUE) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
    fills matrixA and matrixB with random numbers within the valid range
     */
    private void fillMatricesWithRandomNumbers() {
        SecureRandom rand = new SecureRandom(); //we use the class to get random numbers
        int i, j; //indices for loops

        //-------fill matrixA--------
        for(i = 0; i < this.matrixA.length; i++) {
            for(j = 0; j < this.matrixA[Utils.FIRST_ROW].length; j++) {
                this.matrixA[i][j] = Utils.LOWER_BOUND_MATRIX_RANGE + rand.nextInt(Utils.UPPPER_BOUND_MATRIX_RANGE);
            }
        }

        //-------fill matrixB--------
        for(i = 0; i < this.matrixB.length; i++) {
            for(j = 0; j < this.matrixB[Utils.FIRST_ROW].length; j++) {
                this.matrixB[i][j] = Utils.LOWER_BOUND_MATRIX_RANGE + rand.nextInt(Utils.UPPPER_BOUND_MATRIX_RANGE);
            }
        }
    }
}
