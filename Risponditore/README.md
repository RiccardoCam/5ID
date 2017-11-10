#Risponditore
L'esercizio del risponditore è stato realizzato con l'utilizzo di tre classi
* Server
* Mercato
* Client

La classe **Server** è la piu semplice: fa partire un ServerSocket che ascolta sulla porta 8008 e fa partire un thread (Mercato) ogni volta che c'è una nuova connessione inoltre avvisa con un messaggio se si prova ad avviare il server piu di una volta.

La classe **Mercato** implementa le funzionalita per gestire i prodotti e gli sconti utilizzando delle HashMap come una sorta di database.
La classe gestisce sopratutto il sistema di risposte per il Client: legge ogni riga inviata dal client e secondo cosa è stato inviato risponde in maniera adeguata.
I messaggi che il Mercato riceve sono una sottospecie di richieste ad esempio se ricevo "GET Prodotti" il Mercato invierà la lista dei prodotti riga per riga e alla fine invierà il messaggio "END" per segnalare al Client la fine della lista.
Per altri comandi ad esempio "GET Pagamento" il Mercato esegue alcune operazioni per calcolare gli sconti ed altro e infine invia un singolo messaggio contenente le informazioni richieste.
Se il Client chiude l'applicazione il Mercato termina la sua esecuzione.

La classe **Client** gestisce la comunicazione con il Server: essa è sotto forma di una semplice applicazione grafica in cui i bottoni inviano determinate richieste al Server.
All'avvio il Client prova a connettersi al Server (Indirizzo locale e porta 8008) e finche il server non è disponibile riprova.
Quando la connessione è stata stabilita viene richiesto un username se viene lasciato vuoto il Client comunichera al server che si tratta di un utente Guest.
Successivamente viene richiesta la lista dei prodotti tramite il metodo richiediProdotti(), a questo punto è possibile utilizzare i comandi dell'applicazione, se si aggiunge un prodotto verra mandata una richiesta al Server con la richiesta di calcolare il conto, se si clicca su "Applica Sconto" verra mandata la richiesta di applicare uno sconto se disponibile.
Al click del pulsante "Paga" viene inviato la richiesta "GET Pagamento" e il Server ci inviera un messaggio di riepilogo, all'Ok l'applicazione terminera.
La caratteristica piu importante del Client è che all'invio delle richieste sa esattamente cosa aspettarsi e quindi agisce di conseguenza aspettando le risposte.