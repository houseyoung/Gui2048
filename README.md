# Gui2048
基于JavaFX的2048游戏。

2048 Game based on JavaFX.

## 如何运行本游戏 | How to run this game
1.在命令提示符(Windows)/终端(OS X或Linux)中进入**src**目录。
<br>
2.执行**javac *.java**
<br>
3.执行**java Gui2048** (可在本命令后附加一些参数，详情见下文。)

1.Open **src** folder in Command Prompt(Windows)/Terminal(OS X or Linux).
<br>
2.Execute **javac *.java**
<br>
3.Execute **java Gui2048** (You can add some command line arguments after this command, see below for details.)

## 如何玩本游戏 | How to play this game
**方向键** -> 控制方块移动。
<br>
**s键** -> 保存游戏。
<br>
**u键** -> 撤销移动。只可撤销一步。
<br>
**r键** -> 将所有方块顺时针移动。

**Arrow keys** -> Move the tiles.
<br>
**"s" key** -> Save the board.
<br>
**"u" key** -> Undo. User can only do undo once at a time.
<br>
**"r" key** -> Rotates the board by 90 degrees clockwise.

## 命令行参数 |  Command line arguments
**-s [size]** -> 设置游戏的维数。若不使用该参数，游戏维数默认为4x4。
<br>
**-i [file]**  -> 从保存的文件中读取游戏。当-i与-s参数同时存在时，-s参数不起作用，游戏采用保存的文件中定义的维度。
<br>
**-o [file]**  -> 设置保存游戏时存储的文件名。若不使用该参数，默认使用"2048.board"存储。

**-s [size]** -> Specifies the size of the 2048 board, the default size is 4.
<br>
**-i [file]**  -> Specifies a 2048 board that should be loaded. If both -s and -i are used, then the size of the board will be determined by the input file.
<br>
**-o [file]**  -> Specifies a file that should be used to save the 2048 board. If none specified then the default "2048.board" file will be used.
