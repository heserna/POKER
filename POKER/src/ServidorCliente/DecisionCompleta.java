package ServidorCliente;

import java.io.Serializable;

public class DecisionCompleta implements Serializable{
	private Decision decision;
	private String nombre;
	private int cantidad;
	
	//Crea una 
	public DecisionCompleta(Decision decision, String nombre, int cantidad) {
		super();
		this.decision = decision;
		this.nombre = nombre;
		this.cantidad = cantidad;
	}
	public Decision getDecision() {
		return decision;
	}
	public void setDecision(Decision decision) {
		this.decision = decision;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	 public  void mostrardecisionCompleta() {
		 if(this.getCantidad()!= 0) {
	    	System.out.println("Jugador: "+ this.getNombre()+" toma decision: " + this.getDecision()+ " con cantidad: "+this.getCantidad());
		 }else {
			 System.out.println("Jugador: "+ this.getNombre()+" toma decision: " + this.getDecision());
		 }
	    	
	    }

}
