package views;

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

    private int tailleRect;

    public GridView(Map map){

        this.map = map;
        tailleRect = Math.floorDiv(graphicHeigh, map.getSize())-1;
        map.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                fillPane();
            }
        });

        this.grid = new Pane();
        this.mainPane = new Pane();
        this.setHeight(graphicHeigh);
        this.setWidth(graphicWidth);

        grid.setPadding(new Insets(5));
        grid.setPrefSize(map.getSize()*tailleRect,map.getSize()*tailleRect);
        grid.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        grid.setLayoutX(5);
        grid.setLayoutY(5);


        this.mainGroup = new Group(grid);
        this.mainPane.getChildren().add(mainGroup);
        this.scene = new Scene(mainPane, graphicWidth, graphicHeigh);

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
                    Rectangle rectangle = new Rectangle(tailleRect,tailleRect);
                    rectangle.setX((col*tailleRect)+1);
                    rectangle.setY((line*tailleRect)+1);
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
            rectangle.setWidth(1);
            rectangle.setHeight(grid.getPrefHeight());
            rectangle.setLayoutY(1);
            rectangle.setLayoutX((x*sizeToOrdonnee)-1);
            rectangle.setFill(Color.GRAY);
            grid.getChildren().add(rectangle);
        }
        // parallèles aux abscisses
        for (int x=1 ; x<map.getSize() ; x++){
            Rectangle rectangle = new Rectangle();
            rectangle.setStroke(Color.GRAY);
            rectangle.setWidth(grid.getPrefWidth());
            rectangle.setHeight(1);
            rectangle.setLayoutX(1);
            rectangle.setLayoutY((x*sizeToAbscisse)-1);
            rectangle.setFill(Color.GRAY);
            grid.getChildren().add(rectangle);
        }
    }

    @Override
    public void close(){
        super.close();
    }
}
