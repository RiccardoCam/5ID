package risponditore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Gelateria implements Runnable {

	protected String name;
	protected boolean isGelato;
	protected final String[] gustiGelati = {"fragola", "menta", "cioccolato", "stracciatella", "yogurt", "nutella", "melone"};
	protected final String[] gustiGranite = {"cola", "menta", "limone"};
	protected Map<String, Double> aggiunte;
	protected Map scelte;
	protected Map<String, Double> grandezza;
	protected double costoTot;
	protected Map domande;
	private final Socket socket;
	private boolean isTrovato;

	public Gelateria(Socket socket) {
		this.name = "";
		this.costoTot = 0;
		this.aggiunte = new HashMap();
		aggiunte.put("panna", 1.0);
		aggiunte.put("biscotto", 0.0);
		aggiunte.put("topping fragola", 1.0);
		aggiunte.put("cioccolata", 1.0);
		this.grandezza = new HashMap();
		grandezza.put("piccolo", 1.0);
		grandezza.put("medio", 2.0);
		grandezza.put("grande", 3.0);
		this.scelte = new HashMap();
		scelte.put("coppa", grandezza);
		scelte.put("cono", grandezza);
		scelte.put("granita", grandezza);
		this.domande = new HashMap();
		domande.put(0, "Come si chiama?");
		domande.put(1, "Cosa desideri? Abbiamo: cono, coppa e granita");
		domande.put(2, "Quanto grande?");
		domande.put(3, "I gelati che abbiamo sono: " + gustiToString(true));
		domande.put(4, "Le granite che abbiamo sono: " + gustiToString(false));
		domande.put(5, "Vuoi la panna?");
		domande.put(6, "Vuoi della cioccolata sopra?");
		domande.put(7, "Vuoi del topping alla fragola?");
		domande.put(8, "Vuoi un biscotto?");
		domande.put(9, "Vuoi altro?");
		this.socket = socket;
	}

	@Override
	public void run() {

		String input = "";
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			for (int i = 0; i < domande.size(); i++) {
				out.println(domande.get(i));
				input = in.readLine().toLowerCase();
				switch (i) {
					//come si chiama
					case 0:
						this.name = input;
						break;
					//cosa desidera
					case 1:
						if (scelte.containsKey(input)) {
							if (!input.equals("granita")) {
								isGelato = true;
							} else {
								isGelato = false;
							}
							break;
						} else {
							i--;
							break;
						}
					//quanto grande
					case 2:
						if (grandezza.containsKey(input)) {
							costoTot += grandezza.get(input);
							if (isGelato) {
								break;
							} else {
								i++;
								break;
							}
						} else {
							i--;
							break;
						}
					//gelati
					case 3:
						for (int j = 0; j < gustiGelati.length; j++) {
							if (gustiGelati[j].equals(input)) {
								isTrovato = true;
								break;
							} else {
								isTrovato = false;
							}
						}
						if (isTrovato) {
							i++;
							break;
						} else {
							i--;
							break;
						}

					//granite
					case 4:
						for (int j = 0; j < gustiGranite.length; j++) {
							if (gustiGranite[j].equals(input)) {
								isTrovato = true;
								break;
							} else {
								isTrovato = false;
							}
						}
						if (isTrovato) {
							i=8;
							break;
						} else {
							i--;
							break;
						}

					//panna
					case 5:
						if (input.equals("no")) {
							break;
						} else {
							costoTot += aggiunte.get("panna");
							break;
						}

					//cioccolata
					case 6:
						if (input.equals("no")) {
							break;
						} else {
							costoTot += aggiunte.get("cioccolata");
							break;
						}

					//topping
					case 7:
						if (input.equals("no")) {
							break;
						} else {
							costoTot += aggiunte.get("topping fragola");
							break;
						}
					//biscotto
					case 8:
						if (input.equals("no")) {
							break;
						} else {
							break;
						}
					//altro
					case 9:
						if (input.equals("no")) {
							break;
						} else {
							i = 0;
							break;
						}
				}
			}
			out.println("Il costo totale è: " + costoTot + "€");
		} catch (IOException ex) {
			Logger.getLogger(Gelateria.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			try {
				
				socket.close();
			} catch (IOException ex) {
				Logger.getLogger(Gelateria.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private String gustiToString(boolean isGelato) {
		String ret = "";
		if (isGelato) {
			for (int i = 0; i < gustiGelati.length - 1; i++) {
				ret += gustiGelati[i];
				ret += ',';
			}
			ret += gustiGelati[gustiGelati.length - 1];
			return ret;
		} else {
			for (int i = 0; i < gustiGranite.length - 1; i++) {
				ret += gustiGranite[i];
				ret += ',';
			}
			ret += gustiGranite[gustiGranite.length - 1];
			return ret;
		}
	}

}
