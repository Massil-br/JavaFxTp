## JavaFxTp – Ticket Persistence

Application JavaFX de gestion de tickets de support, utilisant SQLite pour la persistance et Maven pour la construction.

## Prérequis

- **Java JDK 11** (ou supérieur compatible) correctement installé.
  - `java -version` doit afficher au moins la version 11.
  - La variable d’environnement `JAVA_HOME` doit pointer vers le JDK.
- **Maven 3.6+** installé et disponible dans le `PATH`.
  - Vérification : `mvn -version`.
- **Git** (facultatif mais recommandé) pour cloner le dépôt.
- Système testé principalement sous **Windows 10/11**.

## Récupération du projet

- **Via Git** :
  - `git clone <url-du-depot>`
  - `cd JavaFxTp`
- **Ou via une archive** :
  - Télécharger le projet (ZIP).
  - Extraire dans un dossier, par exemple `c:\Dev\Desktop\Java\JavaFxTp`.

Le projet Maven se trouve dans le sous-dossier `javafxtp`.

## Import dans l’IDE

- **IntelliJ IDEA** (recommandé) :
  - `File` → `Open...` → sélectionner le dossier `javafxtp`.
  - Confirmer l’import en tant que projet Maven.
- **Eclipse** :
  - `File` → `Import...` → `Existing Maven Projects`.
  - Sélectionner le dossier `javafxtp` puis terminer l’assistant.

L’IDE détecte automatiquement les dépendances (JavaFX, SQLite JDBC, etc.) via Maven.

## Lancer l’application depuis la ligne de commande

Dans un terminal positionné dans le dossier `javafxtp` :

- **Télécharger les dépendances et compiler** :
  - `mvn clean compile`
- **Lancer l’application JavaFX** :
  - `mvn clean javafx:run`

Le plugin `javafx-maven-plugin` est déjà configuré pour démarrer la classe principale `com.massil.TicketPersistenceApp`.

## Lancer l’application depuis l’IDE

- S’assurer que le module utilise le **JDK 11**.
- Créer (si nécessaire) une configuration de lancement avec :
  - **Main class** : `com.massil.TicketPersistenceApp`
  - Module/Classpath basé sur le projet Maven `javafxtp`.
- Exécuter la configuration : la fenêtre JavaFX de gestion des tickets doit s’ouvrir.

## Exécuter les tests

Dans le dossier `javafxtp` :

- **Lancer tous les tests Maven (JUnit 5)** :
  - `mvn test`

Les classes de tests JUnit (par exemple `TicketDaoTests`, `TicketPersistenceServiceTests` dans `src/test/java/com/massil`) sont exécutées automatiquement et vérifient le bon fonctionnement de la couche DAO et du service de persistance.

## Problèmes fréquents

- **Erreur JavaFX (module manquant)** :
  - Vérifier que l’exécution passe bien par Maven (`mvn javafx:run`) ou qu’un JDK 11+ est utilisé.
- **Erreur de connexion SQLite** :
  - Vérifier que le fichier `tickets.db` est accessible en écriture dans le dossier `javafxtp`.
- **Compilation impossible** :
  - Vérifier `JAVA_HOME`, la version de Maven, puis relancer `mvn clean compile`.