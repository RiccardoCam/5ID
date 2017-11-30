CHATROOM UDP

Il client dispone di 2 thread 1 per continuare a ricevere i messaggi e uno per continuare a inviarli non appena pronti

Il client dispone di un sistema per non visualizzare i messaggi inviati dallo stesso username ( per evitare di visualizzare i messaggi che invia lo stesso client)
È dato da uno split sulla stringa con i caratteri ">>" Per poi accedere alla posizione 0 dove si trova l`username

Il server semplicemente rimane in ascolto e ogni volta che riceve un messaggio lo reinvia nel gruppo
Il server è stato costruito con un thread

IL GRUPPO E LA PORTA SONO FISSATO IN ALTO ALLE 2 CLASSI COME VARIABILI STATICHE PRIVATE

