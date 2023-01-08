package MD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mano implements Comparable<Mano>, Serializable {
	private final List<Carta> cartas;
	private final List<Carta> todasCartas;
	private final List<Carta> cartamesa;

	
	//Crea una mano y las ordena de menor a mayor
	//Lo ordenamos para que luego sea mas facil usar los metodos
	public Mano(List<Carta> cartas, List<Carta> cartamesa) {
		this.cartas = cartas;

		this.cartamesa = cartamesa;
		Collections.sort(this.cartamesa);
		this.todasCartas = cartamesa;
		this.todasCartas.add(cartas.get(0));
		this.todasCartas.add(cartas.get(1));

		Collections.sort(this.todasCartas);
		for (Carta c : todasCartas) {
			c.mostrar();
		}
	}

	
	//Devuelve el rango segun la mano que tengas
	//Este metodo va llamando del rango mayor al menor de ellos para comprobar todos los casos 
	public Rango getRango() {
		if (esRoyalFlush()) {
			return Rango.ROYAL_FLUSH;
		} else if (esStraightFlush()) {
			return Rango.STRAIGHT_FLUSH;
		} else if (esFourOfAKind()) {
			return Rango.FOUR_OF_A_KIND;
		} else if (esFullHouse()) {
			return Rango.FULL_HOUSE;
		} else if (esFlush()) {
			return Rango.FLUSH;
		} else if (esStraight()) {
			return Rango.STRAIGHT;
		} else if (esThreeOfAKind()) {
			return Rango.THREE_OF_A_KIND;
		} else if (esTwoPair()) {
			return Rango.TWO_PAIR;
		} else if (esPair()) {
			return Rango.PAIR;
		} else {
			return Rango.HIGH_CARD;
		}
	}

	// Verifica si la Mano es un royal flush (9,10, J, Q, K, del mismo Palo)
	private boolean esRoyalFlush() {
		return esStraightFlush() && EscaleraColor(todasCartas).get(0).getValor() == Valor.NUEVE;
	}

	
	// Verfica si la Mano es un straight flush (five cards of the same Palo in
	// sequence)
	private boolean esStraightFlush() {
		return EscaleraColor(todasCartas).size()==5;
	}
	
	//Metodo auxiliar que te mira si tienes escalera de color 
	//Devolvemos una lista para que este mismo metodo nos sirva para el Royal Flush
	public List<Carta> EscaleraColor(List<Carta> aux){
	    if(cartasSeguidas(aux)==5){      //Comprobamos si tiene cinco cartas seguidas
	        List<Carta> aux1= new ArrayList<Carta>();
	        int i=0;
	        int num =1;
	        while(i<6 && num!=5){
	                if(aux.get(i).getValor().ordinal()+1==aux.get(i+1).getValor().ordinal() || aux.get(i).getValor().ordinal()==aux.get(i+1).getValor().ordinal()){
	                    if(aux.get(i).getValor()!=aux.get(i+1).getValor()){
	                    aux1.add(aux.get(i));     								//Añadimos las cartas seguidas en una nueva lista para mirar luego 
	                    num++;													//mirar si son del mismo color 
	                }
	                }else{
	                    aux1= new ArrayList<Carta>();
	                    num =1;
	                }
	                i++;
	        }
	        aux1.add(aux.get(i));
		     i=0;
		    while(i<5){
		            if(aux1.get(i).getPalo()!=aux1.get(0).getPalo()) {
	                return new ArrayList();  								//Si tiene alguna carta con palo diferente devueleve una lista vacia
		            }
		            i++;
		    }
		     return aux1;			//Y si no devuelve la lista entera 

	    }else{
	        return new ArrayList();
	    }

	}
	
	// Verfica si la Mano es un four of a kind (four cards of the same Valor)
	private boolean esFourOfAKind() {
		return hasOfAKind(4);  //Comprueba si tiene 4 cartas iguales
	}

	// Verfica si laMano es un full house (three of a kind and a pair)
	private boolean esFullHouse() {
		
		if (hasOfAKind(3)) {		//Comprobamos si tiene un trio 
			List<Carta> aux = new ArrayList<>();
			for (Carta c : todasCartas) {
				aux.add(c);							//Creamos una lista auxiliar para luego poder borrar el trio
			}									
			boolean esta = false;
			int i = 0;
			while (esta == false) {				
				if (aux.get(i).getValor() == aux.get(i + 1).getValor()
						&& aux.get(i).getValor() == aux.get(i + 2).getValor()) {
					aux.remove(i + 2);												//Borramos el trio ya que si no el mismo trio tambien era considerado
					aux.remove(i + 1);												// una pareja entonces al borrarlo se nos queda el resto de cartas
					aux.remove(i);													//entre ellas si hay alguna pareja 
					esta = true;
				}
				i++;
			}
			
			//Comprobamos si tiene una pareja con la lista de la que hemos borrado el trio
			for (int p = 0; i < 3; i++) {
				boolean found = true;
				Valor currentValor = aux.get(i).getValor();
				for (int j = 1; j < 2; j++) {
					if (aux.get(i + j).getValor() != currentValor) {
						found = false;
						break;
					}
				}
				if (found) {
					return true;
				}
			}

		}

		return false;

	}

	// Verfica si laMano es un flush (five cards of the same Palo)
	private boolean esFlush() {
		if(numeroPalo(todasCartas, Palo.CORAZON)==5|| numeroPalo(todasCartas, Palo.DIAMANTE)==5||numeroPalo(todasCartas, Palo.PICAS)==5 ||numeroPalo(todasCartas, Palo.TREBOL)==5) {
			return true;
		}
		return false;
	}

	// Verfica si laMano es un straight (five cards in sequence)
	private boolean esStraight() {
		if (cartasSeguidas(todasCartas) == 5) {
			return true;
		}
		return false;
	}

	// Verfica si laMano es un three of a kind (three cards of the same Valor)
	private boolean esThreeOfAKind() {
		return hasOfAKind(3);
	}

	// Verfica si laMano es un two pair (two pairs of cards with different Valors)
	private boolean esTwoPair() {
		int numPairs = 0;
		for (int i = 0; i < 6; i++) {
			if (todasCartas.get(i).getValor() == todasCartas.get(i + 1).getValor()) {
				numPairs++;
			}
		}
		return numPairs == 2;
	}

	
	// Verfica si laMano es un pair (two cards of the same Valor)
	private boolean esPair() {
		return hasOfAKind(2);
	}

	// Verfica si la Mano tiene num cartas de igual Valor
	private boolean hasOfAKind(int num) {
		for (int i = 0; i < 7 - num + 1; i++) {
			boolean found = true;
			Valor currentValor = todasCartas.get(i).getValor();
			for (int j = 1; j < num; j++) {
				if (todasCartas.get(i + j).getValor() != currentValor) {
					found = false;
					break;
				}
			}
			if (found) {
				return true;
			}
		}
		return false;
	}

	//Verifica cuantas cartas seguidas(en escalera) tiene en la mano)
	public int cartasSeguidas(List<Carta> aux) {
		int numcartseguidas = 1;
		int i = 0;

		while (i < 6 && numcartseguidas !=5) {
			if (aux.get(i).getValor().ordinal() + 1 == aux.get(i + 1).getValor().ordinal()
					|| aux.get(i).getValor().ordinal() == aux.get(i + 1).getValor().ordinal()) {
				if (aux.get(i).getValor().ordinal() != aux.get(i + 1).getValor().ordinal()) {
					numcartseguidas++;
				}
			} else {
				numcartseguidas = 1;
			}
			i++;
		}

		return numcartseguidas;
	}
	
	// Devolvemos el numero de veces que se repite un palo
	public int numeroPalo(List<Carta> aux, Palo p){
	    int numcartpalo=0;
	    int i=0;
	    while(i<7){
	            if(aux.get(i).getPalo()==p) {
	                numcartpalo++;
	            }
	            i++;
	    }
	    return numcartpalo;

	}

	@Override
	public int compareTo(Mano o) {
		// Auto-generated method stub
		return 0;
	}
}
