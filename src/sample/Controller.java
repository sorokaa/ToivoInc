package sample;

import MusicList.ListOfMusic;
import javafx.animation.TranslateTransition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;


public class Controller {


    public Button menuBtn;
    public Label currentTrackLabel;
    public Button chooseDirBtn;
    public ListView<String> listOfMusic;
    public Slider volumeSlider;
    private MediaPlayer mediaPlayer;
    private ListOfMusic l;
    private String path;

    @FXML
    private Button play;
    @FXML
    private VBox drawer;

    @FXML
    void initialize() {

    }

    private boolean played = false;

    private String makeTextForLabel(String path) {
        String result = path.substring(path.lastIndexOf("\\") + 1);
        result = result.replaceAll("[-_]", " ");
        result = result.replaceAll(".mp3", "");
        return result.toUpperCase();
    }

    public void playMusic() {
        if(path == null || path.equals("")) {
            return;
        }
        currentTrackLabel.setText(makeTextForLabel(path));
        if (!played) {
            play.setStyle("-fx-background-image:url('pause.png');");
            mediaPlayer.play();
        } else {
            play.setStyle("-fx-background-image:url('playImg.png');");
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
                listOfMusic.getItems().add(makeTextForLabel(f.getAbsolutePath()));
            }
        }
    }
    public void chooseDir() {
        if(l == null) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            try {
                path = directoryChooser.showDialog(new Stage()).getAbsolutePath();
            } catch (NullPointerException e) {
                return;
            }
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

    public void prevTrack() {

    }

    public void nextTrack() {
        String path = l.getNextTrack();

        if(path.equals("")) {
            System.out.println("Error");
            return;
        }
        mediaPlayer.stop();
        played = false;
        setTrack();
        playMusic();
    }

    public void menuOpen() {
        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);
        boolean open = true;
        if (drawer.getTranslateY() == 0) {
            openNav.setToY(141);
            openNav.play();
        } else {
            chooseDirBtn.setVisible(false);
            closeNav.setToY(0);
            open = false;
            closeNav.play();
        }
        if(open) {
            chooseDirBtn.setVisible(true);
        }
    }

    public void changeVolume(MouseEvent mouseEvent) {
        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100);
            }
        });
    }

    public void openValueSlider(ActionEvent actionEvent) {
        if(volumeSlider.isVisible()) {
            volumeSlider.setVisible(false);
        } else {
            volumeSlider.setVisible(true);
        }
    }
}
