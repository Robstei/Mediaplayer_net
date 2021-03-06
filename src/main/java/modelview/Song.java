package modelview;

import idgenerator.*;
import javafx.beans.property.SimpleStringProperty;
import org.apache.openjpa.persistence.Persistent;
import org.apache.openjpa.persistence.jdbc.Strategy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Entity
@Table(name = "Songs")
public class Song extends UnicastRemoteObject implements interfaces.Song, Serializable {

    private static final long serialVersionUID = 716389091202934571L;

    @Persistent
    @Strategy("strategies.StringPropertyValueHandler")
    transient private SimpleStringProperty path = new SimpleStringProperty();

    @Persistent
    @Strategy("strategies.StringPropertyValueHandler")
    transient private SimpleStringProperty titel = new SimpleStringProperty();

    @Persistent
    @Strategy("strategies.StringPropertyValueHandler")
    transient private SimpleStringProperty album = new SimpleStringProperty();

    @Persistent
    @Strategy("strategies.StringPropertyValueHandler")
    transient private SimpleStringProperty interpret = new SimpleStringProperty();
    @Id
    private long id;

    public Song() throws RemoteException {
        super();
    }

    public Song(String p, String t, String a, String i) throws RemoteException {
        super();
        path = new SimpleStringProperty();
        titel = new SimpleStringProperty();
        album = new SimpleStringProperty();
        interpret = new SimpleStringProperty();
        this.setPath(p);
        this.setTitle(t);
        this.setAlbum(a);
        this.setInterpret(i);
        setId(IDGenerator.getNextID());
    }

    @Override
    public String getAlbum() {
        return album.get();
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
        this.id = id;
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

    @Override
    public String toString()  {
        return "Titel: " + getTitle() + "\nInterpret: " + getInterpret() + "\nAlbum: " + getAlbum() + "\nPath: " + getPath();
    }

    private void writeObject(ObjectOutputStream o) throws IOException {
        o.defaultWriteObject();
        o.writeObject(getPath());
        o.writeObject(getTitle());
        o.writeObject(getAlbum());
        o.writeObject(getInterpret());
    }

    private void readObject(ObjectInputStream o) throws IOException, ClassNotFoundException {
        o.defaultReadObject();
        path = new SimpleStringProperty();
        setPath((String) o.readObject());
        titel = new SimpleStringProperty();
        setTitle((String) o.readObject());
        album = new SimpleStringProperty();
        setAlbum((String) o.readObject());
        interpret = new SimpleStringProperty();
        setInterpret((String) o.readObject());
    }
}
