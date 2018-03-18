# concurrent-matrix-multiplication
### A multi-threaded app which caculates the multiplication of two matrices composed of random integers. The app was written in Java and runs on [JVM](https://java.com/en).

To run the app:

1. Make sure you have [JRE](http://www.oracle.com/technetwork/java/javase/downloads/jre9-downloads-3848532.html) or [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk9-downloads-3848520.html) installed. 
2. In terminal run `cd src && javac *.java` and `java MatrixMain x y z` in order to calculate multiplication of matrices A, B where `x` is the number of rows of matrix A, `y` is the number of columns of matrix A and `z` is the number of columns of matrix B.

## Main challenge ##

The main challenge was to calculate the results concurrently while having a limited number of threads as well as having to display calculation results (each cell in the resulting matrix) in specific order.

