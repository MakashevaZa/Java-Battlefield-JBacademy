package battleship;

import java.util.Scanner;

public class Main {

    public static boolean endOfGame = false;

    public static void launch(Player player) {
        System.out.printf("%s, place your ships on the game field\n", player.getName());
        player.playerField.printField();
        player.playerField.enterShips();
    }

    public static void iterGame(Player player, Player enemy) {

        enemy.enemyFoggedField.printField();
        System.out.println("---------------------");
        player.playerField.printField();
        System.out.println(player.getName() + ", it's your turn:");
        enemy.playerField.shoot(enemy.playerField, enemy.enemyFoggedField);
        Player.promptEnterKey();
    }

    public static void game(Player p1, Player p2) {
        System.out.println("The game starts!\n");
        p1.playerField.iterShip = 0;
        p2.playerField.iterShip = 0;
        int flag = 0;
        while (endOfGame == false) {
            if (flag == 0) {
                iterGame(p1, p2);
                flag = 1;
            } else {
                iterGame(p2, p1);
                flag = 0;
            }
        }
    }

    public static void main(String[] args) {
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");
        launch(player1);
        player1.promptEnterKey();
        System.out.println("...");
        launch(player2);
        player2.promptEnterKey();
        game(player1, player2);
    }
}
