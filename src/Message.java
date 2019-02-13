public class Message {
    private boolean parent;
    private int inUID;
    private boolean search;

    public Message(boolean parent, int inUID, boolean search) {
        this.parent = parent;
        this.inUID = inUID;
        this.search = search;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public int getInUID() {
        return inUID;
    }

    public void setInUID(int inUID) {
        this.inUID = inUID;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }
}
