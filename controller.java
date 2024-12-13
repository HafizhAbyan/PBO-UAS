package snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGameController {

    @FXML
    private Canvas gameCanvas;
    private GraphicsContext gc;
    private Timeline timeline, yellowFoodTimeline, greenFoodTimeline, wallTimeline;

    private Snake snake;
    private Food food, yellowFood, greenFood;
    private boolean gameOver = false;
    private int score = 0;
    
    private double rectWidth = 600;
    private double rectHeight = 400;

    private enum Direction {UP, DOWN, LEFT, RIGHT} 

    private Direction direction = Direction.RIGHT;
    private Direction nextDirection = Direction.RIGHT;

    private Timeline shrinkRectangleTimeline;

    public void initialize() {
        gc = gameCanvas.getGraphicsContext2D();
        drawStartScreen();

        gameCanvas.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                setupKeyEvents(newScene);
            }
        });

        startSpecialFoods();
        startShrinkingRectangle(); 
    }

    private void setupKeyEvents(javafx.scene.Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && timeline == null) {
                gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
                initGame();
                startGame();
            } else {
                handleInput(e.getCode());
            }
        });
    }

    private void handleInput(KeyCode code) {
        if (code == KeyCode.UP && direction != Direction.DOWN) {
            nextDirection = Direction.UP;
        } else if (code == KeyCode.DOWN && direction != Direction.UP) {
            nextDirection = Direction.DOWN;
        } else if (code == KeyCode.LEFT && direction != Direction.RIGHT) {
            nextDirection = Direction.LEFT;
        } else if (code == KeyCode.RIGHT && direction != Direction.LEFT) {
            nextDirection = Direction.RIGHT;
        }
    }

    private void initGame() {
        snake = new Snake(15, 10); 
        spawnFood();
        gameOver = false;
    }

    private void startGame() {
        timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> run())); 
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void startShrinkingRectangle() {
        shrinkRectangleTimeline = new Timeline(
            new KeyFrame(Duration.seconds(1), e -> shrinkRectangle()) // Polymorphism: KeyFrame digunakan untuk memanggil fungsi shrinkRectangle.
        );
        shrinkRectangleTimeline.setCycleCount(Timeline.INDEFINITE);
        shrinkRectangleTimeline.play();
    }

    private void shrinkRectangle() {
        if (rectWidth > 0 && rectHeight > 0) {
            rectWidth -= 2;
            rectHeight -= 2;
        }
    }

        private void run() {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font(40));
            gc.fillText("Game Over", 200, 200);
            timeline.stop();
            return;
        }

        update();
        draw();
    }

    private void update() {
        direction = nextDirection;

        int dx = 0, dy = 0;
        switch (direction) {
            case UP: dy = -1; break;
            case DOWN: dy = 1; break;
            case LEFT: dx = -1; break;
            case RIGHT: dx = 1; break;
        }

        snake.move(dx, dy);

        int[] head = snake.getBody().get(0);

        double left = (gameCanvas.getWidth() - rectWidth) / 2;
        double top = (gameCanvas.getHeight() - rectHeight) / 2;
        double right = left + rectWidth;
        double bottom = top + rectHeight;

        try {
            if (head[0] * 20 < left || head[0] * 20 >= right || head[1] * 20 < top || head[1] * 20 >= bottom) {
                gameOver = true;
                return;
            }

            for (int i = 1; i < snake.getBody().size(); i++) {
                if (head[0] == snake.getBody().get(i)[0] && head[1] == snake.getBody().get(i)[1]) {
                    gameOver = true;
                }
            }

            if (head[0] == food.getX() && head[1] == food.getY()) {
                snake.grow();
                score++;
                spawnFood();
                growRectangle(10);
            }

            if (yellowFood != null && head[0] == yellowFood.getX() && head[1] == yellowFood.getY()) {
                snake.grow();
                score += 2;
                yellowFood = null;
                growRectangle(15);
            }

            if (greenFood != null) {
                if (head[0] == greenFood.getX() && head[1] == greenFood.getY()) {
                    snake.grow();
                    score += 3;
                    greenFood = null;
                    growRectangle(20);
                } else {
                    greenFood.moveRandomly();
                }
            }
        } catch (Exception e) {
            System.err.println("Error in game update: " + e.getMessage()); // Error/Exception handling: Menangani kemungkinan error dalam permainan.
            gameOver = true;
        }
    }
