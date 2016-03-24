/**
 * Sample Board
 * <p/>
 * 0   1   2   3
 * 0   -   -   -   -
 * 1   -   -   -   -
 * 2   -   -   -   -
 * 3   -   -   -   -
 * <p/>
 * The sample board shows the index values for the columns and rows
 * Remember that you access a 2D array by first specifying the row
 * and then the column: grid[row][column]
 */

import java.util.*;
import java.io.*;

public class Board {
    public final int NUM_START_TILES = 2;
    public final int TWO_PROBABILITY = 90;
    public final int GRID_SIZE;


    private final Random random;
    private int[][] grid;
    private int score;

    //用来存放撤销操作时所用的数据
    private int[][] undoGrid;
    private int undoScore;

    // Constructs a fresh board with random tiles
    public Board(int boardSize, Random random) {
        //初始化random
        this.random = random;

        //把GRID_SIZE设置为boardSize
        GRID_SIZE = boardSize;

        //按照给定的Size建立二维数组
        grid = new int[GRID_SIZE][GRID_SIZE];

        //将Board中所有的位置都先设置为0
        for (int row = 0; row < GRID_SIZE; row++){
            for (int column = 0; column < GRID_SIZE; column++){
                grid[row][column] = 0;
            }
        }

        //生成几个随机块
        for (int i = 0; i < NUM_START_TILES; i++){
            addRandomTile();
        }

    }

    // Construct a board based off of an input file
    public Board(String inputBoard, Random random) throws IOException {
        //初始化random
        this.random = random;

        //配置Scanner以从文件中读取数据
        Scanner input = new Scanner(new File(inputBoard));

        //读取boardSize并把GRID_SIZE设置为boardSize
        int boardSize = input.nextInt();
        GRID_SIZE = boardSize;

        //读取得分
        score = input.nextInt();

        //按照给定的Size建立二维数组
        grid = new int[GRID_SIZE][GRID_SIZE];

        //读取文件中存储的Board的内容，存入grid
        int row = 0;
        int column = 0;
        while(input.hasNextInt()){
            //若一行还没读完，则继续读本行的数据
            if (column < GRID_SIZE) {
                grid[row][column] = input.nextInt();
                column++;
            }
            //若一行已读完，则开始读下一行的数据
            else {
                row++;
                column = 0;
                grid[row][column] = input.nextInt();
                column++;
            }
        }

        //关闭Scanner
        input.close();
    }

    // Saves the current board to a file
    public void saveBoard(String outputBoard) throws IOException {
        //将outputBoard作为文件名，生成文件
        File file = new File(outputBoard);

        //创建一个PrintWriter对象
        PrintWriter printWriter = new PrintWriter(file);

        //输出boardSize
        printWriter.println(GRID_SIZE);

        //输出得分
        printWriter.println(score);

        //输出Board
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                printWriter.print(grid[row][column] + " ");

                //如果是一行的结尾，则输出数字后加一个回车
                if (column == GRID_SIZE - 1) {
                    printWriter.print("\n");
                }
            }
        }

        //关闭PrintWriter
        printWriter.close();
    }

    // Adds a random tile (of value 2 or 4) to a
    // random empty space on the board
    public void addRandomTile() {
        //计算可以放置新数字的位置的总量
        int count = 0;
        for (int row = 0; row < GRID_SIZE; row++){
            for (int column = 0; column < GRID_SIZE; column++){
                //为0的位置可放置新数字
                if (grid[row][column] == 0) {
                    count++;
                }
            }
        }

        //若没有可放置新数字的位置，直接返回
        if (count == 0) {
            return;
        }

        //随机生成放置新数字的位置
        int location = random.nextInt(count);

        //生成一个0-99的随机数
        int value = random.nextInt(100);

        //放置新数字
        int i = 0;
        for (int row = 0; row < GRID_SIZE; row++){
            for (int column = 0; column < GRID_SIZE; column++){
                if (grid[row][column] == 0) {
                    if (i == location) {
                        //如果生成的随机数小于TWO_PROBABILITY，放置的新数字为2；否则为4
                        if (value < TWO_PROBABILITY) {
                            grid[row][column] = 2;
                        } else {
                            grid[row][column] = 4;
                        }
                    }

                    i++;
                }
            }
        }
    }

    // Rotates the board by 90 degrees clockwise or 90 degrees counter-clockwise.
    // If rotateClockwise == true, rotates the board clockwise , else rotates
    // the board counter-clockwise
    public void rotate(boolean rotateClockwise) {
        //临时数组，用于记录旋转中的数组
        int[][] temp = new int[GRID_SIZE][GRID_SIZE];

        //顺时针旋转
        if (rotateClockwise == true) {
            for (int row = 0; row < GRID_SIZE; row++){
                for (int column = 0; column < GRID_SIZE; column++){
                    temp[row][column] = grid[GRID_SIZE - column - 1][row];
                }
            }
        }

        //逆时针旋转
        if (rotateClockwise == false) {
            for (int row = 0; row < GRID_SIZE; row++){
                for (int column = 0; column < GRID_SIZE; column++){
                    temp[GRID_SIZE - column - 1][row] = grid[row][column];
                }
            }
        }

        //将转换完的数组赋给grid
        grid = temp;
    }

    //Complete this method ONLY if you want to attempt at getting the extra credit
    //Returns true if the file to be read is in the correct format, else return
    //false
    public static boolean isInputFileCorrectFormat(String inputFile) {
        //The try and catch block are used to handle any exceptions
        //Do not worry about the details, just write all your conditions inside the
        //try block
        try {
            //配置Scanner以从文件中读取数据
            Scanner input = new Scanner(new File(inputFile));

            //以String格式读取Board的大小
            String boardSizeStr = input.next();
            //转换为int格式
            int boardSize = Integer.parseInt(boardSizeStr);
            //若boardSize小于2，则证明输入文件存在错误
            if (boardSize < 2) {
                return false;
            }

            //以String格式读取得分
            String scoreStr = input.next();
            //转换为int格式
            int score = Integer.parseInt(scoreStr);
            //若得分不为2的倍数，则证明输入文件存在错误
            if (score % 2 != 0) {
                return false;
            }

            //用一个ArrayList来读取Board中的内容
            List<Integer> gridList = new ArrayList<Integer>();
            while (input.hasNextInt()) {
                gridList.add(input.nextInt());
            }

            //若ArrayList的大小不为boardSize的平方，则证明输入文件存在错误
            if (gridList.size() != (boardSize * boardSize)) {
                return false;
            }

            //若Board中含有不为2的倍数的数字，则证明输入文件存在错误
            for (int i : gridList) {
                if (i % 2 != 0) {
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //移动操作
    public boolean move(Direction direction) {
        //把原有的二维数组及得分记录下来，撤销时使用
        undoGrid = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++){
            for (int column = 0; column < GRID_SIZE; column++){
                undoGrid[row][column] = grid[row][column];
            }
        }
        undoScore = score;

        if (direction == Direction.UP) {
            return moveUp();
        }

        if (direction == Direction.DOWN) {
            return moveDown();
        }

        if (direction == Direction.LEFT) {
            return moveLeft();
        }

        if (direction == Direction.RIGHT) {
            return moveRight();
        }

        return false;
    }

    public boolean moveUp() {
        //从最下面一行开始，先将所有数字都移动到最上方
        for (int row = GRID_SIZE - 1; row > 0 ; row--) {
            for (int column = 0; column < GRID_SIZE; column++) {
                //若上一行是0，则将本行及本行之下的每一行都上移
                if (row > 0 && grid[row - 1][column] == 0) {
                    for (int i = row; i < GRID_SIZE; i++) {
                        grid[i - 1][column] = grid[i][column];
                        grid[i][column] = 0;
                    }
                }
            }
        }

        //从第一行开始依次合并
        //注意：只合并相邻两行，例如8 4 2 2将被合并为8 4 4 0，而不是16 0 0 0（这句只是为了提示你，交的时候删了）
        for (int row = 0; row < GRID_SIZE - 1; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                //若本行与下一行数字相同，则合并至本行
                if (grid[row][column] == grid[row + 1][column]) {
                    grid[row][column] += grid[row + 1][column];
                    grid[row + 1][column] = 0;

                    //计分
                    score += grid[row][column];
                }
            }
        }

        //合并完后再次将所有数字都移动到最上方
        //注意：如果不做这一步，4 4 2 2将被合并为8 0 4 0，实际上应该被合并为8 4 0 0（这句只是为了提示你，交的时候删了）
        for (int row = GRID_SIZE - 1; row > 0 ; row--) {
            for (int column = 0; column < GRID_SIZE; column++) {
                //若上一行是0，则将本行及本行之下的每一行都上移
                if (row > 0 && grid[row - 1][column] == 0) {
                    for (int i = row; i < GRID_SIZE; i++) {
                        grid[i - 1][column] = grid[i][column];
                        grid[i][column] = 0;
                    }
                }
            }
        }

        return true;
    }

    public boolean moveDown() {
        //从最上面一行开始，先将所有数字都移动到最下方
        for (int row = 0; row < GRID_SIZE - 1; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                //若下一行是0，则将本行及本行之上的每一行都下移
                if (row < GRID_SIZE && grid[row + 1][column] == 0) {
                    for (int i = row; i >= 0; i--) {
                        grid[i + 1][column] = grid[i][column];
                        grid[i][column] = 0;
                    }
                }
            }
        }

        //从最后一行开始依次合并
        for (int row = GRID_SIZE - 1; row > 0 ; row--) {
            for (int column = 0; column < GRID_SIZE; column++) {
                //若本行与上一行数字相同，则合并至本行
                if (grid[row][column] == grid[row - 1][column]) {
                    grid[row][column] += grid[row - 1][column];
                    grid[row - 1][column] = 0;

                    //计分
                    score += grid[row][column];
                }
            }
        }

        //合并完后再次将所有数字都移动到最下方
        for (int row = 0; row < GRID_SIZE - 1; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                //若下一行是0，则将本行及本行之上的每一行都下移
                if (row < GRID_SIZE && grid[row + 1][column] == 0) {
                    for (int i = row; i >= 0; i--) {
                        grid[i + 1][column] = grid[i][column];
                        grid[i][column] = 0;
                    }
                }
            }
        }

        return true;
    }

    public boolean moveLeft() {
        //从最右一列开始，先将所有数字都移动到最左方
        for (int column = GRID_SIZE - 1; column > 0; column--) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //若左一列是0，则将本列及本列之右的每一列都左移
                if (column > 0 && grid[row][column - 1] == 0) {
                    for (int i = column; i < GRID_SIZE; i++) {
                        grid[row][i - 1] = grid[row][i];
                        grid[row][i] = 0;
                    }
                }
            }
        }

        //从最左一列开始依次合并
        for (int column = 0; column < GRID_SIZE - 1; column++) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //若本列与右一列数字相同，则合并至本列
                if (grid[row][column] == grid[row][column + 1]) {
                    grid[row][column] += grid[row][column + 1];
                    grid[row][column + 1] = 0;

                    //计分
                    score += grid[row][column];
                }
            }
        }

        //合并完后再次将所有数字都移动到最左方
        for (int column = GRID_SIZE - 1; column > 0; column--) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //若左一列是0，则将本列及本列之右的每一列都左移
                if (column > 0 && grid[row][column - 1] == 0) {
                    for (int i = column; i < GRID_SIZE; i++) {
                        grid[row][i - 1] = grid[row][i];
                        grid[row][i] = 0;
                    }
                }
            }
        }

        return true;
    }

    public boolean moveRight() {
        //从最左一列开始，先将所有数字都移动到最右方
        for (int column = 0; column < GRID_SIZE - 1; column++) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //若右一列是0，则将本列及本列之左的每一列都右移
                if (column < GRID_SIZE && grid[row][column + 1] == 0) {
                    for (int i = column; i >= 0; i--) {
                        grid[row][i + 1] = grid[row][i];
                        grid[row][i] = 0;
                    }
                }
            }
        }

        //从最右一列开始依次合并
        for (int column = GRID_SIZE - 1; column > 0 ; column--) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //若本列数字与左一列相同，则合并至本列
                if (grid[row][column] == grid[row][column - 1]) {
                    grid[row][column] += grid[row][column - 1];
                    grid[row][column - 1] = 0;

                    //计分
                    score += grid[row][column];
                }
            }
        }

        //合并完后再次将所有数字都移动到最右方
        for (int column = 0; column < GRID_SIZE - 1; column++) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //若右一列是0，则将本列及本列之左的每一列都右移
                if (column < GRID_SIZE && grid[row][column + 1] == 0) {
                    for (int i = column; i >= 0; i--) {
                        grid[row][i + 1] = grid[row][i];
                        grid[row][i] = 0;
                    }
                }
            }
        }

        return true;
    }

    //判断游戏是否结束(如果上下左右都不能移动了就结束了)
    public boolean isGameOver() {
        if (canMoveUp() == true) {
            return false;
        }

        if (canMoveDown() == true) {
            return false;
        }

        if (canMoveLeft() == true) {
            return false;
        }

        if (canMoveRight() == true) {
            return false;
        }

        return true;
    }

    //检测是否可以向某方向移动
    public boolean canMove(Direction direction) {
        if (direction == Direction.UP) {
            return canMoveUp();
        }

        if (direction == Direction.DOWN) {
            return canMoveDown();
        }

        if (direction == Direction.LEFT) {
            return canMoveLeft();
        }

        if (direction == Direction.RIGHT) {
            return canMoveRight();
        }

        return false;
    }

    public boolean canMoveUp() {
        //从最后一行开始往上检测是否可以移动
        for (int row = GRID_SIZE - 1; row > 0 ; row--) {
            for (int column = 0; column < GRID_SIZE; column++) {
                //若上一行是0且本行不为0，则肯定可以移动，return true
                if (grid[row - 1][column] == 0 && grid[row][column] != 0) {
                    return true;
                }
                //若上一行与本行数字相同且不为0，则肯定可以移动，return true
                if (grid[row - 1][column] == grid[row][column] && grid[row][column] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canMoveDown() {
        //从第一行开始往下检测是否可以移动
        for (int row = 0; row < GRID_SIZE - 1 ; row++) {
            for (int column = 0; column < GRID_SIZE; column++) {
                //若下一行是0且本行不为0，则肯定可以移动，return true
                if (grid[row + 1][column] == 0 && grid[row][column] != 0) {
                    return true;
                }
                //若下一行与本行数字相同且不为0，则肯定可以移动，return true
                if (grid[row + 1][column] == grid[row][column] && grid[row][column] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canMoveLeft() {
        //从最右一列开始往左检测是否可以移动
        for (int column = GRID_SIZE - 1; column > 0 ; column--) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //若左一列是0且本列不为0，则肯定可以移动，return true
                if (grid[row][column - 1] == 0 && grid[row][column] != 0) {
                    return true;
                }
                //若左一列与本列数字相同且不为0，则肯定可以移动，return true
                if (grid[row][column - 1] == grid[row][column] && grid[row][column] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canMoveRight() {
        //从最左一列开始往右检测是否可以移动
        for (int column = 0; column < GRID_SIZE - 1 ; column++) {
            for (int row = 0; row < GRID_SIZE; row++) {
                //若右一列是0且本列不为0，则肯定可以移动，return true
                if (grid[row][column + 1] == 0 && grid[row][column] != 0) {
                    return true;
                }
                //若右一列与本列数字相同且不为0，则肯定可以移动，return true
                if (grid[row][column + 1] == grid[row][column] && grid[row][column] != 0) {
                    return true;
                }
            }
        }

        return false;
    }

    //撤销操作
    public void undo() {
        //当二维数组undoGrid为空时，证明游戏开始后第一次执行的操作就是undo操作，此时没有什么可以undo的
        //因而无需执行undo操作。
        if (undoGrid != null) {
            grid = undoGrid;
            score = undoScore;
        }
    }

    // Return the reference to the 2048 Grid
    public int[][] getGrid() {
        return grid;
    }

    // Return the score
    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", score));
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int column = 0; column < GRID_SIZE; column++)
                outputString.append(grid[row][column] == 0 ? "    -" :
                        String.format("%5d", grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
