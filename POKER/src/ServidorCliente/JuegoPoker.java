package ServidorCliente;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import MD.*;

public class JuegoPoker implements Runnable {
    private  List<Jugador> Jugadores;
    private final int NUM_CARTAS_EN_MANO;// este estilo de poker cada jugador tiene dos cartas individuales en mano y 5 comunitarias en mesa.
    private List<Carta> Mazo;
    private List<Carta> mesaCartas;
    private int currentBet;
    private int bote;
    private int minapuesta = 25;// Tener en cuenta que esto se puede meter como parametro al hilo y hacer diferentes jugadas de ciegas y subidas maximas distintas
    private int maxSubidas=150;
    private  List<Socket> sockets;   
    private List<ObjectOutputStream> los = new ArrayList<ObjectOutputStream>();
    private List<ObjectInputStream> lis = new ArrayList<ObjectInputStream>();
    private List<DecisionCompleta> ld;
    



    public JuegoPoker(List<Jugador> Jugadores, int i,List<Socket> s) {
        this.Jugadores = Jugadores;
        this.NUM_CARTAS_EN_MANO = i;
        this.sockets = s;
        int p = 0 ;
        try {
        for (Jugador j:Jugadores ){

                los.add(new ObjectOutputStream(this.sockets.get(p).getOutputStream()));

            lis.add(new ObjectInputStream(this.sockets.get(p).getInputStream()));
            p++;
        }} catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
            // Inicializar el mazo y las cartas de la mesa
            Mazo = crearMazo();
            mesaCartas = new ArrayList<>();
		try {
            int m =0;
            // Repartir las manos iniciales a los jugadores
            for (Jugador Jugador : Jugadores) {
                List<Carta> iniciarCartas = new ArrayList<>();
                for (int i = 0; i < NUM_CARTAS_EN_MANO; i++) {
                    iniciarCartas.add(Mazo.remove(0));
                }

                
                try {
					los.get(m).writeObject(iniciarCartas);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

                m++;
            }

            // Primera ronda de decisiones

            RondaDecisionyApuesta();

		
		
		  // Robar el flop (primeras tres cartas comunitarias)
		  
		  mesaCartas =  sacarCartas(3 , mesaCartas);
		   
		  m=0;
		  
		  // Enviar el flop a los jugadores
		  
		  for (Jugador Jugador : Jugadores) {
		  
		 
				 try { 
					 los.get(m).writeObject(mesaCartas); 
			
				 } catch(IOException e) { 
					 e.printStackTrace(); 
				 }
			 m++; 
		  }
		  
		  
		  RondaDecisionyApuesta();
		 
		  
		  // Robar el turn (cuarta carta comunitaria) 
		  mesaCartas = sacarCartas(1,mesaCartas);
		  
		  m=0;
		  
		  // Enviar el turn a los jugadores

		  for (Jugador Jugador : Jugadores) {
				 try { 
					 los.get(m).writeObject(mesaCartas); 
				} catch (IOException e) {  
					 e.printStackTrace(); }
				 m++; 
			  }
			  
		 
		  RondaDecisionyApuesta();
			
		  // Robar el river (quinta carta comunitaria) 
			  
			  mesaCartas =sacarCartas(1,mesaCartas);
		  
		   // Enviar el river a los jugadores 
			  m=0;
			  for (Jugador Jugador : Jugadores) {
				  
					 try { 
						 los.get(m).writeObject(mesaCartas); 
					 } catch(IOException e) { 
						 e.printStackTrace(); }
					 m++; 
				  }



			RondaDecisionyApuesta();



		//Determinamos el ganador y el dinero que ga ganado
				  //Si hay mas de un ganador se reparte el dinero entre ambos 
				 List<Integer> ganadores =  determinarGanador();
				 for (Integer i : ganadores) {
					 System.out.println(ld.get(i).getNombre());
				 }
				 RepartoBote(ganadores);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

    }
    
    //Metodo que coge las decisiones de las puesta 
    //Con ayuda de otros metodos quita a los jugadores de la lista de jugadores 
    private void RondaDecisionyApuesta() throws IOException {
    	jugarTurno();
    	ld.remove(0);
    	lis = retirarlis();
    	los = retirarlos();
    	Jugadores= retirarJug();
    	ld= retirarld();
        decidirIr();
        lis = retirarlis();
        los = retirarlos();
        Jugadores= retirarJug();
        ld= retirarld();
        System.out.println("bote"+ this.bote);
    }

    
    //Metodo para decircir que vas a hacer
    private void decidirIr() {
		// TODO Auto-generated method stub
    	try {
    		int m =0;
    		
    		DecisionCompleta dc1;
	    	for (DecisionCompleta dc : ld) {
	    		if(dc.getDecision() != Decision.RETIRARSE ) {
	    			
						los.get(m).writeObject(new DecisionCompleta(null,null,this.currentBet));
						 dc1= (DecisionCompleta)lis.get(m).readObject();
						
						if(dc1.getDecision()==Decision.VOY) {
							
							dc.setCantidad(currentBet);
							
						}else {
							dc.setDecision(Decision.RETIRARSE);
						}
					bote += dc.getCantidad();
					}
	    		m++;
	    		}
    		
    	} catch (IOException | ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		
    		}
    }

	
	//Elimina a los jugadores de la lista de los ObjectInputStream 
	private List<ObjectInputStream> retirarlis() throws IOException {
		List<ObjectInputStream> aux = new ArrayList<>();
		int i = 0;
		for (DecisionCompleta dc : ld) {
			if(dc.getDecision() != Decision.RETIRARSE) {
				
				aux.add(lis.get(i));
			}
			i++;
		}
		return aux;
	}
	
	
	//Elimina a los jugadores de la lista de los ObjectOutputStream 
	private List<ObjectOutputStream> retirarlos() throws IOException {
		List<ObjectOutputStream> aux = new ArrayList<>();
		int i = 0;
		for (DecisionCompleta dc : ld) {
			if(dc.getDecision() != Decision.RETIRARSE) {
				
				aux.add(los.get(i));
			}
			i++;
		}
		return aux;
	}
	
	
	//Elimina a los jugadores de la lista de jugadores  
	private List<Jugador> retirarJug(){
		List<Jugador> aux = new ArrayList<>();
		int i = 0;
		for (DecisionCompleta dc : ld) {
			if(dc.getDecision() != Decision.RETIRARSE) {
				aux.add(Jugadores.get(i));
			}
			i++;
		}
		return aux;
	}
	
	
	//Elimina a los jugadores de la lista de Decisiones
	private List<DecisionCompleta> retirarld(){
		List<DecisionCompleta> aux = new ArrayList<>();
		
		for (DecisionCompleta dc : ld) {
			if(dc.getDecision() != Decision.RETIRARSE) {
				aux.add(dc);
			}
			
		}
		return aux;
	}

	
	
	//Metodo para robar cartas 
	private List<Carta> sacarCartas(int numCartas,List<Carta> mc) {
        // Robar un cierto número de cartas del mazo y agregarlas a la mesa
		List<Carta> mc1 = new ArrayList<>();
		for (Carta c : mc) {
			mc1.add(c);
		}
        for (int i = 0; i < numCartas; i++) {
            mc1.add(Mazo.remove(0));
        }
        return mc1;
    }

	
	//Metodo para crear el mazo 
    private List<Carta> crearMazo() {
        // Crear un mazo de cartas
        List<Carta> Mazo = new ArrayList<>();
        for (Palo palo : Palo.values()) {
            for (Valor valor : Valor.values()) {
                Mazo.add(new Carta(palo, valor));
            }
        }
        // El metodo shuffle hace permutaciones pseudoaleatorias asi que nos sirve como barajar.
        Collections.shuffle(Mazo);
        return Mazo;
    }

    
    //Metodo para juugar 
    public void jugarTurno() {
        try {
        	int numSubidas = 0;
        	currentBet=0;
	        int m =0;
	         ld =new ArrayList<>();
	        ld.add(new DecisionCompleta(Decision.VOY,"Crupier",0));
	        int cant = 0;
	       
	        for (Jugador jugador : Jugadores) {    //Para la lista de jugadores mira a ver que decision toma 
	        	
	            DecisionCompleta decision;
				los.get(m).writeObject(ld);
				decision = (DecisionCompleta) lis.get(m).readObject();
				cant = decision.getCantidad();
	            switch (decision.getDecision()) {
	                case RETIRARSE:                 
	                    			//Si se retira se va 
	                    break;
	                case VOY:
	                    // Verificar si el jugador tiene suficiente dinero para cubrir la apuesta más alta
	                    System.out.println("La apuesta tiene que ser mayor de 25");
						if(jugador.getMonto()<numSubidas) {
	                        throw new IllegalArgumentException("No tiene suficiente dinero");
	                    }
	                    if(currentBet ==0) {
	                    	cant = 25;
	                    	currentBet = cant;
	                    }else {
	                    	 cant =currentBet;
	                    }
	                   
	                    break;
	                case APUESTA:    
	                    currentBet = Math.max(currentBet, cant);
	                    numSubidas++;
	                    if (numSubidas >= maxSubidas) {
	                        throw new IllegalStateException("No se puede superar el límite de apuestas");
	                    }
	                    break;
	                case SUBIR:
	                    if (cant <  minapuesta) {
	                        throw new IllegalArgumentException("El mínimo a apostar es: " +  minapuesta);
	                    }
	                    currentBet =currentBet +cant;
	                    
	                    numSubidas++;
	                    if (numSubidas >= maxSubidas) {
	                        throw new IllegalStateException("No se puede superar el límite de apuestas");
	                    }
	                    break;
	               
	            }
	            
	            ld.add(new DecisionCompleta(decision.getDecision(), decision.getNombre(), cant ));
	            
	           
	            m++;
	            
	        }
	    } catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
    }


    //Metodo que deterimna el ganador 
    private List<Integer> determinarGanador() {
    	List<Integer> ganadores = new ArrayList<>();
        
        Rango Rangomax = Rango.HIGH_CARD;
        int m =0;
        // Recorrer todos los jugadores y determinar cuál tiene la mano ganadora
        for (Jugador jugador : Jugadores) {
        	Mano mano;
			try {
				mano = (Mano)lis.get(m).readObject();
				Rango rango = mano.getRango();
             
            if (rango.compareTo(Rangomax) <=0) {
            	if(rango.compareTo(Rangomax)==0) {
            		ganadores.add(m);
            	}else {
            		Rangomax = rango;
            		ganadores = new ArrayList<>();
            		ganadores.add(m);
            	}
            }
            m++;
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        System.out.println(Rangomax.name());
        return ganadores;
    }
    
    
    //Reparte el dinero a los ganadores
    //Comprueba cuantos ganadores hay y el dinero del bote
    public void RepartoBote(List<Integer> ganadores) {
    	Integer cant = bote/ganadores.size();
    	for ( Integer m : ganadores) {
    		try {
				los.get(m).writeObject(cant);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    		
    	}
}







