package ru.matrix;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.IntStream;

public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB) throws InterruptedException, ExecutionException {
        return singleThreadMultiply(matrixA, matrixB);
    }

    public static int[][] stream1Multiply(int[][] matrixA, int[][] matrixB) {

        final int matrixSize = matrixA.length;
        final int[][] matrixBrev = new int[matrixSize][matrixSize];

        for (int r = 0; r < matrixSize; r++) {          
           for (int k = 0; k < matrixSize; k++) {
              matrixBrev[k][r] = matrixB[r][k];
           }
        }    

        return Arrays.stream(matrixA).parallel().map(arr->getRow(arr,matrixBrev)).toArray(int[][]::new);

    }   
    
    public static int[] getRow(int[] thisRow, int[][] matrixBrev) {
        
    	int matrixSize = matrixBrev.length;
    	int[] columnret = new int[matrixSize];
    	for (int r = 0; r < matrixSize; r++) {          
           int sum = 0;
           for (int k = 0; k < matrixSize; k++) {
              sum += thisRow[k] * matrixBrev[r][k];
           }
           columnret[r] = sum;
       }
    return columnret;
}        
    
    
    public static int[][] streamMultiply(int[][] matrixA, int[][] matrixB) {

         final int matrixSize = matrixA.length;
         final int[][] matrixC = new int[matrixSize][matrixSize];
         final int[][] matrixBrev = new int[matrixSize][matrixSize];

         for (int r = 0; r < matrixSize; r++) {          
            for (int k = 0; k < matrixSize; k++) {
               matrixBrev[k][r] = matrixB[r][k];
            }
         }    
             
         IntStream.range(0, matrixSize).boxed().parallel().forEach(j->{matrixC[j]=getRow(j,matrixA,matrixBrev);}); 

         return matrixC;


     }    
    
    public static int[] getRow(int row,int[][] matrixA, int[][] matrixBrev) {
        
    	int matrixSize = matrixA.length;
    	int[] columnret = new int[matrixSize];
    	for (int r = 0; r < matrixSize; r++) {          
           int sum = 0;
           int thisRow[] = matrixA[row];
           for (int k = 0; k < matrixSize; k++) {
              sum += thisRow[k] * matrixBrev[r][k];
           }
           columnret[r] = sum;
       }
    return columnret;
}    
    
    
    public static int[][] multiThreadMultiply(int[][] matrixA, int[][] matrixB) {
        
    	final int matrixSize = matrixA.length;
        
    	ExecutorService executor = Executors.newWorkStealingPool();

        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[][] matrixBrev = new int[matrixSize][matrixSize];
        
        
       Future<int[]>[] future = new Future[matrixSize];

        try {
            for (int r = 0; r < matrixSize; r++) {          
                for (int k = 0; k < matrixSize; k++) {
                	matrixBrev[k][r] = matrixB[r][k];
                }
            }    
  
            for (int j = 0; j < matrixSize ; j++) {
            	future[j] = executor.submit(new MultiplyCallable(matrixA,matrixBrev,j));
            }


            
            for (int j = 0; j < matrixSize ; j++) {
                 matrixC[j] = future[j].get();
            }

            
           
        }
        catch (IndexOutOfBoundsException e)  {
        	e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        } 
        catch (ExecutionException e) {
            e.printStackTrace();
        }       


       executor.shutdown();

        return matrixC;


    }


    public static class MultiplyCallable implements Callable<int[]> {

        private int[][] matrixA;
        private int[][] matrixBrev;
        private int row;
        private int matrixSize;

        private int[] columnret;      
        
        public MultiplyCallable(int[][] matrixA, int[][] matrixBrev, int row) {
            this.matrixA=matrixA;
            this.matrixSize=matrixA.length;
            this.row=row;
            columnret = new int[matrixSize];
            this.matrixBrev = matrixBrev;
        
        }

        @Override
        public int[] call() {
                for (int r = 0; r < matrixSize; r++) {          
                   int sum = 0;
                   int thisRow[] = matrixA[row];
                   for (int k = 0; k < matrixSize; k++) {
                      sum += thisRow[k] * matrixBrev[r][k];
                   }
                   columnret[r] = sum;
               }
            return columnret;
        }
    }


    public static class MultiplyInStream  {

        private int[][] matrixA;
        private int[][] matrixBrev;
        private int row;
        private int matrixSize;

        private int[] columnret;      
        
        public MultiplyInStream(int[][] matrixA, int[][] matrixBrev, int row) {
            this.matrixA=matrixA;
            this.matrixSize=matrixA.length;
            this.row=row;
            columnret = new int[matrixSize];
            this.matrixBrev = matrixBrev;
        
        }

        public int[] getRow() {
                for (int r = 0; r < matrixSize; r++) {          
                   int sum = 0;
                   int thisRow[] = matrixA[row];
                   for (int k = 0; k < matrixSize; k++) {
                      sum += thisRow[k] * matrixBrev[r][k];
                   }
                   columnret[r] = sum;
               }
            return columnret;
        }
    }    
    

    
    
    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[] column = new int[matrixSize];

        try {
            for (int j = 0; ; j++) {
                for (int k = 0; k < matrixSize; k++) {
                    column[k] = matrixB[k][j];
                }
                for (int i = 0; i < matrixSize; i++) {
                    int thisRow[] = matrixA[i];
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += thisRow[k] * column[k];
                    }
                    matrixC[i][j] = sum;
                }
            }
        }
        catch (IndexOutOfBoundsException ignored) { }

        return matrixC;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
 