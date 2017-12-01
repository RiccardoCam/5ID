# Chatroom UDP
Chatroom UDP realizzata in JavaFX.
#### Client
Questa è l'unica classe del progetto si occupa della gestione della sessione e dell'interfaccia grafica, questa infatti è una chat P2P ossia non è presente un server centrale.
Tramite il metodo enter() viene richiesto all'utente un nome per identificarlo e un indirizzo IP di tipo Multicast (224.0.0.0 a 239.255.255.255) una volta inseriti verra mandato a tutti i partecipanti il messaggio di connessione dell'utente
viene inoltre avviato un thread Receiver che si occupa dei paccheti in arrivo
tramite il metodo sendMessage() viene inviato un messaggio alla chatroom
il metodo sendPacket() viene usato per creare i pacchetti che verrano inviati,
questi pacchetti hanno un header di un byte che consente di creare fino a 256 tipi diversi di pacchetti.
Il Receiver quando riceve un pacchetto lo invia decodificato al Client con il metodo receivePacket() che provederà a stamparlo a video
Infine con il metodo exit() viene inviato il messaggio di disconessione e viene chiuso il socket.
