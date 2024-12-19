Web App dedicata alla gestione di attività commmerciali come un salone di bellezza.
Tecnologie utilizzate: Spring(Boot), Jpa e db MySql per la persistenza dati, Thymeleaf per la parte grafica e JavaScript per l automazione di alcune funzionalità.
L'Applicazione permette operazioni CRUD(admin) rispettivamente di Clienti, Appuntamenti e Ricevute. 
Permette la visualizzazione degli appuntamenti del giorno a schermo. 
Permette di generare le ricevute direttamente dagli appuntamenti. 
Salvataggio degli appuntamenti e ricevute in storico(tabella seprata).
Permette di visionare l' andamento dell'attività, previo login, accedendo alle funzionalità admin(NON implementata spring security).

struttura del progetto(Eclipse)
![index](src/main/resources/static/img/struttura.jpg)

index accessibile senza credenziali
![index](src/main/resources/static/img/index.jpg)

index e funzionalità dopo login admin
![index](src/main/resources/static/img/indexAdmin.jpg)

form inserimento/aggiornamento ricevuta
![index](src/main/resources/static/img/aggiornaRicevuta.jpg)

funzionalità ricerca clienti tramite iniziali nome (JavaScript)
![index](src/main/resources/static/img/ricercaCliente.jpg)

dettagli andamento dell'attività
![index](src/main/resources/static/img/temometroSalone.jpg)
