package MusicList;

public class ListOfMusic {

    private String currentTrack;
    private Node first;
    private Node last;
    private Node n;
    private int size;

    public ListOfMusic() {
        first = null;
        last = null;
    }

    public void add(String path) {
        if (this.first == null) {
            first = new Node(null, null, path);
        } else {
            Node prevElement = this.last == null ? this.first : this.last;
            this.last = new Node(prevElement, null, path);
            prevElement.setNext(this.last);
        }
        size++;
    }

    public int getSize() {
        return size;
    }

    public String getNextTrack() {
        if(first.getPrev() == null && n == null) {
            n = first;
        }
        String path = n.getCurrentTrack();
        n = n.getNext();
        return path;
    }
}
