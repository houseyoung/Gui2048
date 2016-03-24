import javafx.application.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.scene.text.*;
import javafx.geometry.*;

import java.io.IOException;
import java.util.*;

public class Gui2048 extends Application {
    private String outputBoard; // The filename for where to save the Board
    private Board board; // The 2048 Game Board

    private static final int TEXT_SIZE_LOW = 55; // Low value tiles (2,4,8,etc)
    private static final int TEXT_SIZE_MID = 45; // Mid value tiles
    //(128, 256, 512)
    private static final int TEXT_SIZE_HIGH = 35; // High value tiles
    //(1024, 2048, Higher)

    // Fill colors for each of the Tile values
    private static final Color COLOR_EMPTY = Color.rgb(205, 192, 180); //这个地方把透明度去了，不然会有重影
    private static final Color COLOR_2 = Color.rgb(238, 228, 218);
    private static final Color COLOR_4 = Color.rgb(237, 224, 200);
    private static final Color COLOR_8 = Color.rgb(242, 177, 121);
    private static final Color COLOR_16 = Color.rgb(245, 149, 99);
    private static final Color COLOR_32 = Color.rgb(246, 124, 95);
    private static final Color COLOR_64 = Color.rgb(246, 94, 59);
    private static final Color COLOR_128 = Color.rgb(237, 207, 114);
    private static final Color COLOR_256 = Color.rgb(237, 204, 97);
    private static final Color COLOR_512 = Color.rgb(237, 200, 80);
    private static final Color COLOR_1024 = Color.rgb(237, 197, 63);
    private static final Color COLOR_2048 = Color.rgb(237, 194, 46);
    private static final Color COLOR_OTHER = Color.BLACK;
    private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);

    private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242);
    // For tiles >= 8

    private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101);
    // For tiles < 8

    // 建立一个GridPane，把游戏主界面放到该布局中。
    private GridPane pane;
    // 建立一个GridPane，把游戏名称及得分放到该布局中。
    private GridPane titleAndScorePane;
    // 建立一个BorderPane，把所有要显示的内容都放到该布局中。
    private BorderPane borderPane;

    // 显示得分的Text
    private Text score;

    @Override
    public void start(Stage primaryStage) {
        // Process Arguments and Initialize the Game Board
        processArgs(getParameters().getRaw().toArray(new String[0]));

        // Create the pane that will hold all of the visual objects
        pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
        pane.setStyle("-fx-background-color: rgb(187, 173, 160)");
        // Set the spacing between the Tiles
        pane.setHgap(15);
        pane.setVgap(15);

        // 设置游戏名称
        Label title = new Label("2048");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 50));

        // 设置得分
        score = new Text("Score: " + board.getScore());
        score.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));

        // 将游戏名称与得分分别放置到一个矩形中
        Rectangle titleRectangle = new Rectangle();
        titleRectangle.setWidth(215);
        titleRectangle.setHeight(50);
        titleRectangle.setFill(Color.rgb(187, 173, 160));

        Rectangle scoreRectangle = new Rectangle();
        scoreRectangle.setWidth(215);
        scoreRectangle.setHeight(50);
        scoreRectangle.setFill(Color.rgb(187, 173, 160));

        // 将包含游戏名称与得分的矩形放置到titleAndScorePane上
        titleAndScorePane = new GridPane();
        titleAndScorePane.setAlignment(Pos.CENTER);
        titleAndScorePane.setPadding(new Insets(11.5, 0, 0, 0));
        titleAndScorePane.setStyle("-fx-background-color: rgb(187, 173, 160)");
        titleAndScorePane.setHgap(15);
        titleAndScorePane.add(titleRectangle, 0, 0);
        titleAndScorePane.add(scoreRectangle, 1, 0);
        titleAndScorePane.add(title, 0, 0);
        GridPane.setHalignment(title, HPos.CENTER);
        titleAndScorePane.add(score, 1, 0);
        GridPane.setHalignment(score, HPos.CENTER);

        // 调用setGui方法将主界面的方格及得分放置到界面上
        setGui();

        // 把pane及titleAndScorePane都放置到borderPane上
        borderPane = new BorderPane();
        borderPane.setTop(titleAndScorePane);
        borderPane.setCenter(pane);

        // 把borderPane放到Scene上
        Scene scene = new Scene(borderPane);

        // 调用键盘响应事件
        scene.setOnKeyPressed(new myKeyHandler());

        // 把Scene放到Stage上，显示出来，并设置标题。
        primaryStage.setTitle("Gui2048");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // 将主界面的方格及得分放置到界面上
    private void setGui() {
        score.setText("Score: " + board.getScore());

        // 设置主界面的方格
        for (int row = 0; row < board.GRID_SIZE; row++) {
            for (int column = 0; column < board.GRID_SIZE; column++) {
                // 为每个方格定义一个矩形
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(100);
                rectangle.setHeight(100);

                // 根据方格中的数字大小设置矩形颜色
                switch (board.getGrid()[row][column]) {
                    case 0:
                        rectangle.setFill(COLOR_EMPTY);
                        break;
                    case 2:
                        rectangle.setFill(COLOR_2);
                        break;
                    case 4:
                        rectangle.setFill(COLOR_4);
                        break;
                    case 8:
                        rectangle.setFill(COLOR_8);
                        break;
                    case 16:
                        rectangle.setFill(COLOR_16);
                        break;
                    case 32:
                        rectangle.setFill(COLOR_32);
                        break;
                    case 64:
                        rectangle.setFill(COLOR_64);
                        break;
                    case 128:
                        rectangle.setFill(COLOR_128);
                        break;
                    case 256:
                        rectangle.setFill(COLOR_256);
                        break;
                    case 512:
                        rectangle.setFill(COLOR_512);
                        break;
                    case 1024:
                        rectangle.setFill(COLOR_1024);
                        break;
                    case 2048:
                        rectangle.setFill(COLOR_2048);
                        break;
                    default:
                        rectangle.setFill(COLOR_OTHER);
                        break;
                }

                // 设置方格中显示的数字
                Text text = new Text();

                // 如果数字为0，则不显示，否则显示。
                if (board.getGrid()[row][column] != 0) {
                    text.setText("" + board.getGrid()[row][column]);
                }
                text.setFill(Color.WHITE);

                // 根据数字大小设置数字显示的大小
                if (board.getGrid()[row][column] < 128) {
                    text.setFont(Font.font("Times New Roman", FontWeight.BOLD, TEXT_SIZE_LOW));
                } else if (board.getGrid()[row][column] < 1024) {
                    text.setFont(Font.font("Times New Roman", FontWeight.BOLD, TEXT_SIZE_MID));
                } else {
                    text.setFont(Font.font("Times New Roman", FontWeight.BOLD, TEXT_SIZE_HIGH));
                }

                // 根据数字大小设置数字显示的颜色
                if (board.getGrid()[row][column] >= 8) {
                    text.setFill(COLOR_VALUE_LIGHT);
                } else {
                    text.setFill(COLOR_VALUE_DARK);
                }

                // 把方格及其中的文字放置到pane上
                pane.add(rectangle, column, row);
                pane.add(text, column, row);
                GridPane.setHalignment(text, HPos.CENTER);
            }
        }
    }

    // 将界面用"Game Over!"字样覆盖
    private void gameOver() {
        // 建立一个矩形，用于覆盖界面
        Rectangle rectangle = new Rectangle(borderPane.getWidth(), borderPane.getHeight());
        rectangle.setFill(COLOR_GAME_OVER);

        // 建立"Game Over!"文字
        Text text = new Text();
        text.setText("Game Over!");
        text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 80));
        text.setFill(COLOR_OTHER);
        text.setX(20);
        text.setY(280);

        // 将矩形及文字放置到borderPane上
        borderPane.getChildren().addAll(rectangle, text);
    }

    // 键盘响应事件
    private class myKeyHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent e) {
            if (!board.isGameOver()) {
                switch (e.getCode()) {
                    // 按上下左右键进行移动
                    case UP:
                        if (board.canMove(Direction.UP)) {
                            board.move(Direction.UP);
                            board.addRandomTile();
                            setGui();
                        }
                        if (board.isGameOver()) {
                            gameOver();
                        }
                        break;

                    case DOWN:
                        if (board.canMove(Direction.DOWN)) {
                            board.move(Direction.DOWN);
                            board.addRandomTile();
                            setGui();
                        }
                        if (board.isGameOver()) {
                            gameOver();
                        }
                        break;

                    case LEFT:
                        if (board.canMove(Direction.LEFT)) {
                            board.move(Direction.LEFT);
                            board.addRandomTile();
                            setGui();
                        }
                        if (board.isGameOver()) {
                            gameOver();
                        }
                        break;

                    case RIGHT:
                        if (board.canMove(Direction.RIGHT)) {
                            board.move(Direction.RIGHT);
                            board.addRandomTile();
                            setGui();
                        }
                        if (board.isGameOver()) {
                            gameOver();
                        }
                        break;

                    // 按s进行保存
                    case S:
                        try {
                            board.saveBoard(outputBoard);
                            System.out.println("Saving Board to " + outputBoard);
                        } catch (IOException ie) {
                            System.out.println("saveBoard threw an Exception");
                        }

                    // 按u进行撤销
                    case U:
                        board.undo();
                        setGui();
                        break;

                    // 按r将游戏区域顺时针90度旋转
                    case R:
                        board.rotate(true);
                        setGui();
                        break;
                }
            }
        }
    }

    // The method used to process the command line arguments
    private void processArgs(String[] args) {
        String inputBoard = null;   // The filename for where to load the Board
        int boardSize = 0;          // The Size of the Board

        // Arguments must come in pairs
        if ((args.length % 2) != 0) {
            printUsage();
            System.exit(-1);
        }

        // Process all the arguments
        for (int i = 0; i < args.length; i += 2) {
            if (args[i].equals("-i")) {   // We are processing the argument that specifies
                // the input file to be used to set the board
                inputBoard = args[i + 1];
            } else if (args[i].equals("-o")) {   // We are processing the argument that specifies
                // the output file to be used to save the board
                outputBoard = args[i + 1];
            } else if (args[i].equals("-s")) {   // We are processing the argument that specifies
                // the size of the Board
                boardSize = Integer.parseInt(args[i + 1]);
            } else {   // Incorrect Argument
                printUsage();
                System.exit(-1);
            }
        }

        // Set the default output file if none specified
        if (outputBoard == null)
            outputBoard = "2048.board";
        // Set the default Board size if none specified or less than 2
        if (boardSize < 2)
            boardSize = 4;

        // Initialize the Game Board
        try {
            if (inputBoard != null)
                board = new Board(inputBoard, new Random());
            else
                board = new Board(boardSize, new Random());
        } catch (Exception e) {
            System.out.println(e.getClass().getName() +
                    " was thrown while creating a " +
                    "Board from file " + inputBoard);
            System.out.println("Either your Board(String, Random) " +
                    "Constructor is broken or the file isn't " +
                    "formated correctly");
            System.exit(-1);
        }
    }

    // Print the Usage Message
    private static void printUsage() {
        System.out.println("Gui2048");
        System.out.println("Usage:  Gui2048 [-i|o file ...]");
        System.out.println();
        System.out.println("  Command line arguments come in pairs of the " +
                "form: <command> <argument>");
        System.out.println();
        System.out.println("  -i [file]  -> Specifies a 2048 board that " +
                "should be loaded");
        System.out.println();
        System.out.println("  -o [file]  -> Specifies a file that should be " +
                "used to save the 2048 board");
        System.out.println("                If none specified then the " +
                "default \"2048.board\" file will be used");
        System.out.println("  -s [size]  -> Specifies the size of the 2048" +
                "board if an input file hasn't been");
        System.out.println("                specified.  If both -s and -i" +
                "are used, then the size of the board");
        System.out.println("                will be determined by the input" +
                " file. The default size is 4.");
    }
}
