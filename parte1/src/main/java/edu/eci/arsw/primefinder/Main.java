package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;

public class Main {

	public static void main(String[] args) throws InterruptedException{
		int nthreads  = 20;
		LinkedList<PrimeFinderThread> threads = new LinkedList<PrimeFinderThread>();
		pauseThreadsAfterDelay(threads,2000); /* Pauses all threads after 5 seconds*/
		int range = 30000000/nthreads;
		for(int i=0; i < 30000000; i+=range){
			int lowerBound = i == 0 ? 2 : i, upperBound = i+range > 30000000 ? 30000000 : i+range;
			PrimeFinderThread pft = new PrimeFinderThread(lowerBound, upperBound);
			threads.add(pft);
			pft.start();
		}
	}

	/**
	 * Pauses all threads after a given delay and wait for user confirmation to resume execution 
	 * @param threads 
	 * @param delay
	 */
	private static void pauseThreadsAfterDelay(LinkedList<PrimeFinderThread> threads, int delay){
		
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.schedule(new Runnable(){
			@Override
			public void run() {
				PrimeFinderThread.stopThreads();
				System.out.println("Se han encontrado "+PrimeFinderThread.getPrimes().size()+
				" primos hasta ahora.\nPresione Enter para continuar la ejecucion...");
				Scanner sc = new Scanner(System.in);
				sc.nextLine(); sc.close();
				PrimeFinderThread.resumeThreads();
				for(Thread thread: threads) try {thread.join();} catch (Exception e) {}
				System.out.println("Se encontraron "+PrimeFinderThread.getPrimes().size()+" primos.");
				System.out.println("Done.");
				executorService.shutdown();
			}
		}, delay, TimeUnit.MILLISECONDS);
	}
}
