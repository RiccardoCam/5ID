# Chat TCP in Java
Usando Sokect TCP si realizzi una chat. I client passano per il server per comunicare fra loro; il client alla connessione ha due possibilità: sign in e sign up; al servizio ci si registra (sign up) con un nickname salvato su di un database (SQLite o a piacere). I file:
```
README.md
Server.java
Client.java
SQLHelper.java
```
Nel primo la documentazione (strategie di progetto, sinteticamente, ed il nome del progettista), il secondo contiene il server, programmato in modo concorrente (thread o executor), che farà uso della classe helper, `SQLHelper`, per interagire con un db. Il tutto verrà salvato nella cartella di progetto
```
ChatTCP
````

## Valutazione

La sufficienza è garantita ad un progetto che garantisca tutte le specifiche di cui sopra; i voti di eccellenza (dal 7 in su) per gli apporti creativi del programmatore con enfasi particolare alla creazione di un client grafico scritto in Java (tipo JavaFX).