//Класс, ответственный за игровую логику

package MinesweeperDemo;

import java.util.Random;

public class Logic {

    private static final int gridWidth = 8; //Ширина сетки в ячейках
    private static final int gridHeight = 8; //Высота сетки в ячейках
    private static final int minesNumber = 10; //Количество мин

    //Матрица состояний ячеек. Фактически отвечает за то, какую текстуру рисовать на экране
    private static final byte[][] condition = new byte[gridHeight][gridWidth];
    //Матрица мин. Не покрывается матрицей состояний, поэтому вынесена в отдельный массив
    private static final boolean[][] mines = new boolean[gridHeight][gridWidth];

    //Запущена ли игра. Фактически - можно ли нажимать на сетку
    private static boolean isRunning = false;
    //Первый клик не должен попадать на мину. Переменная показывает, является ли клик первым.
    //Генерация мин происходит уже после первого клика
    private static boolean firstClick = false;
    //Состояние игры (идёт, выиграна или проиграна). От него зависит иконка сапера
    private static byte gameCondition = 0;

    //Геттеры
    public static int getGridWidth() {
        return gridWidth;
    }
    public static int getGridHeight() {
        return gridHeight;
    }
    public static byte[][] getCondition() {
        return condition;
    }
    public static boolean isIsRunning(){
        return isRunning;
    }
    public static boolean isFirstClick(){
        return firstClick;
    }
    public static byte getGameCondition(){
        return gameCondition;
    }

    //Переводы координат нажатия курсором в номера строк/столбцов ячеек
    private static int xToJ(int x){
        return (x-Renderer.getCellSide())/Renderer.getCellSide();
    }
    private static int yToI(int y){
        return (y-Renderer.getCellSide()*4)/Renderer.getCellSide();
    }

    //Было ли нажатие осуществлено в пределах сетки
    public static boolean isInGrid(int x, int y){
        return x > Renderer.getCellSide() && x <= Renderer.getCellSide() * (gridWidth + 1) &&
                y > Renderer.getCellSide() * 4 && y <= Renderer.getCellSide() * (gridHeight + 4);
    }

    //Было ли нажатие осуществлено в пределах кнопки рестарта (иконки сапера)
    public static boolean isInRestart(int x, int y){
        return x > Renderer.getCellSide() * (gridWidth / 2) && x <= Renderer.getCellSide() * (gridWidth / 2 + 2) &&
                y > Renderer.getCellSide() && y <= Renderer.getCellSide() * 3;
    }

    //Начало игры (до первого клика)
    public static void start(){
        for(byte i = 0; i<gridHeight; i++)
            for(byte j = 0; j<gridWidth; j++){
                condition[i][j] = 9;
                mines[i][j] = false;
            }
        isRunning = true;
        firstClick = true;
        gameCondition = 0;
    }

    //Генерация мин по первому клику
    public static void generateGrid(int x, int y){
        if(isInGrid(x,y)) {
            int n = 0, i, j;
            Random rnd = new Random();
            while (n < minesNumber) {
                i = rnd.nextInt(gridHeight - 1);
                j = rnd.nextInt(gridWidth - 1);
                if (mines[i][j] || (i == yToI(y) && j == xToJ(x))) continue;
                mines[i][j] = true;
                n++;
            }
            openSafeCell(yToI(y),xToJ(x));
            firstClick = false;
        }
    }

    //Подсчёт мин вокруг ячейки
    public static byte minesCounter(int celli, int cellj){
        byte count = 0;
        int starti, endi, startj, endj;
        if(celli == 0) starti = 0; else starti = celli-1;
        if(celli == gridHeight-1) endi = gridHeight-1; else endi = celli+1;
        if(cellj == 0) startj = 0; else startj = cellj-1;
        if(cellj == gridWidth-1) endj = gridWidth-1; else endj = cellj+1;
        for (int i = starti; i <= endi; i++)
            for (int j = startj; j <= endj; j++){
                if(i == celli && j == cellj) continue;
                if(mines[i][j]) count++;
            }
        return count;
    }

    //Открытие безопасной ячейки
    private static void openSafeCell(int celli, int cellj){
        condition[celli][cellj] = minesCounter(celli,cellj);
        if(minesCounter(celli,cellj) == 0){
            int starti, endi, startj, endj;
            if(celli == 0) starti = 0; else starti = celli-1;
            if(celli == gridHeight-1) endi = gridHeight-1; else endi = celli+1;
            if(cellj == 0) startj = 0; else startj = cellj-1;
            if(cellj == gridWidth-1) endj = gridWidth-1; else endj = cellj+1;
            for (int i = starti; i <= endi; i++)
                for (int j = startj; j <= endj; j++){
                    if(condition[i][j] == 9 || condition[i][j] == 10)
                        openSafeCell(i,j);
                }
        }
        if(isWon()){
            isRunning = false;
            gameCondition = 1;
        }
    }

    //Открытие ячейки с миной и геймовер
    private static void explode(int celli, int cellj){
        condition[celli][cellj] = 11;
        for(byte i = 0; i<gridHeight; i++)
            for(byte j = 0; j<gridWidth; j++){
                if(condition[i][j] == 10 && !mines[i][j])
                    condition[i][j] = 13;
                else if(condition[i][j] == 9 && mines[i][j])
                    condition[i][j] = 12;
            }
        isRunning = false;
        gameCondition = 2;
    }

    //Открытие ячейки
    public static void openCell(int x, int y){
        if(isInGrid(x,y) && condition[yToI(y)][xToJ(x)] == 9){
            int i = yToI(y);
            int j = xToJ(x);
            if(!mines[i][j]){
                openSafeCell(i,j);
            }
            else {
                explode(i,j);
            }
        }
    }

    //Установка или снятие флага на ячейке
    public static void flag(int x, int y){
        if(isInGrid(x,y) && condition[yToI(y)][xToJ(x)] == 9)
            condition[yToI(y)][xToJ(x)] = 10;
        else if(isInGrid(x,y) && condition[yToI(y)][xToJ(x)] == 10)
            condition[yToI(y)][xToJ(x)] = 9;
    }

    //Проверка на выигрыш
    public static boolean isWon(){
        for(int i = 0; i<gridHeight; i++)
            for(int j = 0; j<gridWidth; j++)
                if((condition[i][j] == 9 && !mines[i][j]) || (condition[i][j] == 10 && !mines[i][j]))
                    return false;
        return true;
    }
}
