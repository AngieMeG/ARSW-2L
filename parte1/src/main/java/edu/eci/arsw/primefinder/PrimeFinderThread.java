package edu.eci.arsw.primefinder;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrimeFinderThread extends Thread{

	int a,b;
	private static AtomicBoolean stopped = new AtomicBoolean(false);
	private static List<Integer> primes = Collections.synchronizedList(new LinkedList<Integer>());
	
	public PrimeFinderThread(int a, int b) {
		super();
		this.a = a;
		this.b = b;
	}

	/**
	 * Find all primes in a range
	 */
	public void run(){
		for (int i=a;i<=b;i++){	
			synchronized (stopped){
				while (stopped.get()){
					try {stopped.wait();}
					catch (Exception e) {}
				}
			}
			if (isPrime(i)) primes.add(i);			
		}
	}
	
	/**
	 * Check if the given number it's a prime
	 * @param n Number to be checked
	 * @return True if the number is a prime, false otherwise
	 */
	boolean isPrime(int n) {
	    if (n%2==0 && n!=2) return false;
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0) return false;
	    }
	    return true;
	}

	public static List<Integer> getPrimes() {
		return primes;
	}
	
	/**
	 * Thread-safe function to add a new prime 
	 * @param i the new prime to be added
	 */
	synchronized void addPrime(int i){
		primes.add(i);
	}

	/**
	 * Changes stopped flag state to True and notify this to all threads waiting 
	 */
	public static void stopThreads(){
		synchronized (stopped){
			stopped.set(true);
		}
	}

	/**
	 * Changes stopped flag state to false and notify this to all threads waiting 
	 */
	public static  void resumeThreads() {
		synchronized (stopped) {
		  stopped.set(false);
		  stopped.notifyAll();
		}
	}
}
