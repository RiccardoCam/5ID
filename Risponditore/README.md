# Risponditore

Tramite Socket TCP, diversi client comunicano con il server che risponde a degli input seguendo un automa a stati finiti ed eseguendo una diversa azione per ogni stato.
Il server accetta fino a 15 connessioni contemporaneamente e utilizzando la tecnica del pooling.
L'automa viene creato a partire da un file xml che associa ad ogni stato un id univoco e ad ogni transizione tre attributi:
-  **input** - un'espressione regolare che stabilisce se l'input immesso dall'utente corrisponde a quella determinata transizione.
-  **next** - che stabilisce lo stato di destinazione della transizione
-  **patternParametri** - un'espressione regolare che stabilisce il pattern da utilizzare per estrapolare i parametri immessi dall'utente scartando le informazioni superflue

Ad ogni stato dell'automa viene associato un oggetto del tipo Azione. Un oggetto di tipo Azione è un oggetto Runnable che richiede di ridefinire il metodo astratto *esegui()* che da come risultato una stringa che viene poi inviata all'utente come risposta.
Ad ogni oggetto di tipo Azione vengono passati anche il riferimento ad una mappa che verrà utilizzata come Bundle e che permetterà di salvare alcuni dati dell'utente e la macchina a stati finiti in modo che un'azione possa passare ad un'altra azione senza l'esplicita richiesta dell'utente.
Ogni azione contiene un semaforo inizializzato a 0 permessi. Per ottenere la risposta relativa ad un determinato stato, occorre chiamare il metodo *getRisultato()* che rimarrà in attesa del risultato fintanto che l'azione non sarà terminata (quindi fino a quando il semaforo non sarà rilasciato).
Poichè l'azione è di tipo Runnable, è possibile eseguire codice Java e pertanto effettuare elaborazioni sui dati immessi (come ad esempio contattare un web service). All'interno della classe AzioniDefault sono state caricate alcune azioni di esempio che poi possono essere estese agendo lato server.

Il client non fa altro che avviare l'interfaccia grafica e iniziare la comunicazione con il server.
Poi permette all'utente di inviare un messaggio e attende la risposta dell'automa.

La classe server si occupa solo della comunicazione con il client, ad ogni server è associato un oggetto di tipo Risponditore che si occupa di tradurre i messaggi ricevuti ed eseguire l'azione corrispondente allo stato della macchina a stati finiti. La classe FSM si occupa di gestire la macchina a stati finiti e di aggiornare lo stato corrente in base all'input ricevuto.

Nel progetto si fa uso della libreria org.json.
