package ru.ilyafx.sapper;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class SapperGUI extends Application {

    private static Map<Integer, String> colors = new HashMap<>();

    static {
        colors.put(0, "BLACK");
        colors.put(1, "BLUE");
        colors.put(2, "GREEN");
        colors.put(3, "YELLOW");
        colors.put(4, "PURPLE");
        colors.put(5, "BROWN");
        colors.put(6, "ORANGE");
        colors.put(7, "BLACK");
        colors.put(8, "RED");
    }

    private final Map<int[], Button> buttons = new HashMap<>();

    private AnchorPane pane;

    @Override
    public void start(Stage primaryStage) {
        SapperMap map = SapperMap.create(10,10,20);

        Button[] btns = new Button[100];
        int s = 0;
        for(int y = 0; y<10; y++) {
            for(int x = 0; x<10; x++) {
                Button bt = new Button(" ");
                bt.setLayoutX((x+1)*45);
                bt.setLayoutY((y+1)*35);
                buttons.put(new int[]{x,y},bt);
                final int xf = x, yf = y;
                bt.setOnMousePressed((event) -> {
                    process(event,xf,yf,map,primaryStage);
                });
                btns[s] = bt;
                s++;
            }
        }
        this.pane = new AnchorPane(btns);
        Scene scene = new Scene(pane,550, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void reload(SapperMap map, Stage stage) {
        for(int[] mas : buttons.keySet()) {
            Button bt = buttons.get(mas);
            SapperMap.Coordinate cord = map.get(mas[0],mas[1]);
            if(cord.isFlag()) {
                bt.setText("!");
                bt.setOnMousePressed((event) -> {
                    map.flag(mas[0],mas[1]);
                    reload(map,stage);
                });
            }
            if(cord.isOpen()) {
                bt.setOnMousePressed(event -> {});
                if(cord.isMine()) bt.setText("@");
                else {
                    int l = cord.getSize();
                    bt.setText("" + l);
                    bt.setStyle("-fx-background-color: " + colors.get(l) + ";-fx-text-fill:WHITE;");
                }
            }
            if(!cord.isOpen() && !cord.isFlag()) {
                    bt.setText("");
                    bt.setOnMousePressed((event) -> {
                        process(event, mas[0], mas[1], map, stage);
                    });
            }
        }
    }

    private void process(MouseEvent event, int xf, int yf, SapperMap map, Stage stage) {
        Button fin = new Button("Новая игра");
        fin.setOnMousePressed((even) -> {
            buttons.clear();
            try {
                start(stage);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        fin.setLayoutX(200);
        fin.setLayoutY(420);
        switch (event.getButton()) {
            case PRIMARY:
                if(map.getCoordinates().isEmpty()) map.build(xf,yf);
                if(!map.open(xf,yf)) {
                    new Alert(Alert.AlertType.INFORMATION,"Вы проебали, сударь").show();
                    pane.getChildren().add(fin);
                }
                reload(map, stage);
                break;
            case SECONDARY:
                if(map.getCoordinates().isEmpty()) return;
                if(map.flag(xf,yf)) {
                    new Alert(Alert.AlertType.INFORMATION,"Поздравляю с победой!").show();
                    pane.getChildren().add(fin);
                }
                reload(map, stage);
                break;
            default:
                break;
        }
    }

    public static void main(String[] args){
        launch(args);
    }
}
