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
