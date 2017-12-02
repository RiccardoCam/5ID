Questo è il progetto di una chatTCP.
Il progetto è composto dalle classi:
-SQLHelper che contiene dei metodi per interagire con il database che recuperano dati o creano nuove utenze;
-Client che comunica con il server e dopo aver ricevuto i permessi per entrare nelle chat, sceglie con che utente chattare.
-Server che gestisce con i Thread i vari Client e dopo averli fatti loggare chiedento i dati al database tramite la classe SQLHelper, li mostra le liste degli utenti disponibili e dopo che il client ha scelto gli mette in comunicazione tra loro, dopo di che li sconnette dalla chat se riceve "." da uno dei due e li fa scegliere un nuovo utente, mentre li disconnette definitivamente se riceve "." durante la scelta dell'utente con cui chattare da parte di un Client.