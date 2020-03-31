package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Bezierova křivka");
        primaryStage.setScene(new Scene(root, 300, 275, Color.WHITE));
        primaryStage.setWidth(820);
        primaryStage.setHeight(830);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
