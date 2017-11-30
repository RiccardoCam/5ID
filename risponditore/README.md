Utilizzo tre classi: Car, Client e Server 
Nella classe server utilizzo un thread pool per gestire una communicazione con massimo N client alla volta e per ogni client che si collega al server viene lanciato un thread 
Nella classe client viene gestita la connessione con il server e la comunicazione con la classe Runnable Car
Nella classe Car vengono gestite le domande che verranno poste al client e le rispettive risposte