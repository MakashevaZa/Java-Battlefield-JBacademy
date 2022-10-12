package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class Field {
    String[][] field;
    String[][] ships;
    Scanner scanner = new Scanner(System.in);
    boolean endOfGame = false;
    public int iterShip = 0;
    private enum ShipType {
        AIRCRAFTCARRIER("Aircraft Carrier", 5),
        BATTLESHIP("Battleship", 4),
        SUBMARINE("Submarine", 3),
        CRUISER("Cruiser", 3),
        DESTROYER("Destroyer", 2);

        final String name;
        final Integer size;

        ShipType(String name, Integer size) {
            this.name = name;
            this.size = size;
        }

        public String getName() {
            return name;
        }

        public Integer getSize() {
            return size;
        }
        @Override
        public String toString() {
            return String.format("%s (%d cells)", this.name, this.size);
        }
    }


    Field(){
        field = new String[11][11];
        initField(field);
        this.iterShip = 0;
        ships = new String[][]{new String[5], new String[4], new String[3], new String[3], new String[2]};
    }


    public static void initField(String[][] field) {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                if (i == 0 && j == 0) {
                    field[i][j] = " ";
                } else if (i == 0) {
                    field[i][j] = Integer.toString(j);
                } else if (j == 0) {
                    field[i][0] = Character.toString('@' + i);
                } else {
                    field[i][j] = "~";
                }
            }
        }
    }

    public void printField() {
        for (String[] line: this.field) {
            for (String cell: line) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    boolean checkCoord(String coord, String message) {
        boolean ok = coord.charAt(0) < '@' + 11 && coord.charAt(0) > '@'
                && Integer.parseInt(coord.substring(1)) > 0
                && Integer.parseInt(coord.substring(1)) < 11;
        if (!ok) {
            System.out.println(message);
        }
        return ok;
    }

    boolean checkShip(String start, String end, int len) {
        char startC = start.charAt(0);
        char endC = end.charAt(0);
        int startInt = Integer.parseInt(start.substring(1));
        int endInt = Integer.parseInt(end.substring(1));
        boolean ok = true;

        if (startC == endC) {
            ok = Math.abs(endInt - startInt) + 1 == len;
        } else if (startInt == endInt) {
            ok = Math.abs(startC - endC) + 1 == len;
        } else {
            System.out.println("Error! Wrong ship location! Try again:\n");
            return false;
        }
        if (ok == false) {
            System.out.println("Error! Wrong length of the Submarine! Try again:\n");
        }
        return ok;
    }

    boolean checkCloseness(String line, int startC, int endC, int startInt, int endInt) {
        boolean ok = true;
        String[][] copy = Arrays.stream(this.field).map(String[]::clone).toArray(String[][]::new);
        switch (line) {
            case "vertical":
                for (int j = 0; j < 11; j++) {
                    int n = 1;
                    for (int i = 10; i > 0; i--) {
                        copy[j][n++] = this.field[i][j];
                    }
                    copy[j][0] = this.field[0][j];
                }
                endInt = 11 - startC;
                startC = startInt;
                startInt = 11 - endC;
            case "horizontal":
                if (startC != 1) {
                    ok = ok && !Arrays.toString(copy[startC - 1]).substring(startInt, endInt + 1).contains("O");
                }
                if (startC != 10) {
                    ok = ok && !Arrays.toString(copy[startC + 1]).substring(startInt, endInt + 1).contains("O");
                }
                ok = startInt == 1 ? true && ok : copy[startC][startInt - 1] == "~" && ok;
                ok = endInt == 10 ? true && ok : copy[startC][endInt + 1] == "~" && ok;
                break;
        }
        return ok;
    }

    boolean checkFreePlace(String start, String end) {
        int startC = Math.min(start.charAt(0), end.charAt(0))  - '@';
        int endC = Math.max(start.charAt(0), end.charAt(0)) - '@';
        int startTemp = Integer.parseInt(start.substring(1));
        int endTemp = Integer.parseInt(end.substring(1));
        int startInt = Math.min(startTemp, endTemp);
        int endInt = Math.max(startTemp, endTemp);
        if (startC == endC) {
            if (checkCloseness("horizontal", startC, endC, startInt, endInt) == false) {
                System.out.println("Error! You placed it too close to another one. Try again:\n");
                return false;
            }
            for (int i = startInt; i < endInt + 1; i++ ) {
                if (this.field[startC][i].equals("O")) {
                    System.out.println("Error! Wrong ship location! Try again:\n");
                    return false;
                }
            }
        } else if (startInt == endInt) {
            if (checkCloseness("vertical", startC, endC, startInt, endInt) == false) {
                System.out.println("Error! You placed it too close to another one. Try again:\n");
                return false;
            }
            for (int i = startC; i < endC + 1; i++) {
                if (this.field[i][startInt].equals("O")) {
                    System.out.println("Error! Wrong ship location! Try again:\n");
                    return false;
                }
            }
        }
        return true;
    }

    public void fillField(String start, String end) {
        int startC = start.charAt(0) - '@';
        int endC = end.charAt(0) - '@';
        int iter = 0;
        int startInt = Integer.parseInt(start.substring(1));
        int endInt = Integer.parseInt(end.substring(1));
        if (startC == endC) {
            for (int i = Math.min(startInt, endInt); i < Math.max(startInt, endInt) + 1; i++ ) {
                this.field[startC][i] = "O";
                this.ships[this.iterShip][iter++] = "" + start.charAt(0) + String.valueOf(i);
            }
        } else if (startInt == endInt) {
            for (int i = Math.min(startC, endC); i < Math.max(startC, endC) + 1; i++) {
                this.field[i][startInt] = "O";
                this.ships[this.iterShip][iter++] = "" + (char)(i + '@') + String.valueOf(startInt);
            }
        }

    }

    public void enterShips() {
        String mes = "Error! Wrong ship location! Try again:";
        boolean firstQuestion;
        for (ShipType ship: ShipType.values()) {
            while (true) {
                firstQuestion = true;
                if (firstQuestion) {
                    System.out.printf("Enter the coordinates of the %s:\n", ship);
                }
                String start = scanner.next();
                String end = scanner.next();
                if (checkCoord(start, mes) && checkCoord(end, mes) && checkShip(start, end, ship.getSize()) && checkFreePlace(start, end)) {
                    fillField(start, end);
                    this.iterShip++;
                    printField();
                    break;
                }
                firstQuestion = false;
            }
        }

    }

    public void checkSunk(String shot, Field one) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < one.ships[i].length; j++) {
                if (shot.trim().equals(one.ships[i][j])) {
                    one.ships[i][j] = "+";
                    if ("++++++".contains(String.join("", ships[i]))) {
                        one.iterShip++;
                        if (one.iterShip == 5) {
                            System.out.println("You sank the last ship. You won. Congratulations!");
                            endOfGame = true;
                        } else {
                            System.out.println("You sank a ship!");
                        }
                        return;
                    } else {
                        System.out.println("You hit a ship!");
                        return;
                    }

                }
            }
        }


    }

    public void fillShot(String shot, Field one, Field two) {
        int startC = shot.charAt(0) - '@';
        int startInt = Integer.parseInt(shot.substring(1));

        if (one.field[startC][startInt] == "~" || one.field[startC][startInt] == "M") {
            one.field[startC][startInt] = "M";
            two.field[startC][startInt] = "M";
            System.out.println("You missed!");
        } else {
            one.field[startC][startInt] = "X";
            two.field[startC][startInt] = "X";
            checkSunk(shot, one);
        }
    }


    public void shoot(Field one, Field two) {
        String mes = "Error! You entered the wrong coordinates! Try again:";
        String shot = scanner.next();
        if (checkCoord(shot, mes)) {
            fillShot(shot, one, two);
        }
    }
}
