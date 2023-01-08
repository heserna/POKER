package ServidorCliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import MD.*;

public class PokerServer {
    private static final int NumCartasEnMano= 2;
    private static final int NumCartasEnMesa = 5;
    private static final int NumJugadores = 3;//4;

    public static void main(String[] args) {
        // Crear un socket de servidor para escuchar conexiones de jugadores
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5555);
            
            // Crear una lista de jugadores
            List<Jugador> Jugadores = new ArrayList<>();
            
            //Creamos una lista de servidores 
            List<Socket> sockets = new ArrayList<>();
            while(true) {
                try {
                    // Esperar a que se conecten NumJugadores clientes
                    for (int i = 0; i < NumJugadores; i++) {
                        //Aceptamos las conexiones
                        Socket socket = serverSocket.accept();
                        //Y las añadimos a la lista 
                        sockets.add(socket);
                        //Creamos jugador
                        Jugador Jugador = new Jugador();
                        //Y lo añadimos a la lista 
                        Jugadores.add(Jugador);
                    }
                    // Iniciar el hilo del juego
                    new Thread(new JuegoPoker(Jugadores, NumCartasEnMano, sockets)).start();

                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


