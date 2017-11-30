# Realizzato da Sandro Lu

# Server
    Il server usa la tecnica del pooling permettendo un miglior performance.
    Per la gestione del database utilizza una classe di appoggio [SQHelper]
    Per gestire gli utenti online e la loro corretta comunicazione utilizza una classe di appoggio [User]
>In base al messaggio che riceverà dal client, il server eseguirà delle determinate operazioni inviando al client dati necessari per completare la sua richiesta.
- ### SQHelper: E' definito un PreperedStatement, esso è più sicuro di uno Statement.
- ### User: Mette in relazione il Task con il suo relativo nickname.


# Client
## Il client dopo essersi avviato ha due opzioni:
- ### Login, se il cliente ha già effettuato in precedenza un sign up.
- ### Sign up, se il cliente non ha ancora un profilo.


    
    Il server dopo i dovuti controlli sul database può ora gestire la chat.
    
## Dopo aver effettuato l'accesso il cliente può vedere gli utenti connessi e selezionare il suo destinatario.

>Anche il cliente destinatario deve scegliere la persona correlata.

### P.S. TUTTI I FILE SONO DEFINITI IN UN UNICO PROGETTO.
Per test si ricorda di creare il file database con relativo nome senza apici 'database.db' si è usato sqlite, libreria jdbc di versione 3.20.0