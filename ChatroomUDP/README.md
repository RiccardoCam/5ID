Questo è il progetto di una chatroomUDP creata con dei MulticastSocket.
Il progetto è composto dalla classe Client che utilizza un MulticastSocket per comunicare con altri Client(Peer to Peer);questo avviene utilizzando un indirizzo di multicast.
Il Client utilizza altre due classi, ovvero:
-ChatIn, che crea dei DatagramPacket per ricevere i messaggi di tutti i Client(vige un controllo che omette nella visualizzazione i pacchetti inviati dallo stesso).
-ChatOut, che attraverso un BufferedReader legge i messaggi che si vogliono mandare agli altri utenti, e che poi con un DatagramPacket invia a tutti i Client.
