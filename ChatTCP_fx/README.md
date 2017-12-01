# CHAT TCP in Java #

La cartella ChatTCP_fx contiene due cartelle: 
- La cartella server, composta da:
```
Server.java
```
in cui viene creato il Server tcp e lanciati i thread.
```
SocketThread.java
```
Il thread che rappresenta il socket e viene lanciato non appena un client cerca di connettersi al server. 
```
SQLHelper
```
La classe che rappresenta la connessione al database degli account.

- La cartella client, composta da: 
```
Client.java
```
La classe client che rappresenta un utente,
```
MessageInterface.java e UserInterface.java
```
Le due interfacce che gestiscono rispettivamente l'invio di messaggi e la visione di altri utenti online.

# Realizzazione del programma #
Ogni richiesta del client è composta da un comando e a seguire la lista dei parametri necessari, la quale viene poi convertita in un StringTokenizer e spedita al Server.
Quest'ultimo, in base al comando ricevuto andrà ad eseguire i metodi per soddisfare la richiesta dell'utente.
Sia il server che il client hanno un'interfaccia grafica realizzata tramite JavaFX.

Cristian Boldrin 5ID.
