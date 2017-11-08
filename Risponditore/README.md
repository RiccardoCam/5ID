Utilizzo tre classi:
 -Server
 -Client
 -Bar
La classe multithreading server utilizza un thread pool per gestire una communicazione con massimo dieci client alla volta e per ogni client esegue, 
tramite executor, la classe runnable Bar.
La classe client permette di creare un client, di connetterlo al multithreaded server e di prendere le risposte dall'utente.
La classe runnable bar viene utilizzata dal server per prendere le domande da porre al client, inserite all'interno di un'apposita struttura dati (Mappa).

Andrea Zoccarato