package com.laituanmanh.commontools;


/**
 * A tool provide methods to measure memory usage at runtime.
 * <pre>
 * {@code
 * Usage example:
 * TimeMeasure.reset();
 * TimeMeasure.checkPoint();
 * ...
 * //Code bundle
 * ...
 * TimeMeasure.checkPoint();
 * System.out.println("Memory taked: " + TimeMeasure.getTimeMeasureInSec() + " s");
 * 
 * }
 * </pre>
 * @author manh
 *
 */
public class TimeMeasure {
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
			point[0] = System.currentTimeMillis();;
			first=false;
		}else{
			point[1] = System.currentTimeMillis();;
		}
	}
	
	/**
	 * Get shift of memory in byte.
	 * @return usageMemory
	 * @throws NoEndPointChecked
	 */
	public static long getTimeMeasure() throws NoEndPointChecked{
		return point[1] - point[0];
	}
	
	/**
	 * Get shift of memory in Megabyte.
	 * @return usageMemory
	 * @throws NoEndPointChecked
	 */
	public static double getUsageMemoryMeasureInSec() throws NoEndPointChecked{
		return getTimeMeasure()/1000F;
	}
	
	/**
	 * Get shift of memory in Kilobyte.
	 * @return usageMemory
	 * @throws NoEndPointChecked
	 */
	public static double getUsageMemoryMeasureInMin() throws NoEndPointChecked{
		return getTimeMeasure()/1000F/60;
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
