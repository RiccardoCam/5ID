Il progetto è composto da tre file principali: -Client -Server -SQL

Per la realizzazione del progetto vengono utilizzati degli stream socket basati sul protocollo di livello trasporto TCP che garantiscono una communicazione affidabile, fullduplex e orientata alla communicazione.

Il Server gestisce le varie connessioni tra i diversi client. Per ogni client che invia una richiesta di connessione, il Server lancia un Thread che lo gestisce, e lo salva in un'apposita struttura dati (un'arraylist).

Dentro al server ci sono due classi: Login che controlla l'esistenza dell'utente nel database, e se non presente peremtte di crearlo; e Collegamento che gestisce la possibilità di scegliere con quale utente loggato comunicare oppure rimanere in attesa di un collegamento (l'attesa del collegamento dura solo 20s poi sarai in grado di decidere se tornare in attesa o scegliere qualche altro utente che magari si è liberato);

IMPORTATE: È stato riscontrato il problema ricorrente in telecomunicazioni secondo cui i due client per communicare devono sincronizzare perfettamente le loro azioni per inviare dei messaggi (richiesta di communicazione con l'altro client).in quanto potrebbe essere in corso un altra operazione con la conseguente perdita del messaggio. Per risolvere il problema è stato attivato un timer che disconnetterà forzatamente il client non appena sarà scaduto (risolto in collaborazione con Zoccarato e Devid).

La classe client invia una richiesta di connessione verso il server che se viene accettata procede con l'autenticazione o registrazione sul DB e alla connessione con un client desiderato per inizare la communicazione. Dopo di che se tutto è andato a buon fine viene lanciato un Thread che continuamente ascolterà il server e mostrerà i messaggi ricevuti.

La classe SQL permette al server di interfacciarsi con un Database creato in SQLlite. Cioè permette di eseguire le richieste di INSERT e SELECT da parte del Server.