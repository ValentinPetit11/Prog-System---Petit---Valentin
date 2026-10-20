public class TestCompteur {

    public static void main(String[] args) throws InterruptedException {

        final int NOMBRE_TESTS = 50;
        final int NOMBRE_THREADS = 9;
        final int INCREMENTS_PAR_THREAD = 1111111;
        final int VALEUR_ATTENDUE = NOMBRE_THREADS * INCREMENTS_PAR_THREAD;

        for (int test = 1; test <= NOMBRE_TESTS; test++) {

            CompteurDangereux.resetCompteur();

            Thread[] threads = new Thread[NOMBRE_THREADS];

            for (int i = 0; i < NOMBRE_THREADS; i++) {
                CompteurDangereux.Incrementeur incrementeur =
                        new CompteurDangereux.Incrementeur(
                                "Thread-" + (i + 1),
                                INCREMENTS_PAR_THREAD
                        );

                threads[i] = new Thread(incrementeur);
            }

            for (int i = 0; i < NOMBRE_THREADS; i++) {
                threads[i].start();
            }

            for (int i = 0; i < NOMBRE_THREADS; i++) {
                threads[i].join();
            }

            int resultat = CompteurDangereux.getCompteur();

            if (resultat == valeurAttendue) {
                testsCorrects++;
            }

            if (resultat < valeurMin) {
                valeurMin = resultat;
            }

            if (resultat > valeurMax) {
                valeurMax = resultat;
            }
            
			double pourcentage = testsCorrects * 100.0 / NOMBRE_TESTS;

        }
    }
}
