Realizzato da Sandro Lu

Realizzazione della macchina a stati finiti in UML:
Ogni stato è una domanda del server.
Ogni transizione rappresenta la risposta del cliente.
Ad ogni risposta diversa del clientee corrisponde ad una diversa domanda del server.
Ad ogni risposta del server, quest'ultimo cita il nome del cliente dinanzi.

Realizzazione del Client:
Il funzionamento del Client è uguale a "CapitalizedClient", con l'unica differenza il cliente sa quando il Server termina la connessione quindi a sua volta anche il cliente lo termina, con ciò riesco a "scappare" dagli eventi/errori non gestiti/gestibili.

Realizzazione del Server:
Il server usa la tecnica del pooling permettendo un miglior performance.
Il server utilizza la classe Hotel per ricavare domande differenti a seconda della risposta dal cliente dato.
Il cliente ha la possibilità di terminare QUANDO VUOLE la connessione premendo 'Enter'.
Inizia chiedendo il nome.
Finisce o quando il cliente decide di uscire, oppure quando finisco la conversazione(durata conversazione dipendentemente dalle risposte del cliente).

Realizzazione della classe Hotel:
La classe Hotel utilizza come struttura dati un Grafo Orientato.
Il metodo che usa il Server è getDomanda(), funziona a mo' di iteratore, per risolvere il problema di "rispondere diversamente a seconda della risposta del client" si sono usati gli ESPRESSIONI REGOLARI.
PS. VEDI LA PARTE DI DOCUMENTAZIONE DELL'HOTEL FATTO SUL FILE STESSO.