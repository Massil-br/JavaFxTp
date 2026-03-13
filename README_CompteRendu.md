## Architecture choisie

L’application est structurée en couches distinctes :
- **Couche présentation** : `TicketEditorView.fxml`, `TicketEditorController` gèrent l’interface JavaFX, les événements utilisateur et la validation simple des champs.
- **Couche métier** : `SupportTicket`, `TicketPersistenceService` portent la logique fonctionnelle (création, modification, suppression, export, règles métier de base).
- **Couche persistance** : `TicketDAO` (interface) définit le contrat d’accès aux données, `SQLiteTicketDao` en est l’implémentation concrète via SQLite, `DatabaseManager` se charge de la création/connexion à la base.  
Cette séparation permet de changer l’implémentation de stockage (SQLite, fichier CSV, autre SGBD) sans impacter la partie UI ni la logique métier.

## Rôle du DAO

- **Abstraction de la source de données** : l’interface `TicketDAO` expose des méthodes CRUD génériques (`create`, `findById`, `findAll`, `update`, `delete`) sans révéler les détails SQL.
- **Implémentation concrète** : `SQLiteTicketDao` traduit ces opérations en requêtes SQL sur la base `tickets.db`.
- **Point d’extension** : pour changer de persistance, il suffit de fournir une autre implémentation de `TicketDAO` (par exemple `FileTicketDao`) sans modifier le contrôleur ou le service.
- **Centralisation des accès** : tout accès en lecture/écriture passe par le DAO, ce qui facilite la maintenance, la gestion des erreurs et les tests unitaires (`TicketDaoTests`).

## Fonctionnement global du CRUD

- **Create (C)** : à la validation du formulaire, le contrôleur construit un `SupportTicket` puis appelle le `TicketPersistenceService`, qui délègue au `TicketDAO.create`. Le DAO exécute un `INSERT` SQL et renvoie l’objet éventuellement enrichi (id généré).
- **Read (R)** : au chargement de l’application ou sur rafraîchissement, le service appelle `TicketDAO.findAll` (ou `findById`), le DAO exécute un `SELECT` et renvoie la liste de tickets pour affichage dans la TableView JavaFX.
- **Update (U)** : lorsqu’un ticket est édité, le contrôleur met à jour l’objet `SupportTicket` puis appelle `TicketDAO.update`, qui exécute un `UPDATE` SQL ciblé sur l’id.
- **Delete (D)** : sur action de suppression, le contrôleur envoie l’id au service qui appelle `TicketDAO.delete`, ce qui déclenche un `DELETE` SQL dans la base.
- **Export** : via `TicketExporter`, le service peut aussi sérialiser les tickets (par exemple vers `tickets.csv`) à partir des données obtenues via le DAO.

## Difficultés rencontrées

- **Configuration JavaFX + Maven + module-info** : il a fallu configurer correctement `pom.xml` et `module-info.java` (modules `javafx.controls`, `javafx.fxml`, `java.sql`) pour que l’application se lance, que l’injection FXML fonctionne et que JDBC soit accessible.
- **Gestion de la connexion SQLite** : la mise en place de `DatabaseManager` (URL JDBC, driver, création du schéma si nécessaire) a demandé des ajustements, en particulier pour bien fermer les ressources et éviter les fuites.
- **Synchronisation UI / modèle** : faire coïncider les champs FXML, les propriétés de `SupportTicket` et les colonnes de la TableView sans erreurs de binding ni NullPointer a nécessité plusieurs itérations.
- **Tests unitaires de la persistance** : `TicketDaoTests` et `TicketPersistenceServiceTests` doivent gérer un état de base propre (initialisation/suppression de la base ou des tables) pour garantir des tests reproductibles.

## Pistes d’amélioration

- **Validation métier plus riche** : centraliser une vraie validation (champs obligatoires, formats, longueurs, cohérence des états) côté service plutôt que dans le seul contrôleur.
- **Gestion des erreurs utilisateur** : afficher des messages d’erreur plus détaillés et conviviaux (boîtes de dialogue, surlignage des champs en erreur) au lieu de simples logs console.
- **Couche DAO plus générique** : introduire des classes utilitaires génériques pour factoriser le code SQL répétitif (mapping ResultSet → objet, gestion des transactions).
- **Internationalisation et accessibilité** : externaliser les textes dans des fichiers de ressources, améliorer la navigation clavier et le contraste visuel.
- **Séparation plus nette test/prod** : utiliser des bases différentes (par exemple mémoire pour les tests), scripts de migration et données de démo pour faciliter l’intégration continue.
- **Amélioration de l'UI** : Aller vers un design d'UI un peu plus moderne en proposant un mode sombre et un mode clair avec  des amélioration pour que ça fasse moins "Vieille application", se rapprocher plutôt des sites web modernes en beauté tout en restant aussi simple que possible.
