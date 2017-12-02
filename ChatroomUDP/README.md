# ChatroomUDP

Per la realizzazione di questo progetto ho utilizzato una sola classe : Client.java.

La chat è stata realizzata in modalità peer to peer, ossia senza la presenza di un server attraverso il quale passano i messaggi dei vari client.

## Client 

Ogni volta che ci si connette verrà chiesto all'utente un Username con il quale poi chatterà. La classe lancia due thread, SendMessage, che si occupa di inviare, attraverso il metodo .send(), i messaggi scritti dai vari clienti, mentre ReceiveMessage si occupa di ricevere e stampare a video i messaggi che gli arrivano attraverso il metodo .receive().
 
