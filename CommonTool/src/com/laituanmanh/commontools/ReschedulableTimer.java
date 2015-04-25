package com.laituanmanh.commontools;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// TODO: Auto-generated Javadoc
/**
 * The Class ReschedulableTimer.
 */
public class ReschedulableTimer extends ScheduledThreadPoolExecutor {

	/**
	 * Instantiates a new reschedulable timer.
	 *
	 * @param arg0 Number of max task pushed to threadpool.
	 */
	public ReschedulableTimer(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/** The task. */
	private Runnable task;
	static ScheduledFuture<?> t;

	/**
	 * Schedule.
	 *
	 * @param runnable the runnable
	 * @param delay the delay
	 */
	public void schedule(Runnable runnable, long delay) {
		task = runnable;
		t= schedule(task, delay, TimeUnit.SECONDS);
	}

	/**
	 * Reschedule.
	 *
	 * @param delay the delay
	 */
	public void reschedule(long delay) {
		cancel();
		t = schedule(task, delay, TimeUnit.SECONDS);
	}

	/**
	 * Schedule interval.
	 *
	 * @param runnable the runnable
	 * @param delay the delay
	 */
	public void scheduleInterval(Runnable runnable, long delay){
		task = runnable;
		t= scheduleWithFixedDelay(task, delay, delay, TimeUnit.MILLISECONDS);
	}
	
	public void rescheduleInterval(long delay){
		cancel();
		t = scheduleWithFixedDelay(task, delay, delay, TimeUnit.MILLISECONDS);
	}

	/**
	 * Cancel.
	 */
	public void cancel(){
		//shutdownNow();
		//getRemoveOnCancelPolicy();
		t.cancel(false);
	}
}
