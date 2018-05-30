package views;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
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
    private final int graphicHeigh = 600 ;

    private int tailleRect;

    public GridView(Map map){

        this.map = map;
        tailleRect = Math.floorDiv(graphicHeigh, map.getSize());
        map.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                // TODO : fill pane
            }
        });

        this.grid = new Pane();
        this.mainPane = new Pane();
        this.setHeight(graphicHeigh);
        this.setWidth(graphicWidth);

        this.mainGroup = new Group(grid);
        this.mainPane.getChildren().add(mainGroup);
        this.scene = new Scene(mainPane, graphicWidth, graphicHeigh);

        this.setScene(scene);
        this.setResizable(false);
        this.show();

    }

    private void fillPane(){
        grid.getChildren().clear();
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

    @Override
    public void close(){
        super.close();
    }
}
