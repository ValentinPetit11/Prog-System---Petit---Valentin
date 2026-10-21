import java.net.*;
import java.io.*;

public class ClientAsync {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 5000);
        System.out.println("Connecté au serveur");

        // Thread de lecture (messages du serveur)
        Thread lectureThread = new Thread(new ReceptionHandler(socket));
        lectureThread.start();

        // Thread principal = écriture (envoi au serveur)
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader clavier = new BufferedReader(
            new InputStreamReader(System.in)
        );

        String texte;
        while ((texte = clavier.readLine()) != null) {
            out.println(texte);
        }

        socket.close();
    }
}

class ReceptionHandler implements Runnable {
    private Socket socket;

    public ReceptionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			PrintWriter out = new PrintWriter(client.getOutputStream(), true);
			String ligne;
			while ((ligne = in.readLine()) != null) {
				System.out.println("Reçu : " + ligne);
				out.println("ECHO: " + ligne);
			}
			System.out.println("Client déconnecté");
			in.close();
			out.close();
			client.close();
		}catch (Exception erreur) {
		}finally {
			try { 
				client.close(); 
			} catch (IOException ignored) {} {
			}
		}
    }
}