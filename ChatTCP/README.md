# Chat TCP
Chat TCP realizzata in JavaFX.
### Chat
 - **Messaggio**
 Questo è il tipo utilizzato per lo scambio dei messaggi, è una classe Serializable.
 Per poter inviare e ricevere questo oggetto tra client e server utilizzo ObjectInputStream e ObjectOutputStream questa classe è fornita di un enum per identificare il tipo dei messaggi, la creazioni dei suddetti messaggi viene eseguita tramite i vari metodi creaMessaggio che garantiscono che i messaggi siano formati correttamente
### Server
- **Server**
La classe Server funge da entry point nella gestione del server essa rimane in ascolto in attesa di nuove connessioni al ServerSocket e crea e lancia un nuovo thread connessione, gestisce inoltre il sistema di gestione degli utenti connessi e lancia inoltre il thread Buffer.
- **Connessione**
Questa classe gestisce una singola connessione con un Client essa è in grado di ricevere i messaggi dal Client e rispondere in modo adeguato, in particolare loggerà nel Server l'utente solo quando sara avvenuto il login dopodichè inoltrerà i messaggi di conversazione ricevuti dal client.
- **DatabaseUtenti**
Questa classe mantiene e gestisce la connessione al database del server: puo registrare e verificare il login degli utenti.
- **Buffer**
Questa é una classe Runnable che riceve i messaggi che il server non puo mandare ai Client perche disconessi, attraverso una lista cerca di inviare i messaggi contenuti, se gli utenti interessati sono connessi invia i messaggi al server che a sua volta riprovera a mandarli ai client.
### Client
- **Client**
Questa classe rappresenta l'entry point del client all'avvio tramite un Dialog richiederà all'utente il login o la registrazione e gestira in questo momento la comunicazione con il server.
Con il login avvenuto avvierà la Chat vera e propria e il thread Ricevitore.
Contiene inoltre metodi per la gestione delle conversazioni dell'utente e della chat in generale.
- **Chat**
Questa classe gestisce l'interfaccia grafica della chat
- **Ricevitore**
Questa classe gestisce dopo il login la ricezione dei messaggi dal server: quando riceve un messaggio chiama gli opportuni metodi contenuti in Client con le informazioni estratte dal messaggio
- **DatabaseConversazioni**
Questa classe gestisce il database del Client questo è utilizzato per salvare e creare in locale le conversazioni ricevute, tramite i metodi forniti è possibile recuperare le conversazioni degli utenti che hanno usato la chat su una determinata istanza del programma.