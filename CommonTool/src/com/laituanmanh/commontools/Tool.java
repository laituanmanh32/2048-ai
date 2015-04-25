package com.laituanmanh.commontools;

public class Tool {
	public static int[][] cloneArray(int [][] pArray){

		int[][] clArray = new int[pArray.length][pArray[0].length];

		for(int i = 0 ; i < pArray.length ; i ++){
			for(int j = 0 ; j < pArray[0].length; j++){
				clArray[i][j] = pArray[i][j];
			}
		}

		return clArray;
	}
}
