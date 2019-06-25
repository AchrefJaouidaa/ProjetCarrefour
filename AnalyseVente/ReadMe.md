# Project

Ce projet permet de générer des données des magasins et des transactions selon un format bien déterminé et de les analyser et avoir les résultats suivant:

top_100_ventes_<ID_MAGASIN>_YYYYMMDD.data
top_100_ventes_GLOBAL_YYYYMMDD.data
top_100_ca_<ID_MAGASIN>_YYYYMMDD.data
top_100_ca_GLOBAL_YYYYMMDD.data
top_100_ventes_<ID_MAGASIN>_YYYYMMDD-J7.data
top_100_ventes_GLOBAL_YYYYMMDD-J7.data
top_100_ca_<ID_MAGASIN>_YYYYMMDD-J7.data
top_100_ca_GLOBAL_YYYYMMDD-J7.data

## Getting Started
**Les deux packages du projets sont:

1/ Le premier package DataGen permet de générer les données entre dates données, en fixant aussi la taille maximale des données des transactions.

2/ Le deuxième package permet d'analyser les données entre deux dates (période au maximum 7 jours)


**Les résultats des packages sont comme suit:

1/ Pour le DataGen package: les fichiers magasins et transactions sont générés dans le dossier donnée par l'utilisateur

2/ Pour le deuxième package les données sont générés dans le dossier "Rslt" créé dans le meme dossier des données raw.

Le dossier Rslt est divisé en plusieur dossier comme suit:

- les dossiers de (0 à 6) sont les résultats journaliers du premier jour (index 0) au nième jour (n entre 0 et 6).  Dans chaque dossier, on trouve deux dossiers: Un qui englobe le résultat de tous les magasins nommé "global" et un autre pour chaque magasins nommé "Mg"

- le dossier 8 est le résultat pour la période spécifié par magasin

- le dossier 10 est le résultat pour la période spécifié pour tous les magasins

### Installing

Pour executer le code suivre les etapes suivantes, dans un terminal:
1- cd PathOfRootInProject
2- mvn clean compile assembly:single
3- java -cp target/CreatedJarName DataGen.DataGenerator DateDebut DateFin DossierContenantLesFichiersGeneres NombreDeMagasins TailleMegaFichiersTransactions -DskipTests=true
4- java -cp target/CreatedJarName Analyse.ResultMain DateDebut DateFin DossierContenantLesFichiersGeneres -DskipTests=true



## Running the tests

Pour executer les tests, des données de tests sont données avec le projet.Il sufit de spécifier le dossier contenant ces données et changer le AbsPath dans les test cases.

