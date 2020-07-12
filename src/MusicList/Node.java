package MusicList;

public class Node {
    private String currentTrack;
    private Node prev;
    private Node next;

    Node(Node prev, Node next, String currentTrack) {
        this.prev = prev;
        this.next = next;
        this.currentTrack = currentTrack;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    void setNext(Node next) {
        this.next = next;
    }

    String getCurrentTrack() {
        return currentTrack;
    }

    public Node getNext() {
        return next;
    }

    public boolean hasPrev() {
        return prev != null;
    }

    public Node getPrev() {
        return prev;
    }
}
