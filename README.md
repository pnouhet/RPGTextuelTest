⚔️ RPGTextuel : Aventure dans la console de l'IDE

Ce projet est un mini-jeu de RPG Textuel conçu pour être joué directement dans la console. Explorez un donjon, combattez des monstres, achetez des items, affrontez le boss et libérez le Fort !

🌟 Fonctionnalités Principales

    Exploration de Donjon (6x6) : Parcourez une petite carte de 6×6 cases appelée Donjon.
    
    4 Classes disponible : Chaque classe à ses avantages et défaut :

        Chevalier (Défense +)
        Archer (PV-, Attaque+, Défense-)
        Barbare (PV+, Attaque+, Défense--)
        Mage (PV-, Attaque-, Défense++, Mana)

    Rencontres Aléatoires : Chaque case peut révéler aléatoirement :

        Des Monstres pour des combats dynamiques.
        Des Shops pour l'achat d'objets (actuellement limité aux Potions).
        Des Obstacles ou la Sortie.

    Système de Combat Simple : Engagez-vous dans des combats basiques basés sur l'Attaque et la Défense.

    Progression Dynamique : Le niveau des monstres augmente automatiquement en fonction du Niveau du Donjon pour maintenir le défi.

    Système d'Inventaire/Shop : Possibilité d'acheter et d'utiliser des objets comme les Potions pour se soigner.

    Histoire Évolutive : Une trame narrative se déroule à travers différents Actes qui se déclenchent en fonction de votre progression dans le Donjon.

📦 Architecture du Projet

    Histoire Évolutive : Une trame narrative se déroule à travers différents Actes qui se déclenchent en fonction de votre progression dans le Donjon.
    Le programme est structuré en sept (7) packages principaux pour une organisation claire et modulaire :
    Package	Description	Classes Clés
        AsciiArt	Regroupe les représentations visuelles en Art Ascii utilisées pour illustrer les classes du personnage et d'autres éléments.	
        Encounters	Gère tous les événements qui se produisent lorsque le joueur rencontre quelque chose sur une case : Monstre, Obstacle, Shop ou la Sortie.
        Entities	Définit toutes les entités actives dans le jeu (joueur et ennemis).	Entity (Abstraite), Monstre, Obstacle, Player
        Items	    Gère la création et les propriétés des objets trouvables ou achetable dans le jeu.	Item (Abstraite), Potion, Weapon
        Main	    Contient la logique d'initialisation et le cœur du jeu.	Game (Initialisation), GameLogic (Logique principale)
        Story	    Contient le scénario du jeu, avec différents Actes déclenchés par le niveau du Donjon.
        World	    Définit la structure du Donjon (la carte) et les propriétés de ses cases.	Donjon, Case

🧩 Détail des Entités (Entities)

Toutes les entités héritent de la classe abstraite Entity, qui définit les propriétés communes :

    nom
    HP (Points de Vie Actuels)
    HPMax (Points de Vie Maximum)
    attaque
    defense

💎 Détail des Objets (Items)

Tous les objets héritent de la classe abstraite Item, définissant :

    nom
    description
    valeurAchat

Classe	Propriété Spécifique	Fonction
Potion	healAmount	Permet de soigner les HP du Player.
Weapon	attackBonus	Augmente l'attaque de base du Player lorsqu'elle est équipée.

🚀 Comment Jouer

    Téléchargez le fichier src, puis ajouté le à votre IDE.
    Lancez le programme depuis votre.
    Suivez les instructions de la console pour créer votre personnage et commencer l'exploration du Donjon.
