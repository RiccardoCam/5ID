CHATROOM UDP

Il progetto è composto da due file principali:
-Server.java
-Client.java

SERVER
La classe runnable server utilizza un MulticastSocket e due buffer, uno per ricever l'altro per inviare.
Il server si limita a ricevere un DatagramPacket, prelevare le informazioni contenute, reinserirle in un alto DatagramPacket
ed inviarle a tutti.

CLIENT
Anche la classe client usufruisce di un MulticastSocket per partecipare alla chatroom e fa partire due thread di cui:
uno che si occupa di ascoltare constantemente il server e riportare i messaggi ricevuti mentre l'altro utilizza
un bufferedReader per leggere da tastiera e inviare al server i dati.

Andrea Zoccarato