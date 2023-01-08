package MD;

import java.io.Serializable;
import java.util.Scanner;

public class Carta implements Comparable<Carta>,Serializable {
    private final Palo palo;
    private final Valor valor;

    //Crea una carta con un palo y un valor 
    public Carta(Palo palo, Valor valor) {
        this.palo = palo;
        this.valor = valor;
    }

    //Devuelve el Palo de la carta
    public Palo getPalo() {
        return palo;
    }

    //Devuelve el valor de la carta
    public Valor getValor() {
        return valor;
    }

    
    //Compara dos valores de carta y devuelve verdad si son iguales y falso en caso contrario
    @Override
    public int compareTo(Carta otra) {
        return this.valor.compareTo(otra.valor);
    }

    //Muestra las cartas 
    public void mostrar(){
        System.out.print("||"+this.getValor()+" de "+this.getPalo()+" ||");

    }
}



