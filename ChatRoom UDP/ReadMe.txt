Ho creato la classe server e quella client, avviato il client bisogner� fare il login o altrimenti registrasi.
Per registrarsi bisogna inserire un nome utente valido, ovvero che non sia presente nel database altrimenti sar� segnalata la sua presenza. La classe SQLHelper presente nel server provveder� a registrare il nuovo utente nel mio database sqlite.
Il client invia le informazioni di login a server che utilizzer� per registrare gli utenti connessi.
Dopo aver superato con successo la fase di login il client potr� massaggiare con tutti gli altri utenti  connessi, dovr� inserire nella TextField il messaggio  e cliccare sul pulsante invia e il server provveder� ad inviarlo a tutti gli utenti connessi.
Il server, ogni volta che un nuovo utente si connette con successo, aggiunge un copia del pacchetto in un ArrayList per poter prendere in seguito l�indirizzo di destinazione.
