class Consommateur implements Runnable {

    public void run() {
        for (int i = 0; i < 7; i++) {

            try {
                
				elementsDisponibles.acquire();

                mutexBuffer.acquire();

                Integer produit = buffer.poll();

                mutexBuffer.release();
             
                placesLibres.release();

            } catch (InterruptedException e) {
                return;
            }
        }
    }
}