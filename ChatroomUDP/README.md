Realizzato mediante grafica, nel progetto sono quattro le classi principali: Client.java, 
Server.java, SQLHelperUsers.java, SQLHelperMessages.java.

Il progetto è realizzato usando il protocollo di trasporto UDP, tramite l'uso di 
MulticastSocket e un DatagramPacket per l'input e uno per l'output.

Il Server, in base al messaggio ricevuto dal client, esegue una serie di operazioni diverse 
inviando al client dati necessari per completare la sua richiesta.

Il client invece esegue per prima cosa il log in 
o il sign up, inviando i dati al server il quale verificherà nel database la loro 
correttezza o esistenza, e successivamente sarà connesso alla chatroom dove potrà inviare 
messaggi e vedere tutti quelli degli altri utenti connessi, i quali saranno recuperati da 
un database sempre dal server, e poi visualizzati in una VBox.

Infine le due classi SQLHelper permette al server di recuperare dati da un Database 
creato in SQLlite a seconda delle richieste, con operazioni di insert per aggiungere 
utenti e messaggi, e interrogazioni per recuperare le varie informazioni.