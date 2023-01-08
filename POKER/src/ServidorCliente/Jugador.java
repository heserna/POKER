package ServidorCliente;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import MD.*;

public class Jugador {

    private static int monto;	// monto es la cantidad de dinero que tiene disponible el jugador.
    private static int apuesta;	//cantidad de dinero que apuesta la persona 
    private static List<Carta> cartamano ;
    private static List<Carta> mesa;
	private static  String nombre;
   

    public Jugador(){
        monto=1000;
        cartamano = new ArrayList<>();
        
    }

    public static  void main(String args[]) {
    	
    	//Introducimos el nombre
    	System.out.println("Introduce el nombre");
    	Scanner s= new Scanner(System.in);
    	nombre =s.nextLine();
    	
    	
        try (Socket socket =new Socket("localhost", 5555)){
        	
        	//Creamos el ObjectInputStream y el ObjectOutputStream 
            ObjectInputStream dis=new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream dos=new ObjectOutputStream(socket.getOutputStream());
            
            //Creamos una lista de decisiones para saber las decisiones del resto de jugadores 
            List<DecisionCompleta> ld =new ArrayList<>();
            
            //Nos reparten dos cartas y a las añadimos a cartamano 
            cartamano = (List<Carta>) dis.readObject();
            System.out.println("Cartas de tu mano: \n");
            mostrarCartas(cartamano);
           
           
            //muestra las decisiones
            ld=(List<DecisionCompleta>)dis.readObject();
            System.out.println("Decisiones del resto de compañeros");
            mostrarDecisiones(ld);
          
            //Llamamos a la funcion decir Apuesta para saber que decision hace el jugador 
            jugadorDecideyApuesta(dos,dis);
           
           
            //Primera ronda: FLOP -- muestra 3 cartas del monton 
            System.out.println();
            System.out.println();
            System.out.println("Flop");
            mesa = (List<Carta>) dis.readObject();
            System.out.println("Cartas de tu mano: \n");
            mostrarCartas(cartamano);
            System.out.println("Cartas de la mesa : \n");
            mostrarCartas(mesa);
         
            ld=(List<DecisionCompleta>)dis.readObject();
            System.out.println();
            System.out.println("Decisiones del resto de compañeros");
            mostrarDecisiones(ld);
            jugadorDecideyApuesta(dos,dis);
          
          
            //Segunda ronda: TURN---muestra una carta mas
            //Turn
            System.out.println();
         	System.out.println();
         	System.out.println("Turn");
         	mesa = (List<Carta>) dis.readObject();
         	System.out.println("Cartas de tu mano: \n");
         	mostrarCartas(cartamano);
         	System.out.println("Cartas de la mesa : \n");
         	mostrarCartas(mesa);
         
         
        
         	ld=(List<DecisionCompleta>)dis.readObject();
         	System.out.println();
         	System.out.println("Decisiones del resto de compañeros");
         	mostrarDecisiones(ld);
         	jugadorDecideyApuesta(dos,dis);
         
         
         	//Tercera ronda: River---muestra una carta mas
         	//River
         	System.out.println();
         	System.out.println();
         	System.out.println("River");
         	mesa = (List<Carta>) dis.readObject();
         	System.out.println("Cartas de tu mano: \n");
         	mostrarCartas(cartamano);
         	System.out.println("Cartas de la mesa : \n");
         	mostrarCartas(mesa);
        
        
         	ld=(List<DecisionCompleta>)dis.readObject();
         	System.out.println();
         	System.out.println("Decisiones del resto de compañeros");
         	mostrarDecisiones(ld);
         	jugadorDecideyApuesta(dos,dis);
        
         	dos.writeObject(new Mano(cartamano,mesa));
        
         	
         	//Mandamos un mensaje al ganador de que ha ganado y al perdedor que ha perdidio 
         	try{
         		int cant = (Integer)dis.readObject();
         		System.out.println(" Has ganado: " + cant);
         		System.out.println("Enorabuena !!!");
         	}catch (IOException e) {
         		System.out.println("Lo siento, has perdidio");
         	} 
      
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    
	   //Metodo para que cada jugadr decida que apuesta hacer y lo manda al servidor 
	private static void jugadorDecideyApuesta(ObjectOutputStream dos, ObjectInputStream dis) {
		
	    try {
	    	Decision d = recibirDecisionJugador();   
	        DecisionCompleta dc = new DecisionCompleta(d, nombre, apuesta);
			dos.writeObject(dc);
			dos.flush();
			jugadorDecidirIr(dis,dos);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	}
	
		//Metodo para decir una apuesta despues despues de apsotar 
	    private static void jugadorDecidirIr(ObjectInputStream dis, ObjectOutputStream dos) {
			// TODO Auto-generated method stub
	    	try {
				DecisionCompleta apuestaActual = (DecisionCompleta)dis.readObject();
				System.out.println("La apuesta actual esta en:" + apuestaActual.getCantidad());
				System.out.println("¿Qué desea hacer?");
		        System.out.println("(1) Retiro");
		        System.out.println("(2) Voy");
		        
		        Scanner scanner = new Scanner(System.in);
		        int decision = Integer.parseInt(scanner.nextLine());
		        
		        switch (decision) {
		            case 1:
		            	apuesta =0;
		                dos.writeObject(new DecisionCompleta(Decision.RETIRARSE, nombre, 0));
		            case 2:
		            	apuesta =0;
		            	dos.writeObject(new DecisionCompleta(Decision.VOY,nombre, 0));
		        }
		        
				
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	    
	    
	    //Metodo para mandarle las caras al servidor 
		public void mandarCartas(List<Carta> Cartas, ObjectOutputStream os)  {
	        // Enviar una lista de cartas del jugador
	        try {
	            os.writeObject(Cartas);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
		//Metodo para recibir la man 
	    public Mano recibirMano(ObjectInputStream is) {
	        // Recibir la mano final del jugador
	        try {
	            return (Mano) is.readObject();
	        } catch (ClassNotFoundException | IOException e) {
	
	            e.printStackTrace();
	        }
	        return null;
	    }
	
		
	    public int getMonto() {
	        return monto;
	    }
	
	    public int getApuesta() {
	        monto = monto -apuesta;
	        return apuesta;
	    }
	
	    public void ponerApuesta(int cantidad) {
	        if (cantidad > monto) {
	            throw new IllegalArgumentException("fondos insuficientes");
	        }
	        monto-= cantidad;
	        apuesta += cantidad;
	    }
	
	    
	
	    public static  void mostrarCartas(List<Carta>  l){
	        for (Carta c :  l){
	            c.mostrar();
	        }
	    }
	
	
	    
	    //Metodo para que un jugador decida que hacer 
	    private static Decision recibirDecisionJugador() {
	    	
	        // Mostrar al jugador sus opciones de decisión y pedirle que elija una
	        System.out.println("¿Qué desea hacer?");
	        System.out.println("(1) Retiro");
	        System.out.println("(2) Voy");
	        System.out.println("(3) Apostar");
	        System.out.println("(4) Subir");
	        
	        System.out.print("Ingresa tu elección: ");
	
	        // Leer la elección del jugador y devolver la decisión correspondiente
	        Scanner scanner = new Scanner(System.in);
	        int decision = Integer.parseInt(scanner.nextLine());
	        
	        switch (decision) {
	            case 1:
	            	apuesta =0;
	                return Decision.RETIRARSE;
	            case 2:
	            	apuesta =0;
	                return Decision.VOY;
	            case 3:
	                apuesta = recibirApuestaDeJugador();                
	                return Decision.APUESTA;
	            case 4:
	                apuesta = recibirApuestaDeJugador();
	                return Decision.SUBIR;
	           
	            default:
	                throw new IllegalArgumentException("Elección inválida");
	        }
	    }
	
	    private static int recibirApuestaDeJugador() {
	        // Pedir al jugador que ingrese la cantidad de dinero a apostar
	        System.out.print("Ingrese el dinero a apostar: ");
	
	        // Leer la cantidad de dinero a apostar y devolverla
	        Scanner scanner = new Scanner(System.in);
	        return scanner.nextInt();
	    }
	
	   
	    //Metodo para mostrar las decisiones por pantalal
	    public static void mostrarDecisiones(List<DecisionCompleta> ds) {
	    	for (DecisionCompleta d:ds) {
	    		d.mostrardecisionCompleta();
	    	}
	    	
	    }
	    
}
