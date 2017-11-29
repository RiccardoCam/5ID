Realizzato mediante grafica, nel progetto sono quattro le classi principali: Client.java, 
Server.java, SQLHelperUsers.java, SQLHelperMessages.java.

Il progetto è realizzato usando il protocollo di trasporto TCP, che garantisce la 
sicurezza e la affidabilità della connessione, e i relativi Stream di input e output.

Il Server gestisce le connessioni con i client che si connettono mediante la tecnica
del pooling, facendo partire un Thread ogni volta che un utente tenta di connettersi.
In base al messaggio che riceverà dal client, il server eseguirà delle operazioni diverse
inviando al client dati necessari per completare la sua richiesta.

Il client invece, una volta accettata la sua connessione dal server, eseguirà il log in
o il sign up, inviando i dati al server il quale verificherà nel database la loro 
correttezza, e successivamente sarà connesso alla chat dove i tutti i messaggi con altri 
client saranno recuperati da un database sempre dal server, e poi visualizzati in una VBox.

La classe client invia una richiesta di connessione verso il server che se viene 
accettata procede con l'autenticazione o registrazione sul DB e alla connessione con un 
client desiderato per inizare la communicazione. Dopo di che se tutto è andato a buon 
fine viene lanciato un Thread che continuamente ascolterà il server e mostrerà i messaggi 
ricevuti.

Infine le due classi SQLHelper permette al server di recuperare dati da un Database creato 
in SQLlite a seconda delle richieste, con operazioni di insert per aggiungere utenti e 
messaggi, e interrogazioni per recuperare le varie informazioni.