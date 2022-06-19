import java.util.*;
import java.io.*;

public class Checkers {
    // scanner variable
    public static Scanner sc = new Scanner(System.in);

    public static char[][] generateBoard() {
        char[][] board = new char[8][8];

        // fill empty squares
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ' ';
            }
        }

        // fill P1 squares
        for (int i = 7; i > 4; i--) {
            if (i % 2 == 0) {
                for (int j = 1; j < 8; j += 2)
                    board[i][j] = 'x';
            } else {
                for (int j = 0; j < 8; j += 2)
                    board[i][j] = 'x';
            }
        }

        // fill P2 squares
        for (int i = 0; i < 3; i++) {
            if (i % 2 == 0) {
                for (int j = 1; j < 8; j += 2)
                    board[i][j] = 'o';
            } else {
                for (int j = 0; j < 8; j += 2)
                    board[i][j] = 'o';
            }
        }
        return board;
    }

    public static void drawBoard(char[][] board) {
        // print board borders
        System.out.print("\n  ");
        for (int i = 0; i < 8; i++) {
            System.out.print("+---");
        }
        System.out.print("+\n");

        // print board content
        for (int i = 0; i < 8; i++) {
            // print board y coordinate
            System.out.print(Math.abs(8 - i) + " ");
            // print piece on each square
            for (int j = 0; j < 8; j++) {
                System.out.print("| " + board[i][j] + " ");
            }
            // print board borders
            System.out.print("|\n  ");
            for (int k = 0; k < 8; k++) {
                System.out.print("+---");
            }
            System.out.print("+\n");
        }

        System.out.print(" ");
        // print board x coordinates
        for (int i = 97; i <= 104; i++) {
            System.out.print("   " + (char) i);
        }
        System.out.println("\n");
    }

    public static int[] coordToIndex(String coord) {
        // variable declaration
        int[] indexNums = new int[2]; // [col, row]

        // convert coordinate system to array index system
        indexNums[0] = (int) coord.charAt(0) - 97; // column
        indexNums[1] = Math.abs(((int) coord.charAt(1) - 49) - 7); // row

        return indexNums;
    }

    public static String[][] initLegalCaptures(char[][] board, char piece, String coord, int[] startCoord) {
        // variable declaration
        String[][] legalCaptures = new String[4][2];
        char oppPiece;
        boolean complete = false;
        int val, val1 = 1;

        // determine piece values
        if (Character.toLowerCase(piece) == 'x') {
            if (piece == 'X') {
                val1++;
            }
            val = 1;
            oppPiece = 'o';
        } else {
            if (piece == 'O') {
                val1++;
            }
            val = -1;
            oppPiece = 'x';
        }

        // determine possible captures
        for (int i = 0; i < val1; i++) {
            // check right and left of piece
            for (int j = -1; j <= 1; j += 2) {
                try {
                    if (Character.toLowerCase(board[startCoord[1] - val][startCoord[0] + j]) == oppPiece
                            && board[startCoord[1] - val * 2][startCoord[0] + j * 2] == ' ') {
                        // available jump
                        for (int k = 0; k < legalCaptures.length && !complete; k++) {
                            if (legalCaptures[k][0] == null) {
                                legalCaptures[k][0] = "" + (char) (coord.charAt(0) + j)
                                        + (char) (coord.charAt(1) + val);
                                legalCaptures[k][1] = "" + (char) (coord.charAt(0) + j * 2)
                                        + (char) (coord.charAt(1) + val * 2);
                                complete = true;
                            }
                        }
                        complete = false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // piece is on side of board
                }
            }
            val = -val;
        }
        return legalCaptures;
    }

    public static String[] initLegalMoves(char[][] board, char piece, String coord, int[] startCoord) {
        // variable declaration
        String[] legalMoves = new String[4];
        boolean complete = false;
        int val, val1 = 1;

        // determine piece values
        if (Character.toLowerCase(piece) == 'x') {
            if (piece == 'X') {
                val1++;
            }
            val = 1;
        } else {
            if (piece == 'O') {
                val1++;
            }
            val = -1;
        }

        for (int i = 0; i < val1; i++) {
            for (int j = -1; j <= 1; j += 2) {
                try {
                    if (board[startCoord[1] - val][startCoord[0] + j] == ' ') {
                        // determine coord pos
                        for (int k = 0; k < legalMoves.length && !complete; k++) {
                            if (legalMoves[k] == null) {
                                legalMoves[k] = "" + (char) (coord.charAt(0) + j) + (char) (coord.charAt(1) + val);
                                complete = true;
                            }
                        }
                        complete = false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    // piece is on side of board
                }
            }
            val = -val;
        }
        return legalMoves;
    }

    public static void saveGame(char[][] board, int turns, String fileName) {
        BufferedWriter bw;

        try {
            bw = new BufferedWriter(new FileWriter(fileName, false));

            // read board
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board.length; col++) {
                    bw.write(board[row][col]);
                }
                bw.write("\n");
            }
            bw.write(turns);
            bw.close();
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public static char[][] retrieveGame(String fileName) {
        // variable declaration
        char[][] board = new char[8][8];
        String line;
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(fileName));
            for (int row = 0; row < board.length; row++) {
                line = br.readLine();
                for (int col = 0; col < board.length; col++) {
                    board[row][col] = line.charAt(col);
                }
            }
        } catch (IOException e) {
            System.out.println("No saved game found.\n");
            board[0][0] = '0';
        }

        return board;
    }

    public static void twoPlayer(char[][] board, String fileName) {
        boolean run = true, playing = true, win = true, validMove = false;
        int player = 1, turns = 0, counter = 1, legalCaptureCount = 0, legalMoveCount = 0, choice;
        char piece = 'x', oppPiece = 'o';
        String coord;
        int[] startCoord = new int[2], newCoord = new int[2], captured = new int[2];
        String[][] legalCaptures = new String[4][2];
        String[] legalMoves = new String[4];

        while (playing) {
            run = true;

            // prompt current player to move
            System.out.printf("Player %d's move\n", player);

            // prompt for starting piece location
            do {
                System.out.println("Enter piece coordinate: ");
                System.out.println("Q to give up");
                System.out.println("S to save and quit");
                System.out.print("> ");
                coord = sc.nextLine().toLowerCase().replaceAll("\\s+", "");

                // determine if valid coord
                if (coord.equals("q")) {
                    run = false;
                    playing = false;

                    // delete file
                    File file = new File(fileName);
                    file.delete();

                    if (player == 1) {
                        player = 2;
                    } else {
                        player = 1;
                    }
                    System.out.printf("Player %d has won!\n", player);
                } else if (coord.equals("s")) {
                    run = false;
                    playing = false;

                    saveGame(board, turns, fileName);
                } else if (coord.length() != 2 || coord.charAt(0) < 97 || coord.charAt(0) > 104 || coord.charAt(1) < 49
                        || coord.charAt(1) > 56) {
                    System.out.println("\nInvalid coordinate.");
                } else {
                    startCoord = coordToIndex(coord);

                    // determine if there is a piece at given position
                    if (Character.toLowerCase(board[startCoord[1]][startCoord[0]]) != piece) {
                        System.out.println("\nInvalid piece selected.");
                    } else {
                        piece = board[startCoord[1]][startCoord[0]];
                        legalCaptures = initLegalCaptures(board, piece, coord, startCoord);
                        legalMoves = initLegalMoves(board, piece, coord, startCoord);
                        if (legalCaptures[0][0] == null && legalMoves[0] == null) {
                            System.out.println("\nPiece has no valid moves.");
                        } else {
                            run = false;
                        }
                    }
                }
            } while (run);

            if (playing) {
                // determine valid move positions
                System.out.println("\nSelect from valid moves:");
                // display possible captures
                counter = 1;
                for (int i = 0; i < legalCaptures.length && legalCaptures[i][0] != null; i++) {
                    System.out.printf("%d: x%s\n", counter, legalCaptures[i][0]);
                    legalCaptureCount++;
                    counter++;
                }

                // display possible moves
                for (int i = 0; i < legalMoves.length && legalMoves[i] != null; i++) {
                    System.out.printf("%d: %s\n", counter, legalMoves[i]);
                    legalMoveCount++;
                    counter++;
                }

                // prompt for move
                do {
                    try {
                        System.out.print("> ");
                        choice = sc.nextInt();
                        sc.nextLine();

                        if (choice > legalCaptureCount + legalMoveCount || choice < 1) {
                            System.out.println("Invalid option.\n");
                        } else if (choice <= legalCaptureCount) {
                            // chose to capture
                            captured = coordToIndex(legalCaptures[choice - 1][0]);
                            // remove captured piece
                            board[captured[1]][captured[0]] = ' ';
                            newCoord = coordToIndex(legalCaptures[choice - 1][1]);
                            if (newCoord[1] == 7 || newCoord[1] == 0) {
                                piece = Character.toUpperCase(piece);
                            }
                            // move piece to new location
                            board[startCoord[1]][startCoord[0]] = ' ';
                            board[newCoord[1]][newCoord[0]] = piece;

                            // determine if another piece can be captured
                            do {
                                coord = legalCaptures[choice - 1][1];
                                startCoord = coordToIndex(coord);
                                legalCaptures = initLegalCaptures(board, piece, coord, startCoord);

                                if (legalCaptures[0][0] == null) {
                                    run = false;
                                } else {
                                    drawBoard(board);
                                    // display new legal captures
                                    legalCaptureCount = 0;
                                    System.out.println("\nSelect from valid captures (0 to cancel):");
                                    for (int i = 0; i < legalCaptures.length && legalCaptures[i][0] != null; i++) {
                                        System.out.printf("%d: x%s\n", i + 1, legalCaptures[i][0]);
                                        legalCaptureCount++;
                                    }
                                    try {
                                        System.out.print("> ");
                                        choice = sc.nextInt();
                                        sc.nextLine();

                                        if (choice == 0) {
                                            run = false;
                                        } else if (choice > legalCaptureCount || choice < 0) {
                                            System.out.println("Invalid option.\n");
                                        } else {
                                            captured = coordToIndex(legalCaptures[choice - 1][0]);
                                            // remove captured piece
                                            board[captured[1]][captured[0]] = ' ';
                                            newCoord = coordToIndex(legalCaptures[choice - 1][1]);
                                            if (newCoord[1] == 7 || newCoord[1] == 0) {
                                                piece = Character.toUpperCase(piece);
                                            }
                                            // move piece to new location
                                            board[startCoord[1]][startCoord[0]] = ' ';
                                            board[newCoord[1]][newCoord[0]] = piece;
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("Invalid option.\n");
                                    }
                                }
                            } while (run);

                            validMove = true;
                        } else {
                            // chose to move
                            newCoord = coordToIndex(legalMoves[choice - legalCaptureCount - 1]);
                            if (newCoord[1] == 7 || newCoord[1] == 0) {
                                piece = Character.toUpperCase(piece);
                            }
                            // move piece to new location
                            board[startCoord[1]][startCoord[0]] = ' ';
                            board[newCoord[1]][newCoord[0]] = piece;
                            validMove = true;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("\nInvalid input.");
                    }
                } while (!validMove);

                turns++;
                drawBoard(board);

                // determine if game has been won
                for (int row = 0; row < board.length; row++) {
                    for (int col = 0; col < board.length && win; col++) {
                        if (board[row][col] == oppPiece) {
                            win = false;
                        }
                    }
                }
                saveGame(board, turns, fileName);

                if (win) {
                    System.out.printf("\nPlayer %d has won!\n", player);
                    playing = false;
                }

                // switch player
                if (player == 1) {
                    player = 2;
                    piece = 'o';
                    oppPiece = 'x';
                } else {
                    player = 1;
                    piece = 'x';
                    oppPiece = 'o';
                }
            }
        }
        System.out.printf("Game lasted %d turns\n\n", turns);
    }

    public static void main(String[] args) {
        // variable declaration
        char[][] board = new char[8][8];
        boolean run = true, onePlayer = false;
        String name1, name2, choice, fileName;

        while (run) {
            // prompt for player count
            do {
                System.out.print("\nHow many players are there? (1/2)\n> ");

                switch (sc.nextLine().replaceAll("\\s+", "")) {
                    case "1":
                        onePlayer = true;
                        run = false;
                        break;

                    case "2":
                        // prompt for names
                        System.out.print("\nEnter player 1 name: ");
                        name1 = sc.nextLine();
                        try {
                            name1 = (name1.substring(0, 1).toUpperCase() + name1.substring(1).toLowerCase()).substring(
                                    0,
                                    name1.indexOf(' '));
                        } catch (StringIndexOutOfBoundsException e) {
                            name1 = name1.substring(0, 1).toUpperCase() + name1.substring(1).toLowerCase();
                        }
                        System.out.print("Enter player 2 name: ");
                        name2 = sc.nextLine();
                        try {
                            name2 = (name2.substring(0, 1).toUpperCase() + name2.substring(1).toLowerCase()).substring(
                                    0,
                                    name2.indexOf(' '));
                        } catch (StringIndexOutOfBoundsException e) {
                            name2 = name2.substring(0, 1).toUpperCase() + name2.substring(1).toLowerCase();
                        }

                        fileName = name1 + "-" + name2 + ".txt";
                        do {
                            System.out.print(
                                    "\nWould you like to load an in-progress game (a) or start a new game (b)?\n> ");
                            choice = sc.nextLine();
                            switch (choice) {
                                case "a":
                                    // search for existing file
                                    board = retrieveGame(fileName);
                                    if (board[0][0] == '0') {
                                        run = false;
                                        break;
                                    } else {
                                        drawBoard(board);
                                        twoPlayer(board, fileName);
                                        run = false;
                                        break;
                                    }

                                case "b":
                                    board = generateBoard();
                                    drawBoard(board);
                                    twoPlayer(board, fileName);
                                    run = false;
                                    break;

                                default:
                                    System.out.println("Invalid option.");
                            }
                        } while (run);
                        break;

                    default:
                        System.out.println("Invalid option.\n");
                }
            } while (run);
            run = true;

            // vs AI
            while (onePlayer) {
                System.out.println("\nAI is not available.");
                onePlayer = false;
            }
        }
    }
}