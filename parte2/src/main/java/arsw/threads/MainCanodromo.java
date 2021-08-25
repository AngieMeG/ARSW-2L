package arsw.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MainCanodromo {

    private static Galgo[] galgos;

    private static Canodromo can;

    private static RegistroLlegada reg = new RegistroLlegada();

    public static void main(String[] args) {
        can = new Canodromo(17, 100);
        galgos = new Galgo[can.getNumCarriles()];
        can.setVisible(true);

        //Acción del botón start
        can.setStartAction(
            new ActionListener() {
                /* Como acción, se crea un nuevo hilo que cree los hilos 'galgos', los pone a correr, y luego muestra los resultados.
                   La acción del botón se realiza en un hilo aparte para evitar bloquear la interfaz gráfica*/
                @Override
                public void actionPerformed(final ActionEvent e) {
                    ((JButton) e.getSource()).setEnabled(false);
                    new Thread() {
                        public void run() {
                            for (int i = 0; i < can.getNumCarriles(); i++) {
                                //crea los hilos 'galgos'
                                galgos[i] = new Galgo(can.getCarril(i), "" + i, reg);
                                //inicia los hilos
                                galgos[i].start();
                            }
                            for(Galgo galgo : galgos){
                                try{
                                    galgo.join();
                                } catch(InterruptedException e){
                                    e.printStackTrace();
                                }
                            }
                            can.winnerDialog(reg.getGanador(),reg.getUltimaPosicionAlcanzada() - 1); 
                            System.out.println("El ganador fue: " + reg.getGanador());
                        }
                    }.start();
                }
            }
        );

        can.setStopAction(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Galgo.stopRace();
                    System.out.println("Carrera pausada!");
                }
            }
        );

        can.setContinueAction(
            new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Galgo.resumeRace();
                    System.out.println("Carrera reanudada!");
                }
            }
        );
    }
}
