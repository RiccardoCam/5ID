Realizzato mediante grafica, nel progetto sono due le classi principali: Client.java, 
Server.java

Il progetto è realizzato usando il protocollo di trasporto UDP, tramite l'uso di 
MulticastSocket e DatagramPacket.

Il Server ha il compito di inviare e ricevere pacchetti di tipo DatagramPacket 
relativi ai messaggi da inviare a tutti gli altri utenti presenti nella Chatroom.

Il client invece esegue per prima cosa il log in inserendo il proprio nickname, e 
successivamente sarà connesso alla chatroom dove potrà inviare messaggi e ricevere tutti 
quelli degli altri utenti connessi, sempre tramite DatagramPacket, visualizzandoli in 
una VBox.