public class CalculSequentiel {

    private static final int[] DONNEES =
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    private static final int MULTIPLICATEUR = 1000;

    public static void main(String[] args) {
        System.out.println("=== Calcul Séquentiel ===");

        long debut = System.nanoTime();
        long sommeTotal = 0;

        for (int i = 0; i < DONNEES.length; i++) {
            ThreadLocal thread = new ThreadLocal(
                DONNEES[i], MULTIPLICATEUR, i
            );

            thread.start();

            try {
                thread.join(); // Attendre avant de passer au suivant
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            sommeTotal += thread.getResultat();

            System.out.println(
                "Traitement de " + DONNEES[i]
                + " terminé : " + thread.getResultat()
            );
        }

        long fin = System.nanoTime();

        System.out.println("Somme totale : " + sommeTotal);
        System.out.println("Temps : " + (fin - debut) / 1_000_000.0 + " ms");
    }
}