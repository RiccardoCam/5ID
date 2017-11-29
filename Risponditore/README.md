Progetto realizzato usando tre file, ovvero Ristorante, Server e Client.

Il server permette la connessione con i vari client tramite un executor, che serve ad 
eseguire la classe runnable "Ordinazione".

Il client si connette al server tramite un socket e ha il compito di ricevere l'input 
da tastiera immesso dall'utente, di inviarlo al server e di stampare tutti i messaggi
ricevuti da esso.

La classe Ristorante infine contiene tutte le domande all'interno di un ArrayList di
stringhe, in caso in futuro si voglia se ne voglia aggiungere qualcuna, e il grafo, 
ArrayList di ArrayList di un oggetto di classe Arco, contenente tutte le possibili 
risposte che il cliente può dare, le rispettive domande successive e i costi di 
ordinazione in caso ad esempio di bibita o menù. La validità delle risposte date 
dall'utente inoltre è verificata tramite l'uso di stringhe regolari.


