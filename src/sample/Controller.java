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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.Scanner;


public class Controller {

    public AnchorPane mainPane;
    public Button darkModeBtn;
    public Label pathToRemember;
    public Button minimazeBtn;
    public AnchorPane controlPane;
    public Button maximazeButton;
    public Button volumeBtn;
    private String pathToTrack;
    private boolean played = false;
    private ListOfMusic l;
    private boolean isDark = false;
    private boolean isMinimazed = false;

    public Button menuBtn;
    public Button chooseDirBtn;
    public ListView<String> listOfMusic;
    public Slider volumeSlider;
    public Slider trackDurationSlider;
    private MediaPlayer mediaPlayer;
    @FXML
    private Label currentTrackLabel;

    @FXML private Button play;
    @FXML private VBox drawer;

    @FXML void initialize() throws FileNotFoundException {

        //Open earlier opened folder
        if(pathToRemember.getText().contains("Empty")) {

            File fileWithMusic = new File("pathToFolder.txt");
            Scanner myReader = new Scanner(fileWithMusic);
            while (myReader.hasNextLine()) {
                pathToTrack = myReader.nextLine();
            }
            pathToRemember.setText(pathToTrack);
            addTracksFromPath();

            if(l.getSize() == 0) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("INFORMATION");
                a.setContentText("Not contains mp3 files. Try another folder");
                a.show();
                return;
            }
            setTrack();
        } else {
            pathToTrack = pathToRemember.getText();
            addTracksFromPath();
            setTrack();
        }
        mediaPlayer.setVolume(60.0);
        volumeSlider.setValue(60.0);
    }

    private String makeTextForLabel(String path) {
        String result = path.substring(path.lastIndexOf("\\") + 1);
        result = result.replaceAll("[-_]", " ");
        result = result.replaceAll(".mp3", "");
        return result.toUpperCase();
    }

    public void playMusic() {

        //Check correct path to fold with music
        if(pathToTrack == null || pathToTrack.equals("")) {
            return;
        }
        currentTrackLabel.setText(makeTextForLabel(pathToTrack));

        //Button style
        String styleOfButton = "-fx-background-image:url('images/light-mode/";
        if (!played) {
            styleOfButton += "pause.png');";
            mediaPlayer.play();
        } else {
            styleOfButton += "playImg.png');";
            mediaPlayer.pause();
        }

        play.setStyle(styleOfButton);

        played = !played;

        moveMusicSlider();
    }

    private void moveMusicSlider() {
        mediaPlayer.currentTimeProperty().addListener((observableValue, duration, t1) -> {
            if(trackDurationSlider.getValue() >= trackDurationSlider.getMax()) {
                nextTrack();
            }
            trackDurationSlider.setValue(t1.toSeconds());
        });
    }

    private void setTrack() {

        if(l == null) {
            System.out.println("Error");
            return;
        }

        pathToTrack = l.getCurrentTrack();

        Media media = new Media(new File(pathToTrack).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(false);

        trackDurationSlider.setMin(0);



        mediaPlayer.setOnReady(() ->
                trackDurationSlider.setMax(media.getDuration().toSeconds()-1));

    }

    private void addTracksFromPath() {

        File repos = new File(pathToTrack);
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

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("D:\\"));
        try {
            pathToTrack = directoryChooser.showDialog(new Stage()).getAbsolutePath();
            if (!pathToRemember.getText().contains(pathToTrack)) {
                pathToRemember.setText(pathToTrack);
                FileWriter fw = new FileWriter(new File("pathToFolder.txt"));
                PrintWriter pw = new PrintWriter(fw);
                pw.print(pathToTrack);
                pw.close();
            }
        } catch (NullPointerException | IOException e) {
            return;
        }
        addTracksFromPath();
        menuOpen();
        setTrack();
    }

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
                minimazeBtn.setVisible(true);

            });
        } else {
            chooseDirBtn.setVisible(false);
            darkModeBtn.setVisible(false);
            pathToRemember.setVisible(false);
            minimazeBtn.setVisible(false);
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
            Text on = new Text("Dark mode: On");
            on.setFill(Color.GREEN);
            darkModeBtn.setText(on.getText());
        } else {
            Text off = new Text("Dark mode: Off");
            off.setFill(Color.RED);
            darkModeBtn.setText(off.getText());
        }
    }

    public void minimazeWindow() {
        menuOpen();
        if(isMinimazed) {
            //Remember old size of stage and set this size
            //where stage is maximazed
            Main.setOldSizeOfStage();

            maximazeButton.setVisible(false);
            controlPane.relocate(0, 295);
            controlPane.setMaxWidth(600);

            drawer.setVisible(true);
            menuBtn.setVisible(true);
            trackDurationSlider.setMaxWidth(579);

            volumeBtn.setTranslateX(0);
            volumeSlider.setTranslateX(0);

            isMinimazed = false;

        } else {
            Main.setNewSizeOfStage(450, controlPane.getHeight()+25);
            controlPane.setMaxWidth(450);
            trackDurationSlider.setMaxWidth(400);
            listOfMusic.setVisible(false);
            volumeBtn.setTranslateX(-150);
            volumeSlider.setTranslateX(-150);
            controlPane.relocate(0,0);
            drawer.setVisible(false);
            menuBtn.setVisible(false);
            maximazeButton.setVisible(true);
            isMinimazed = true;
        }
        Main.setOnTop();

    }
}
