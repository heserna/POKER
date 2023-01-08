package ServidorCliente;

import java.util.ArrayList;
import java.util.List;

import MD.Carta;
import MD.Mano;
import MD.Palo;
import MD.Valor;

public class prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Carta ReyC = new Carta(Palo.CORAZON, Valor.REY);
		Carta ReyT = new Carta(Palo.TREBOL, Valor.REY);
		Carta ReyD = new Carta(Palo.DIAMANTE, Valor.REY);
		Carta ReyP = new Carta(Palo.PICAS, Valor.REY);
		
		Carta ReinaC = new Carta(Palo.CORAZON, Valor.REINA);
		Carta ReinaT = new Carta(Palo.TREBOL, Valor.REINA);
		Carta ReinaD = new Carta(Palo.DIAMANTE, Valor.REINA);
		Carta ReinaP = new Carta(Palo.PICAS, Valor.REINA);
		
		Carta JC = new Carta(Palo.CORAZON, Valor.JOTA);
		Carta JT = new Carta(Palo.TREBOL, Valor.JOTA);
		Carta JD = new Carta(Palo.DIAMANTE, Valor.JOTA);
		Carta JP = new Carta(Palo.PICAS, Valor.JOTA);
		
		
		Carta diC = new Carta(Palo.CORAZON, Valor.DIEZ);
		Carta diT = new Carta(Palo.TREBOL, Valor.DIEZ);
		Carta diD = new Carta(Palo.DIAMANTE, Valor.DIEZ);
		Carta diP = new Carta(Palo.PICAS, Valor.DIEZ);
		
		Carta nC = new Carta(Palo.CORAZON, Valor.NUEVE);
		Carta nT = new Carta(Palo.TREBOL, Valor.NUEVE);
		Carta nD = new Carta(Palo.DIAMANTE, Valor.NUEVE);
		Carta nP = new Carta(Palo.PICAS, Valor.NUEVE);
		
		
		Carta cC = new Carta(Palo.CORAZON, Valor.CUATRO);
		Carta cT = new Carta(Palo.TREBOL, Valor.CUATRO);
		Carta cD = new Carta(Palo.DIAMANTE, Valor.CUATRO);
		Carta cP = new Carta(Palo.PICAS, Valor.CUATRO);
		
		
		Carta tC = new Carta(Palo.CORAZON, Valor.TRES);
		Carta tT = new Carta(Palo.TREBOL, Valor.TRES);
		Carta tD = new Carta(Palo.DIAMANTE, Valor.TRES);
		Carta tP = new Carta(Palo.PICAS, Valor.TRES);
		
		
		
		Carta doC = new Carta(Palo.CORAZON, Valor.DOS);
		Carta doT = new Carta(Palo.TREBOL, Valor.DOS);
		Carta doD = new Carta(Palo.DIAMANTE, Valor.DOS);
		Carta doP = new Carta(Palo.PICAS, Valor.DOS);
		
		
		Carta AsC = new Carta(Palo.CORAZON, Valor.AS);
		Carta AST = new Carta(Palo.TREBOL, Valor.AS);
		Carta ASD = new Carta(Palo.DIAMANTE, Valor.AS);
		Carta ASP = new Carta(Palo.PICAS, Valor.AS);
		
		List<Carta> mp = new ArrayList<>();
		List<Carta> mp1 = new ArrayList<>();
		mp.add(ReinaC);
		mp.add(nC);
		mp1.add(new Carta(Palo.CORAZON,Valor.OCHO));
		
		mp1.add(JC);
		mp1.add(doT);
		mp1.add(tD);
		mp1.add(ASP);
		
		
		Mano mano = new Mano(mp,mp1);
		System.out.println(mano.getRango());
		
	}

}
