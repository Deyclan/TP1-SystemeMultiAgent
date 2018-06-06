package views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import map.Map;

import java.util.Observable;
import java.util.Observer;

public class GridView extends Stage {

    private Map map;
    private Pane grid;
    private Pane mainPane;
    private Group mainGroup;
    private Scene scene;

    private final int graphicWidth = 600 ;
    private final int graphicHeigh = 650 ;

    private final int widthOffset = 75 ;
    private final int heightOffset = 50 ;

    private final int lineCorrection = 2;
    private final int rectPosCorrection = 4;
    private final int rectSizeCorrection = 2*rectPosCorrection;

    private int tailleRect;

    public GridView(Map map){

        this.map = map;
        tailleRect = Math.floorDiv(graphicHeigh, map.getSize());
        map.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                Platform.runLater(() -> fillPane());
            }
        });

        this.grid = new Pane();
        this.mainPane = new Pane();
        this.setHeight(graphicHeigh + heightOffset);
        this.setWidth(graphicWidth + widthOffset);

        grid.setPadding(new Insets(5));
        grid.setPrefSize(map.getSize()*tailleRect,map.getSize()*tailleRect);
        grid.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        grid.setLayoutX(5);
        grid.setLayoutY(5);


        this.mainGroup = new Group(grid);
        this.mainPane.getChildren().add(mainGroup);
        this.scene = new Scene(mainPane, graphicWidth , graphicHeigh);

        fillPane();
        this.setScene(scene);
        //this.setResizable(false);
        this.show();

    }

    private void fillPane(){
        grid.getChildren().clear();
        addGridLines();
        for (int line = 0; line < map.getSize(); line++ ) {
            for (int col = 0; col < map.getSize(); col++) {
                if (map.getGrille()[line][col] != null){
                    Rectangle rectangle = new Rectangle(tailleRect - rectSizeCorrection,tailleRect - rectSizeCorrection);
                    rectangle.setX((col*tailleRect)+rectPosCorrection);
                    rectangle.setY((line*tailleRect)+rectPosCorrection);
                    rectangle.setFill(map.getGrille()[line][col].getAgentColor());
                    grid.getChildren().addAll(rectangle);
                }
            }
        }
    }

    private void addGridLines(){
        double sizeToOrdonnee = Math.floor(grid.getPrefHeight()/map.getSize());
        double sizeToAbscisse = Math.floor(grid.getPrefWidth()/map.getSize());
        // parallèles aux ordonnées
        for (int x=1 ; x<map.getSize() ; x++){
            Rectangle rectangle = new Rectangle();
            rectangle.setStroke(Color.GRAY);
            rectangle.setWidth(0);
            rectangle.setHeight(grid.getPrefHeight() - lineCorrection);
            rectangle.setLayoutY(1);
            rectangle.setLayoutX((x*sizeToOrdonnee));
            rectangle.setFill(Color.GRAY);
            grid.getChildren().add(rectangle);
        }
        // parallèles aux abscisses
        for (int x=1 ; x<map.getSize() ; x++){
            Rectangle rectangle = new Rectangle();
            rectangle.setStroke(Color.GRAY);
            rectangle.setWidth(grid.getPrefWidth() - lineCorrection);
            rectangle.setHeight(0);
            rectangle.setLayoutX(1);
            rectangle.setLayoutY((x*sizeToAbscisse));
            rectangle.setFill(Color.GRAY);
            grid.getChildren().add(rectangle);
        }
    }

    @Override
    public void close(){
        Platform.runLater(()-> map.stopAgents());
        super.close();
    }
}
