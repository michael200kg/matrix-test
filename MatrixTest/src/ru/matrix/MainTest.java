package ru.matrix;

import java.time.Duration;
import java.time.LocalDateTime;

public class MainTest {
    
	public static void main(String[] args) {
    	
    	int[][] a = MatrixUtil.create(1000);
    	int[][] b = MatrixUtil.create(1000);

    	LocalDateTime time = LocalDateTime.now();   	
    	int[][] c = MatrixUtil.singleThreadMultiply(a, b);
    	Duration duration = Duration.between(time,LocalDateTime.now());
        System.out.println("Duration single thread = "+duration.toMillis());
        
    	time = LocalDateTime.now();   	
    	int[][] d = MatrixUtil.multiThreadMultiply(a, b);
    	duration = Duration.between(time,LocalDateTime.now());
        System.out.println("Duration multi thread = "+duration.toMillis());       
    
        System.out.println("Equals = "+MatrixUtil.compare(c, d));
        
/*    	
        for(int i=0;i<a.length;i++) {
        	for(int j=0;j<a.length;j++) {
        		System.out.print(a[i][j]+ " ");  
        	}
        	System.out.println("");
        }       
        System.out.println("");
        for(int i=0;i<a.length;i++) {
        	for(int j=0;j<a.length;j++) {
        		System.out.print(b[i][j]+ " ");  
        	}
        	System.out.println("");
        }         
        System.out.println("Equals = "+MatrixUtil.compare(c, d));
        
        for(int i=0;i<a.length;i++) {
        	for(int j=0;j<a.length;j++) {
        		System.out.print(c[i][j]+ " ");  
        	}
        	System.out.println("");
        }
        System.out.println("");
        for(int i=0;i<a.length;i++) {
        	for(int j=0;j<a.length;j++) {
        		System.out.print(d[i][j]+ " ");  
        	}
        	System.out.println("");
        }        
    */
    }	
}
