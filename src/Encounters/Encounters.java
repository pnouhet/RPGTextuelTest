package Encounters;

import java.util.Random;
import java.util.Scanner;

import Entities.Monster;
import Entities.Player;
import Main.GameLogic;
import Story.Story;
import World.Dungeon;

public class Encounters {

	//static Player player;
	
	public static Dungeon currentDungeon;
	public static int dungeonLvl = 1; //1. Village abandonné, 2. Plaines dangereuses, 3. Fort Gobelins, 4. Salle du boss
	private static Random random = new Random();
	private static Scanner scanner = new Scanner(System.in);
	
	//Methode pour gérer le combat
	public static void handleMonster(Player player, int x, int y)
	{
		Monster monster = createRandomMonster();
		System.out.println("Vous rencontrez un " + monster.getName() + " ennemi ! Battez-vous !");
		
		while(player.isAlive() && monster.isAlive()) {
			System.out.println("\n---COMBAT---");
			System.out.println(player.getName() + " HP: " + player.getHp() + "/" + player.getMaxHp());
			System.out.println(monster.getName() + " HP: " + monster.getHp() + "/" + monster.getMaxHp());
			System.out.println("Que voulez-vous faire ?\n(1) Attaquer\n(2) Fuir le combat");
			String inputStr = scanner.nextLine();
			
			//int choice = -1;
			try {
				int playerDmg = Math.max(1, player.getAttack() - monster.getDefense());
				int monsterDmg = Math.max(1, monster.getAttack() - player.getDefense());
				int choice = Integer.parseInt(inputStr);
				
				//Logique d'attaque
				if(choice == 1) {
					monster.takeDmg(playerDmg);
					System.out.println("Vous infligez " + playerDmg + " dégats à " + monster.getName());
					
					if(monster.isAlive()) {
						player.takeDmg(monsterDmg);
						System.out.println(monster.getName() + " vous inflige " + monsterDmg + " dégats");
					}
				} else if (choice == 2) {
					GameLogic.showPlayerInventory();
				} else if (choice == 3){
					if(random.nextBoolean()) {
						System.out.println("Vous avez réussi à fuir !");
						return;
					} else {
						System.out.println("Vous n'avez pas réussi à fuir le combat...");
						player.takeDmg(monsterDmg);
						System.out.println(monster.getName() + " vous inflige " + monsterDmg + " pendant votre tentative de fuite");
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("Veuillez entrer un numéro valide.");
			}
		}
		
		//Resultat combat
		if(!player.isAlive()) {
			System.out.println("Vous êtes mort. Game Over");
			GameLogic.isRunning = false;
		} else {
			System.out.println("Vous avez vaincu " + monster.getName() + "!");
			player.takeXp(monster.getXpOnDeath());
			player.takeGold(monster.getGoldOnDeath());
			System.out.println("Vous avez gagné " + monster.getXpOnDeath() + " EXP et " + monster.getGoldOnDeath() + " Pièces.");
			player.setPos(player, x, y);
			currentDungeon.clearTile(x, y);
		}
	}

	//Creation de monstre aleatoire en fonction du nv du donjon
	private static Monster createRandomMonster() 
	{
		if(dungeonLvl == 1) {
			return new Monster("Slime", 30, 5, 2, 10, 5);
		} else if (dungeonLvl == 2) {
			if (random.nextBoolean()) {
				return new Monster("Loup", 50, 10, 4, 20, 10);
			} else {
				return new Monster("Meute de Loups", 70, 15, 3, 30, 15);
			}
		} else if (dungeonLvl == 3) {
			return new Monster("Gobelin", 60, 12, 6, 25, 12);
		}
		return new Monster("Slime", 30, 5, 2, 10, 5);
	}

	public static void handleObstacle(Player player, int x, int y) 
	{
		System.out.println("Un rocher vous bloque le chemin");
		System.out.println("Voulez-vous le détruire ?\n(1) Oui\n(2) Non");
		int choice = scanner.nextInt();
		
		if(choice == 1) {
			System.out.println("Vous détruisez le rocher.");
			//Math.random()
			System.out.println("Malheureusement vous vous blessez en détruisant l'obstacle et perdez 5 PV.");
			player.takeDmg(5);
			currentDungeon.clearTile(x, y);
			player.setPos(player, x, y);
		} else {
			System.out.println("Vous faites demi-tour.");
		}
	}

	public static void handleExit(Player player) 
	{
		System.out.println("BRAVO ! Vous avez trouvé la sortie du Donjon !");
		dungeonLvl++;
		if (dungeonLvl == 2) {
			Story.Act1();
			player.setPos(player, 0, 0);
			currentDungeon = new Dungeon(dungeonLvl, player);
		} else if (dungeonLvl == 3) {
			Story.Act2();
			player.setPos(player, 0, 0);
			currentDungeon = new Dungeon(dungeonLvl, player);
		} else if (dungeonLvl == 4) {
			Story.Act3();
			player.setPos(player, 0, 0);
			currentDungeon = new Dungeon(dungeonLvl, player);
		} else {
			Story.End();
		}
	}
	
	public static void handleBoosFight(Player player)
	{
		//Creation du boss
		Monster boss = new Monster("Ogre, Boss du Fort", 300, 30, 10, 1000, 500);
		
		System.out.println("Vous rencontrez un " + boss.getName() + " ennemi ! Battez-vous !");
		
		while(player.isAlive() && boss.isAlive()) {
			System.out.println("\n---COMBAT---");
			System.out.println(player.getName() + " HP: " + player.getHp() + "/" + player.getMaxHp());
			System.out.println(boss.getName() + " HP: " + boss.getHp() + "/" + boss.getMaxHp());
			System.out.println("Que voulez-vous faire ?\n(1) Attaquer\n(2) Ouvrir l'inventaire\n(3) Fuir le combat");
			int choice = scanner.nextInt();
			int playerDmg = Math.max(1, player.getAttack() - boss.getDefense());
			int monsterDmg = Math.max(1, boss.getAttack() - player.getDefense());
			
			//Logique d'attaque
			if(choice == 1) {
				
				boss.takeDmg(playerDmg);
				System.out.println("Vous infligez " + playerDmg + " dégats à " + boss.getName());
				
				if(boss.isAlive()) {
					player.takeDmg(monsterDmg);
					System.out.println(boss.getName() + " vous inflige " + monsterDmg + " dégats");
				}
			} else if (choice == 2) {
				GameLogic.showPlayerInventory();
			} else if (choice == 3)	{
				System.out.println("Vous ne pouvez pas fuir le combat.");
			}
		}
		
		//Resultat combat
		if(!player.isAlive()) {
			System.out.println("Vous êtes mort. Game Over");
			GameLogic.isRunning = false;
		} else {
			System.out.println("FÉLICITATIONS ! VOUS AVEZ VAINCU " + boss.getName() + " !");
			System.out.println("Grâce à vous le Fort est libéré !");
			player.takeXp(boss.getXpOnDeath());
			player.takeGold(boss.getGoldOnDeath());
			System.out.println("Vous avez gagné " + boss.getXpOnDeath() + " EXP et " + boss.getGoldOnDeath() + " Pièces.");
		}
		handleExit(player);
	}

	public static void handleShop(Player player, int x, int y) 
	{
		System.out.println("Vous rencontrez un Marchand louche !");
		player.setPos(player, x, y);
		
		while(true) {
			System.out.println("Pièces en poche : " + player.getGold());
			System.out.println("Que voulez-vous acheter ?");
			System.out.println("(1) Potions - 10 Pièces \n(2) Hache - 100 Pièces\n(3) Armure Rouillé - 25 Pièces\n(4) Partir");
			int choice = scanner.nextInt();
			
			if(choice == 1) {
				if (player.getGold() >= 10) {
				//player.addPotion();
				player.takeGold(-10);
				System.out.println("Vous avez acheté 1 Potion !");
				} else {
					System.out.println("Vous n'avez pas assez de pièces.");
				}
			} else if (choice == 2) {
				if (player.getGold() >= 100) {
				//player.addAxe();
				player.takeGold(-100);
				System.out.println("Vous avez acheté 1 Hache !");
				} else {
					System.out.println("Vous n'avez pas assez de pièces.");
				}
			} else if (choice == 3) {
				if (player.getGold() >= 25) {
				//player.addRustyArmor();
				player.takeGold(-25);
				System.out.println("Vous avez acheté 1 Armure Rouillée !");
				} else {
					System.out.println("Vous n'avez pas assez de pièces.");
				}
			} else if (choice == 4) {
				System.out.println("Marchand Louche : À bientôt Voyageur.");
				break;
			}
		} 
	}
}
