# Risponditore automatico in Java #

La cartella Risponditore contiene i seguenti file: 
```
Negozio.java
```
Dentro la cartella Server, è il file in cui viene costruito il grafo.
```
Server.java
```
Dentro la cartella Server, è il file che contiene la classe Commesso che estende "Thread" e serve per creare il server e gestire il programma 
```
Client.java
```
Dentro la cartella client, è la classe Client che comunica col Server tramite socket tcp.
```
Graph.jar
```
Dentro la cartella GraphLib, è il file che contiene la libreria del grafo usato per organizzare i dati.
```
RisponditoreUML.jpg
```
È il file che contiene lo schema del progetto realizzato in UML.

# Realizzazione del programma #
Il programma funziona facendo partire un Server Socket che ascolta sulla porta 9898 e fa partire un Thread (Commesso) ad ogni nuova connessione; il progetto si basa su un grafo inizializzato nella classe Negozio.
Partendo dal nodo 'Start', vengono mostrati tutti i nodi adiacenti e quindi le opzioni possibili per il cliente. 
La gestione degli input/output è stata realizzata grazie a degli InputStream e OutputStream.
Inoltre è presente il tasto buy, per comprare un oggetto e il tasto end per terminare l'acquisto. Durante tutta l'esecuzione del programma verrà mostrato il conto.
Il grafo si può trovare riassunto in RisponditoreUML.jpg

Cristian Boldrin 5ID.
