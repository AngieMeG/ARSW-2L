package arsw.threads;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Un galgo que puede correr en un carril
 * @author rlopez
 */
public class Galgo extends Thread {
	private int paso;
	private Carril carril;
	RegistroLlegada regl;
	static AtomicBoolean isPaused = new AtomicBoolean(false);

	public Galgo(Carril carril, String name, RegistroLlegada reg) {
		super(name);
		this.carril = carril;
		paso = 0;
		this.regl=reg;
	}

	public void corra() throws InterruptedException {
		while (paso < carril.size()) {
			synchronized (isPaused){
				while (isPaused.get()) isPaused.wait();
			}			
			Thread.sleep(100);
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);
			if (paso == carril.size()) {	
				synchronized (regl){
					int ubicacion=regl.getUltimaPosicionAlcanzada();
					carril.finish(ubicacion);
					regl.setUltimaPosicionAlcanzada(ubicacion+1);
					System.out.println("El galgo "+this.getName()+" llego en la posicion "+ubicacion);
					if (ubicacion==1) regl.setGanador(this.getName());					
				}
			}
		}
	}

	@Override
	public void run() {
		try {
			corra();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Changes isPaused flag state to True and notify this to all threads waiting 
	 */
	public static void stopRace(){
		synchronized (isPaused){
			isPaused.set(true);
		}
	}

	/**
	 * Changes isPaused flag state to false and notify this to all threads waiting 
	 */
	public static void resumeRace() {
		synchronized (isPaused) {
		  isPaused.set(false);
		  isPaused.notifyAll();
		}
	}
}
