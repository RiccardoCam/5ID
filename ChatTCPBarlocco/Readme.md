# CHAT TCP
##INTRODUZIONE CLASSI

Il progetto è composto da tre file: -Client.java -Server.java - DatabaseConnection.java

Per la realizzazione del progetto ho utilizzato gli stream socket tramite il protocollo TCP che garantiscono una communicazione full-duplex.

La parte principale del programma è il **server**. Nel server si tiene traccia del numero di client connessi ed è responsabile della comunicazione con il database.

Il **client** richiede al server di entrare nella stanza e successivamente di potersi mettere in comunicazione.Inoltre La comunicazione tra i due utenti è gestita da questa classe.

Infine la **classeDatabaseConnection** effettua le operazioni di comunicazione con il database per login e registrazione.


##FLUSSO DEL PROGRAMMA

La prima operazione da fare è lanciare un'istanza del Server. Questo una volta avviatolancia il metodo `start() `dove il server si connette alla porta 8080 e crea la stanza, successivamente si mette in attesa dell'istanza di un client per iniziare a popolare la Trheadpool (ovvero una lista di trhead che posso rimanere attivi contemporaneamente anche dopo la chiusura).
La seconda operazione da svolgere è avviare un Client.All'avvio del Client il Server incrementerà la Trheadpool e successivamente svolge tutte le operazioni di login/autenticazione.
Dopo essersi loggati con successo con almeno due Client il Server chiede di mettersi in comunicazione con un'altro utente attivo tramite la funzione `connessione()`.
La comunicazione dei due Client continuerà fino a quando uno dei due utenti digiterà la parola "fine".