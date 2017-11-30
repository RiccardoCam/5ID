# Chat TCP

Server e client si scambiano informazioni inviando messaggi in JSON. Per creare il JSON viene utilizzata la classe Pacchetto che specifica il tipo di messaggio che si sta inviando e i parametri che contiene quel determinato messaggio.
Il server utilizza la tecnica del pooling e accetta fino a 50 client contemporaneamente. Per ogni client che si connette viene creato un thread di tipo ServerThread. Questo contiene un oggetto di tipo SessioneChat che permette di effettuare operazioni sul database.
Ad ogni ServerThread è associato un id che lo identifica. Il Server memorizza le associazioni tra l'identificativo del ServerThread e gli Stream di input e ouput e le associazioni tra username e ServerThread.
Ogni volta che un client esegue il login o si disconnette viene inviata a tutti i client connessi la lista degli utenti attivi.
Il client, dopo aver chiesto ip e porta del server a cui connettersi, mostra la finestra di login. Una volta ricevuto una risposta positiva al login, avvia la finestra della chat. Da questo momento il client resta perennemente in ascolto dei messaggi che gli arrivano dal server.
Basandosi sul tipo di pacchetto ricevuto, stabilisce cose fare con quel determinato messaggio. Salva inoltre tutti i messaggi ricevuti dal momento in cui l'utente ha effettuato l'accesso.
Se un utente esegue il login da due dispositivi diversi, il primo viene automaticamente disconnesso in modo da garantire che ad ogni utente corrisponda un solo ServerThread.

Il progetto fa uso della libreria org.json.
