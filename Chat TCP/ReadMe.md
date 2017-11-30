Ho creato la classe server e quella client, avviato il client bisogna fare o il login o registrasi. 
Per registrarsi bisogna inserire un nome utente valido, ovvero che non sia presente nel database altrimenti sarà segnalata la sua presenza.
Il client invia le informazioni di login al server che utilizzerà per registrare l’utente come connesso.
Il server si appoggia alla classe SQLHelper per verificare la presenza dell'utente o registrane uno nuovo.
Dopo aver superato con successo la fase di login il client potrà massaggiare con gli altri utenti connessi, dovrà quindi inserire il nome del client destinatario del messaggio e il server provvederà ad inviarglielo mentre tutti gli atri utenti connessi non avranno la possibilità di vedere il messaggio.
Se l’utente di destinazione dovesse non essere connesso allora il client sarà informato.
