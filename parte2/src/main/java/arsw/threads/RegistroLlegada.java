package arsw.threads;

import java.util.concurrent.atomic.AtomicInteger;

public class RegistroLlegada {

	private AtomicInteger ultimaPosicionAlcanzada = new AtomicInteger(1);

	private String ganador=null;
	
	public String getGanador() {
		return ganador;
	}

	public void setGanador(String ganador) {
		this.ganador = ganador;
	}

	synchronized public int getUltimaPosicionAlcanzada() {
		return ultimaPosicionAlcanzada.get();
	}

	synchronized public void setUltimaPosicionAlcanzada(int ultimaPosicionAlcanzada) {
		this.ultimaPosicionAlcanzada.set(ultimaPosicionAlcanzada);
	}
	
}
