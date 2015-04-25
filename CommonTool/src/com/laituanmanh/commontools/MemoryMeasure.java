package com.laituanmanh.commontools;

/**
 * A tool provide methods to measure memory usage at runtime.
 * <pre>
 * {@code
 * Usage example:
 * MemoryMeasure.reset();
 * MemoryMeasure.checkPoint();
 * ...
 * //Code bundle
 * ...
 * MemoryMeasure.checkPoint();
 * System.out.println("Memory taked: " + MemoryMeasure.getUsageMemoryMeasureInMB() + " mb");
 * 
 * }
 * </pre>
 * @author manh
 *
 */
public class MemoryMeasure {
	private static final Runtime runtime = Runtime.getRuntime();
	private static long[] point = new long[2];
	private static boolean first = true;
	
	/**
	 * Reset memory of measure.
	 */
	public static void reset(){
		first = true;
		point[0] = 0;
		point[1] = 0;
	}
	
	/**
	 * Set checkpoint. It use the first checkpoint and last checkpoint
	 * to measure.
	 */
	public static void checkPoint(){
		if(first){
			point[0] = runtime.totalMemory() - runtime.freeMemory();
			first=false;
		}else{
			long tmp = runtime.totalMemory() - runtime.freeMemory();
			point[1] = (point[1] < tmp ? tmp : point[1]);
		}
	}
	
	/**
	 * Get shift of memory in byte.
	 * @return usageMemory
	 * @throws NoEndPointChecked
	 */
	public static long getUsageMemoryMeasure() throws NoEndPointChecked{
		return point[1] - point[0];
	}
	
	/**
	 * Get shift of memory in Megabyte.
	 * @return usageMemory
	 * @throws NoEndPointChecked
	 */
	public static double getUsageMemoryMeasureInMB() throws NoEndPointChecked{
		return (double)getUsageMemoryMeasure()/1024/1024;
	}
	
	/**
	 * Get shift of memory in Kilobyte.
	 * @return usageMemory
	 * @throws NoEndPointChecked
	 */
	public static double getUsageMemoryMeasureInKB() throws NoEndPointChecked{
		return (double)getUsageMemoryMeasure()/1024/1024;
	}
	
	public class NoEndPointChecked extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public String toString() {
			
			return "No Endpoint have checked.";
		}
	}

}
