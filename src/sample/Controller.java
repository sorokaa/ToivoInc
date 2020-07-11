package sample;

import MusicList.ListOfMusic;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class Controller {

    public Rectangle rect;
    public Button menuBtn;
    private MediaPlayer mediaPlayer;
    private ListOfMusic l;
    private String path;

    @FXML
    private Button play;
    @FXML
    private Pane test;
    @FXML
    private VBox drawer;

    @FXML
    void initialize() {
    }

    private boolean played = false;

    public void playMusic() {
        if(path == null || path.equals("")) {
            return;
        }
        if (!played) {
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }
        played = !played;
    }

    private void setTrack() {
        path = l.getNextTrack();
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
    }

    private void addTracksFromPath() {
        File repos = new File(path);
        File[] files = repos.listFiles();
        if(files == null) {
            return;
        }
        l = new ListOfMusic();
        for(File f : files) {
            if(f.toString().contains(".mp3")) {
                l.add(f.getAbsolutePath());
            }
        }
    }

    private void set(int t) throws InterruptedException {
        Thread.sleep(500);
        test.setMinHeight(t);
    }


    public void chooseDir(ActionEvent actionEvent) throws InterruptedException {

        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        if (drawer.getTranslateY() == 0) {
            openNav.setToY(141);
            openNav.play();
        } else {
            closeNav.setToY(0);
            closeNav.play();
        }

        if(l == null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            path = directoryChooser.showDialog(new Stage()).getAbsolutePath();
            addTracksFromPath();
            if(l.getSize() == 0) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("INFORMATION");
                a.setContentText("Haven't mp3 files. Try another fold");
                a.show();
            }
        }
        setTrack();
    }

    public void prevTrack(ActionEvent actionEvent) {

    }

    public void nextTrack(ActionEvent actionEvent) {
        String path = l.getNextTrack();
        if(path.equals("")) {
            System.out.println("Errord");
            return;
        }
        played = false;
        playMusic();
    }
}
