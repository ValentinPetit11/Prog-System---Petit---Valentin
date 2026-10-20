import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

private static final int TAILLE_BUFFER = 5;
private final Integer[] buffer = new Integer[TAILLE_BUFFER];


private final Semaphore placesLibres = new Semaphore(TAILLE_BUFFER);
private final Semaphore elementsDisponibles = new Semaphore(0);  
private final Semaphore mutexBuffer = new Semaphore(1);

class Producteur implements Runnable {
    
	public void run() {
        for (int i = 0; i < 10; i++) {
            int produit = genererProduit();

            try {
				placesLibres.acquire();
				mutexBuffer.acquire();
				mutexBuffer.release();  
			}catch (InterruptedException e) {
				return;
			}
        }
    }
		
}