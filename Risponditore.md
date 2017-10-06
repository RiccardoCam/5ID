# risponditore automatico in Java
Usando Sokect TCP si realizzi, mediante un automa a stati finiti, un risponditore automatico. Si progetti in UML la macchina a stati finiti utilizzando l'opportuno diagramma: ogni stato è una domanda (server), ogni transizione costituisce la risposta (client) che porta alla successiva domanda; la conversazione dovrà iniziare e terminare in modo opportuno (transizione); il risponditore risponde citando il nome del cliente ad ogni risposta; possono essere serviti più clienti (ad ogni cliente un thread o usando tecnica del pooling). I file da consegnare sono i seguenti:
```
README.md
Pizzeria.java
Server.java
Client.java
```
Nel primo la documentazione (strategie di progetto, sinteticamente, ed il nome del progettista), il secondo contiene la macchina a stati finiti, ovvero il protocollo (qui in questo esempio si immagina un risponditore per una pizzeria) e gli altri due il client ed il server. Il tutto verrà messo nella cartella
```
Risponditore
````

## Valutazione

La sufficienza è garantita ad un progetto che rispetti tutte le specifiche di cui sopra; i voti di eccellenza (dal 7 in su) per gli apporti creativi del programmatore.
