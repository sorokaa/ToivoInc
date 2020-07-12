package sample;

import MusicList.ListOfMusic;
import StyleOfFrame.FrameStyle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.Scanner;

public class Controller {

    public AnchorPane mainPane;
    public Button darkModeBtn;
    public Label pathToRemember;
    private String path;
    private boolean played = false;
    private ListOfMusic l;
    private boolean isDark = false;

    public Button menuBtn;
    public Button chooseDirBtn;
    public Label currentTrackLabel;
    public ListView<String> listOfMusic;
    public Slider volumeSlider;
    public Slider trackDurationSlider;
    private MediaPlayer mediaPlayer;
    @FXML private Button play;
    @FXML private VBox drawer;

    @FXML void initialize() throws FileNotFoundException {
        if(pathToRemember.getText().contains("Empty")) {
            File myObj = new File("pathToFolder.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                path = myReader.nextLine();
            }
            pathToRemember.setText(path);
            addTracksFromPath();
            setTrack();

        } else {
            path = pathToRemember.getText();
            addTracksFromPath();
            setTrack();
        }
        volumeSlider.setValue(60.0);
    }

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
            play.setStyle("-fx-background-image:url('images/pause.png');");
            mediaPlayer.play();
        } else {
            play.setStyle("-fx-background-image:url('images/playImg.png');");
            mediaPlayer.pause();
        }
        played = !played;
        mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> {
            if(trackDurationSlider.getValue() >= trackDurationSlider.getMax()) {
                nextTrack();
            }
            trackDurationSlider.setValue(t1.toSeconds());
        });
    }

    private void setTrack() {

        path = l.getCurrentTrack();
        Media media = new Media(new File(path).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);
        trackDurationSlider.setMin(0);
        mediaPlayer.setOnReady(() ->
                trackDurationSlider.setMax(media.getDuration().toSeconds()-1));

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
            directoryChooser.setInitialDirectory(new File("D:\\"));
            try {
                path = directoryChooser.showDialog(new Stage()).getAbsolutePath();
                if(!pathToRemember.getText().contains(path)) {
                    pathToRemember.setText(path);
                    FileWriter fw = new FileWriter(new File("pathToFolder.txt"));
                    PrintWriter pw = new PrintWriter(fw);
                    pw.print(path);
                    pw.close();
                }
            } catch (NullPointerException | IOException e) {
                return;
            }
            addTracksFromPath();
            if(l.getSize() == 0) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("INFORMATION");
                a.setContentText("Not contains mp3 files. Try another folder");
                a.show();
                return;
            }
            menuOpen();
            setTrack();
        }
    }
    //TODO
    public void prevTrack() {
        String path;
        try {
            path = l.getPrevTrack();
        } catch (NullPointerException e) {
            return;
        }
        if(path.equals("")) {
            System.out.println("Error");
            return;
        }
        mediaPlayer.stop();
        played = false;
        setTrack();
        playMusic();
    }

    public void nextTrack() {
        String path;
        try {
            path = l.getNextTrack();
        } catch (NullPointerException e) {
            return;
        }
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
        Main.stage.setWidth(600);
        Main.stage.setHeight(136);
        TranslateTransition openNav = new TranslateTransition(new Duration(350), drawer);
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), drawer);

        if (drawer.getTranslateY() == 0) {
            openNav.setToY(141);
            openNav.play();
            openNav.setAutoReverse(true);
            openNav.setOnFinished(actionEvent -> {
                chooseDirBtn.setVisible(true);
                darkModeBtn.setVisible(true);
                pathToRemember.setVisible(true);
            });
        } else {
            chooseDirBtn.setVisible(false);
            darkModeBtn.setVisible(false);
            pathToRemember.setVisible(false);
            closeNav.setToY(0);
            closeNav.play();
        }
    }

    public void changeVolume() {
        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
        volumeSlider.valueProperty().addListener(
                observable -> mediaPlayer.setVolume(volumeSlider.getValue() / 100));
    }

    public void openValueSlider() {
        if(volumeSlider.isVisible()) {
            volumeSlider.setVisible(false);
        } else {
            volumeSlider.setVisible(true);
        }
    }

    public void darkModeEnable(ActionEvent actionEvent) {
        String text = ((Button)actionEvent.getSource()).getText();
        FrameStyle f = new FrameStyle(text);

        boolean isEmptyList = true;
        if (!listOfMusic.getItems().isEmpty()) {
            isEmptyList = false;
        }

        String[] settings = f.changeMode(isEmptyList);

        mainPane.setStyle(settings[0]);
        listOfMusic.setStyle(settings[1]);

        isDark = !isDark;

        if(isDark) {
            darkModeBtn.setText("Dark mode: On");
        } else {
            darkModeBtn.setText("Dark mode: Off");
        }
    }
}
