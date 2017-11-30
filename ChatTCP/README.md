Per la realizzazione di questo progetto ho utilizzato tre classi : CLient.java, Server.java e SQLHelper
.java. 
Il protocollo usato è il TCP che rende la comunicazione affidabile e in modalità full-duplex.
La classe Client si occupa di gestire la parte relativa alla connessione tra Client e Server e l'invio di messaggi (che il Server andrà a gestire) attraverso l'utilizzo di un socket.

La classe Server si occupa di creare e gestire il login, il logout e la comunicazione uno-a-uno tra gli utenti sfruttando un database SQLite di supporto (con nome e password al suo interno) gestito dalla classe SQLHelper.La classe è implentata tramite una macchina a stati FIND_FRIEND,CHAT e DISCONNESSIONE.
Alla connessione l'utente invia nome utente e password dove, se il nome utente non esiste,per semplicità crea l'account con la password fornita(effettuando poi automaticamente il login), se invece esiste procederà al login.
La classe SQLHelper infine serve ad interagire con un database(SQLite), permette richieste di inserimento e di controllo.