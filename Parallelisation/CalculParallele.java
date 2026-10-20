public class CalculParallele {
	private static final int[] DONNEES = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    private static final int MULTIPLICATEUR = 1000;
	
    public static void main(String[] args) {
		ThreadLocal[] threads = new ThreadLocal[DONNEES.length];
		long debut = System.nanoTime();
        long sommeTotal = 0;
		
		for (int i = 0; i < DONNEES.length; i++) {
			threads[i] = new ThreadLocal(
				DONNEES[i], MULTIPLICATEUR, i
			);
			threads[i].start();
		}

	// 2. Attendre la fin de chaque thread et récupérer les résultats
		for (int i = 0; i < DONNEES.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}

			sommeTotal += threads[i].getResultat();
		}
	}
	
	
}