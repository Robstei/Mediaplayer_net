import javafx.beans.property.SimpleStringProperty;

public class Song implements interfaces.Song {

    SimpleStringProperty path = new SimpleStringProperty();
    SimpleStringProperty titel = new SimpleStringProperty();
    SimpleStringProperty album = new SimpleStringProperty();
    SimpleStringProperty interpret = new SimpleStringProperty();
    long id;

    public Song(String p,String t,String a,String i){
        this.setPath(p);
        this.setTitle(t);
        this.setAlbum(a);
        this.setInterpret(i);
    }
    @Override
    public String getAlbum() {
        return  album.get();
    }

    @Override
    public void setAlbum(String album) {
        this.album.set(album);
    }

    @Override
    public String getInterpret() {
        return interpret.get();
    }

    @Override
    public void setInterpret(String interpret) {
        this.interpret.set(interpret);
    }

    @Override
    public String getPath() {
        return path.get();
    }

    @Override
    public void setPath(String path) {
        this.path.set(path);
    }

    @Override
    public String getTitle() {
        return titel.get();
    }

    @Override
    public void setTitle(String title) {
        this.titel.set(title);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id=id;
    }

    @Override
    public SimpleStringProperty pathProperty() {
        return path;
    }

    @Override
    public SimpleStringProperty albumProperty() {
        return album;
    }

    @Override
    public SimpleStringProperty interpretProperty() {
        return interpret;
    }
}
