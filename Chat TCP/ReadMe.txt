Ho creato la classe server e quella client, avviato il client bisogna fare o il login o registrasi. 
Per registrarsi bisogna inserire un nome utente valido, ovvero che non sia presente nel database altrimenti sar� segnalata la sua presenza.
Il client invia le informazioni di login al server che utilizzer� per registrare l�utente come connesso.
Il server si appoggia alla classe SQLHelper per verificare la presenza dell'utente o registrane uno nuovo.
Dopo aver superato con successo la fase di login il client potr� massaggiare con gli altri utenti connessi, dovr� quindi inserire il nome del client destinatario del messaggio e il server provveder� ad inviarglielo mentre tutti gli atri utenti connessi non avranno la possibilit� di vedere il messaggio.
Se l�utente di destinazione dovesse non essere connesso allora il client sar� informato.
