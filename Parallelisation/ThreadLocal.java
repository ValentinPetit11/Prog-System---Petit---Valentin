public class ThreadLocal extends Thread {

    private final int valeur;
    private final int multiplicateur;
    private final int index;

    private int resultat;

    public ThreadLocal(int valeur, int multiplicateur, int index) {
        this.valeur = valeur;
        this.multiplicateur = multiplicateur;
        this.index = index;
    }

    @Override
    public void run() {
        resultat = 0;

        for (int j = 0; j < multiplicateur; j++) {
            resultat += valeur * valeur + valeur;
        }

        System.out.println(
            "Thread " + index
            + " : traitement de " + valeur
            + " terminé : " + resultat
        );
    }

    public int getResultat() {
        return resultat;
    }
}