package battleship;

import java.io.IOException;

public class Player {
    Field playerField;
    Field enemyFoggedField;
    private String name;

    Player(String name){
        this.name = name;
        playerField = new Field();
        enemyFoggedField = new Field();
    }

    public String getName() {
        return name;
    }

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
