package StyleOfFrame;

public class FrameStyle {

    private String currentStatus;

    public FrameStyle(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String[] changeMode(boolean isBlankList) {

        String[] settings = new String[2];

        if(currentStatus.contains("Off")) {
            settings[0] = "-fx-background-color: black;";
            if (isBlankList) {
                settings[1] = "-fx-background-color: black; -fx-control-inner-background: black";
            } else {
                settings[1] = "-fx-background-color: #FFF7EB; -fx-control-inner-background: #333";
            }
        } else {
            settings[0] = "-fx-background-color: white";
            settings[1] = "-fx-background-color: white";
        }

        return settings;
    }

}
