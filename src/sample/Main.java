package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stage;
    private static double oldWidthOfStage;
    private static double oldHeightOfStage;

    static void setNewSizeOfStage(double width, double height) {
        oldHeightOfStage = stage.getHeight();
        oldWidthOfStage = stage.getWidth();
        stage.setWidth(width);
        stage.setHeight(height);
    }

    static void setOldSizeOfStage() {
        stage.setWidth(oldWidthOfStage);
        stage.setHeight(oldHeightOfStage);
    }

    static void setOnTop() {
        if(stage.isAlwaysOnTop()) {
            stage.setAlwaysOnTop(false);
            Rectangle2D rect = Screen.getPrimary().getBounds();
            stage.setX((rect.getWidth() - stage.getWidth())/2);
            stage.setY((rect.getHeight() - stage.getHeight())/2);
        } else {
            stage.setX(0);
            stage.setY(0);
            stage.setAlwaysOnTop(true);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage = primaryStage;
        primaryStage.setTitle("ToivoInc");
        Image im = new Image("images/logo.png");
        primaryStage.getIcons().setAll(im);
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}

