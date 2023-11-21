import java.util.Scanner;
import java.util.Random;

/** ИНСТРУКЦИЯ
 *
 * Игра - Танки в консоли.
 *
 * ИГРОК:
 *
 * 1. Управление на WASD, каждый ход идет пошагово.
 * 2. Выстрелить на E, враг будет уничтожен если он будет вертикально или горизонтально относительно игрока.
 * 3. Можно уничтожить врага если сходить на ту же клетку, где он БУДЕТ стоять.
 *
 * ВРАГ:
 *
 * 1. Ходит ходит как и игрок пошагово в тех же направлениях.
 * 2. Может уничтожить игрока выстрелом по тому же принципе что и игрок врага.
 * 3. Враг может уничтожить вас свои следующим ходом если вы будете в радиусе 1 клетки от него.
 *
 * @noinspection ALL*/

public class TankGame {
    private char[][] gameBoard;
    private int boardSize = 5; // Размер игрового поля
    private int tankX, tankY; // Координаты танка
    private int enemyX, enemyY; // Координаты врага
    private boolean isGameOver = false;
    private int enemyShootDelay = 1; // Задержка перед выстрелом врага
    private int enemyShootCounter = 0;
    private boolean isEnemyDestroyed = false;
    private boolean isPlayerDestroyed = false;

    public TankGame() {
        gameBoard = new char[boardSize][boardSize];
        tankX = 0;
        tankY = 0;
        enemyX = 4;
        enemyY = 4;
        initializeGame();
    }

    private void initializeGame() {
        // Инициализируем игровое поле
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = ' ';
            }
        }
        gameBoard[tankX][tankY] = 'T'; // Помещаем танк на поле
        gameBoard[enemyX][enemyY] = 'E'; // Помещаем врага на поле
    }

    private void displayGameBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                System.out.print(gameBoard[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void checkCollision() {
        if (tankX == enemyX && tankY == enemyY) {
            isGameOver = true;
            isEnemyDestroyed = true;
        }
    }

    private void Enemy() {
        int moveDirection = new Random().nextInt(5); // Случайное направление движения
        gameBoard[enemyX][enemyY] = ' '; // Очищаем текущую позицию врага
        int targetX = enemyX;
        int targetY = enemyY;

        switch (moveDirection) {
            case 0: // Вверх
                if (enemyX > 0) {
                    enemyX--;
                }
                break;
            case 1: // Вниз
                if (enemyX < boardSize - 1) {
                    enemyX++;
                }
                break;
            case 2: // Влево
                if (enemyY > 0) {
                    enemyY--;
                }
                break;
            case 3: // Вправо
                if (enemyY < boardSize - 1) {
                    enemyY++;
                }
                break;
            case 4:
                if ((tankX == enemyX && tankY < enemyY) ||
                        (tankX == enemyX && tankY > enemyY) ||
                        (tankY == enemyY && tankX < enemyX) ||
                        (tankY == enemyY && tankX > enemyX)) {
                    gameBoard[tankX][tankY] = ' '; // Уничтожение врага
                    isPlayerDestroyed = true;
                }
                break;
        }
        if ((targetX == tankX && targetY == tankY) ||
                (targetX == tankX - 1 && targetY == tankY) ||
                (targetX == tankX + 1 && targetY == tankY) ||
                (targetX == tankX && targetY == tankY + 1) ||
                (targetX == tankX && targetY == tankY - 1) ||
                (targetX == tankX + 1 && targetY == tankY + 1) ||
                (targetX == tankX - 1 && targetY == tankY - 1) ||
                (targetX == tankX + 1 && targetY == tankY - 1) ||
                (targetX == tankX - 1 && targetY == tankY + 1)){
            isGameOver = true;
            isPlayerDestroyed = true;
        }

        gameBoard[enemyX][enemyY] = 'E'; // Перемещаем врага на новую позицию
    }

    public void playGame() {
        Scanner scanner = new Scanner(System.in);
        char move;

        while (!isGameOver) {
            displayGameBoard();
            System.out.println("Введите команду (W - вверх, A - влево, S - вниз, D - вправо, E - выстрел, Q - выход): ");
            move = scanner.next().charAt(0);

            // Двигаем врага на каждом ходу
            Enemy();

            switch (move) {
                case 'W':
                    if (tankX > 0) {
                        gameBoard[tankX][tankY] = ' ';
                        tankX--;
                        gameBoard[tankX][tankY] = 'T';
                    }
                    break;
                case 'A':
                    if (tankY > 0) {
                        gameBoard[tankX][tankY] = ' ';
                        tankY--;
                        gameBoard[tankX][tankY] = 'T';
                    }
                    break;
                case 'S':
                    if (tankX < boardSize - 1) {
                        gameBoard[tankX][tankY] = ' ';
                        tankX++;
                        gameBoard[tankX][tankY] = 'T';
                    }
                    break;
                case 'D':
                    if (tankY < boardSize - 1) {
                        gameBoard[tankX][tankY] = ' ';
                        tankY++;
                        gameBoard[tankX][tankY] = 'T';
                    }
                    break;
                case 'E':
                    // Проверка, был ли выстрел успешным
                    if ((tankX == enemyX && tankY < enemyY) ||
                            (tankX == enemyX && tankY > enemyY) ||
                            (tankY == enemyY && tankX < enemyX) ||
                            (tankY == enemyY && tankX > enemyX)) {
                        gameBoard[enemyX][enemyY] = ' '; // Уничтожение врага
                        isEnemyDestroyed = true;
                    }
                    break;
                case 'Q':
                    isGameOver = true;
                    break;
                default:
                    System.out.println("Неверная команда.");
            }

            checkCollision();

            if (isPlayerDestroyed) {
                System.out.println("Игрок уничтожен! Игра завершена.");
                isGameOver = true;

            } else if (isEnemyDestroyed) {
                System.out.println("Враг уничтожен! Игра завершена.");
                isGameOver = true;
            }

        }
    }

    public static void main(String[] args) {
        TankGame game = new TankGame();
        game.playGame();
    }
}