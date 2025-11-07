package Main;

import java.util.ArrayList;
import java.util.Scanner;
import Entities.Player;
import Entities.Player.PlayerClass;
import Items.Item;
import Items.Weapon;
import World.Dungeon;
import World.Tile;
import Story.Story;
import Encounters.Encounters;

public class GameLogic {

	public static Player player;
	public static boolean isRunning;
	private static Scanner scanner = new Scanner(System.in);
	
	//Methode qui lance le jeu
	public static void startGame()
	{
		isRunning = true;
		
		//Definir son personnage
		setupPlayer();
		
		//Démarrage de l'intro
		Story.Intro();
		
		Encounters.currentDungeon = new Dungeon(Encounters.dungeonLvl, player);
		
		//Lancement de la boucle d'actions principale
		gameLoop();
		
		System.out.println("Merci d'avoir joué !");
	}
	
	//Boucle d'actions principale
	private static void gameLoop()
	{
		while(isRunning) {
			Encounters.currentDungeon.displayMap();
			System.out.println("------------");
			//showPlayerStats();
			
			//Demander l'action
			System.out.println("Choisissez une action : \n(1) Haut\n(2) Bas\n(3) Gauche\n(4) Droite\n(5) Infos " + player.getName());
			System.out.println("(6) Inventaire\n(7) Quitter le jeu");
			String inputStr = scanner.nextLine();
			
			try {
				int input = Integer.parseInt(inputStr);
				if(input == 5) {
					showPlayerStats();
				} else if (input == 6) {
					showPlayerInventory();
				} else if (input == 7) {
					isRunning = false;
				} else {
					processMovement(input);
				}
			} catch (NumberFormatException e) {
				System.out.println("Veuillez entrez un numéro valide.");
			}
		}
	}

	//Methode pour définir le joueur en début de game, définir son nom et sa classe
	private static void setupPlayer()
	{
		boolean nameSet = false;
		boolean classSet = false;
		String name;
		PlayerClass playerClass;
		
		//Choix du nom du joueur
		do {
			System.out.println("Quel est votre nom ?\nEntrez votre nom :");
			name = scanner.next();
			System.out.println("Ton nom est : " + name + ", c'est bien ça ?");
			System.out.println("(1) Oui !\n(2) Non, je veux changer de nom");
			int input = scanner.nextInt();
			if(input == 1) {
				nameSet = true;
			}
		} while (!nameSet);
		
		//Choix de la classe
		do {
			System.out.println(name + ", choisissez votre classe :");
			System.out.println("(1) Chevalier (Défense +)");
			System.out.println("(2) Archer (PV-, Attaque+, Défense-");
			System.out.println("(3) Barbare (PV+, Attaque+, Défense--");
			System.out.println("(4) Mage (PV-, Attaque-, Défense++, Mana");
			
			int choice = scanner.nextInt();
			
			//Définir le choix de la classe en fonction de l'entier entrer
			switch(choice) {
				case 1: playerClass = PlayerClass.CHEVALIER; break;
				case 2: playerClass = PlayerClass.ARCHER; break;
				case 3: playerClass = PlayerClass.BARBARE; break;
				case 4: playerClass = PlayerClass.MAGE; break;
				default: playerClass = PlayerClass.CHEVALIER;
			}
			System.out.println("Vous avez choisi la classe " + playerClass + "!");
			System.out.println("Confirmez-vous ce choix ? \n(1) Oui !\n(2) Non, je veux changer de classe !");
			int input = scanner.nextInt();
			if(input == 1) {
				classSet = true;
			}
		} while (!classSet);
		player = new Player(name, playerClass);
	}
	
	//Methode pour afficher les stats du joueur
	private static void showPlayerStats() 
	{
		System.out.println("---");
		System.out.println(player.getName() + " | " + player.getPlayerClass());
		System.out.println(player.getName() + " | PV: " + player.getHp() + "/" + player.getMaxHp() + " | EXP: " + player.getXp() + " | Pièces: " + player.getGold());
		if (player.getPlayerClass() == PlayerClass.MAGE) {
			System.out.println("Mana: " + player.getMana());
		}
	}

	//Methode pour afficher l'inventaire du joueur
	public static void showPlayerInventory() 
	{
		System.out.println("\n---INVENTAIRE---");
		
		//Afficher l'arme equipée
		Weapon equippedWeapon = player.getCurrentWeapon();
		System.out.println("ARME ÉQUIPÉE: ");
		if (equippedWeapon != null) {
			System.out.println(" - " + equippedWeapon.getItemName() + "(Atk " + equippedWeapon.getAttackBonus() + ")");
			System.out.println(" {'" + equippedWeapon.getItemDesc() + "'}");
		} else {
			System.out.println(" - Aucune arme équipée");
		}
		
		//Afficher les items ArrayList inventory
		System.out.println("\nSAC À DOS: ");
		ArrayList<Item> inventory = player.getInv();
		
		if(inventory.isEmpty()) {
			System.out.println("Votre sac est vide.");
		} else {
			for(int i = 0; i < inventory.size(); i++) {
				Item item = inventory.get(i);
				System.out.println("  (" + (i+1) + ")  " + item.getItemName());
				System.out.println("  {" + item.getItemDesc() + "}  ");
			}
		}
		
		//Actions de l'inventaire
		System.out.println("\nQue voulez-vous faire ? \n(1)Utiliser un objet \n(2)Fermer l'inventaire");
		int choice = scanner.nextInt();
		
		if(choice == 1) {
			if (inventory.isEmpty()) {
				System.out.println("Vous n'avez pas aucun objet à utiliser");
				return;
			}
			
			System.out.println("Quel objet souhaitez-vous utiliser ? (Entrez le numéro)");
			try {
				int input = scanner.nextInt();
				player.useItem(input - 1);
			} catch (NumberFormatException e) {
				System.out.println("Erreur, entrez un numéro");
			} catch (IndexOutOfBoundsException e) {
				System.out.println("Numéro d'objet invalide");
			}
		}
	}

	//Methode pour récupérer le mouvement dans la map
	private static void processMovement(int input) 
	{
		int newX = player.getX();
		int newY = player.getY();
		
		switch (input){
			case 1: newX--; break; //newX--
			case 2: newX++; break; //newX++
			case 3: newY--; break; //newY--
			case 4: newY++; break; //newY++
			default: System.out.println("Entrez un choix entre 1 et 7.");
			return;
		}
		
		//Verifier bords de la map
		if(newX < 0 || newX >= 12 || newY < 0 || newY >= 12) {
			System.out.println("Vous ne pouvez avancé dans le mur.");
			return;
		}
		
		//Verifier la case où le joueur veut aller
		Tile targetTile = Encounters.currentDungeon.getTileAt(newX, newY);
		
		switch(targetTile) {
			case Tile.EMPTY: 
				System.out.println("Vous avancez d'une case");
				player.setPos(player, newX, newY);
				break;
			case Tile.OBSTACLE:
				Encounters.handleObstacle(player, newX, newY);
				break;
			case Tile.MONSTER:
				Encounters.handleMonster(player, newX, newY);
				break;
			case Tile.SHOP:
				Encounters.handleShop(player, newY, newY);
				player.setPos(player, newX, newY);
				break;
			case Tile.EXIT:
				Encounters.handleExit(player);
				break;
		}
		
	}
	
}
