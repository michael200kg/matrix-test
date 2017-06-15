package ru.matrix;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class MatrixTest extends Assert {
   
	int[][] a;
	int[][] b;
	
	int[][] etalon;
	
	@Before
	public void beforeTestMultiplies() {
		a = MatrixUtil.create(1000);
		b = MatrixUtil.create(1000);
		etalon = MatrixUtil.singleThreadMultiply(a, b);
	}
	
	@Test
	public void testMultiplies() {
		
		int[][] d = MatrixUtil.multiThreadMultiply(a, b);
		assertTrue("multiThreadMultiply doesnt match etalon!", MatrixUtil.compare(d, etalon));
		
		d = MatrixUtil.streamMultiply(a, b);
		assertTrue("streamMultiply doesnt match etalon!", MatrixUtil.compare(d, etalon));
		
		d = MatrixUtil.stream1Multiply(a, b);
		assertTrue("stream1Multiply doesnt match etalon!", MatrixUtil.compare(d, etalon));		
	}
	
	@After
	public void afterTestMultiplies() {
		a=b=etalon=null;	
	}
	
}
