package strategies;

import interfaces.SerializableStrategy;
import modelview.*;
import modelview.Song;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenJPAStrategy implements SerializableStrategy {

    private EntityManagerFactory fac;
    private EntityManager e;
    private EntityTransaction t;
    private List<Song> listSongs;
    private List<SongPlaylist> listPlaylist;
    private Query q;
    private Song s;
    private boolean switcher; //true == Songs  false == Playlist
    private int counter;
    private Map prop;

    @Override
    public void openWriteableSongs() throws IOException {
        prop = new HashMap();
        prop.put("openjpa.ConnectionURL", "jdbc:sqlite:C:/Users/Robin/InteliJProjects/Mediaplayer_ser/SongsLibary.db");
        prop.put("openjpa.ConnectionDriverName", "org.sqlite.JDBC");
        prop.put("openjpa.ConnectionUserName", "robin");
        prop.put("openjpa.ConnectionPassword", "steil");
        prop.put("openjpa.jdbc.SynchronizeMappings", "false");
        prop.put("openjpa.RuntimeUnenhancedClasses", "supported");
        List<Class<?>> types = new ArrayList<Class<?>>();
        types.add(Song.class);
        if (!types.isEmpty()) {
            StringBuffer buf = new StringBuffer();
            for (Class<?> c : types) {
                if (buf.length() > 0)
                    buf.append(";");
                buf.append(c.getName());
            }
            prop.put("openjpa.MetaDataFactory", "jpa(Types=" + buf.toString()
                    + ")");
        }
        fac = OpenJPAPersistence.getEntityManagerFactory(prop);
        e = fac.createEntityManager();
        t = e.getTransaction();
    }

    @Override
    public void openReadableSongs() throws IOException {
        fac = Persistence.createEntityManagerFactory(
                "openjpa", System.getProperties());
        e = fac.createEntityManager();
        q = e.createQuery("select x from modelview.Song x");
        listSongs = (List<Song>) q.getResultList();
        counter = 0;
        switcher = true;
    }

    @Override
    public void openWriteablePlaylist() throws IOException {
        fac = Persistence.createEntityManagerFactory(
                "openjpa", System.getProperties());
        e = fac.createEntityManager();
        t = e.getTransaction();
    }

    @Override
    public void openReadablePlaylist() throws IOException {
        fac = Persistence.createEntityManagerFactory(
                "openjpa", System.getProperties());
        e = fac.createEntityManager();
        q = e.createQuery("select x from modelview.SongPlaylist x");
        listPlaylist = (List<SongPlaylist>) q.getResultList();
        counter = 0;
        switcher = false;
    }

    @Override
    public void writeSong(interfaces.Song s) throws IOException {
        t.begin();
        e.persist(s);
        t.commit();
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        if (listSongs.size() > 0 && counter < listSongs.size() && switcher) {
            s = listSongs.get(counter);
            counter++;
            return s;
        }
      /*  if(listPlaylist.size() > 0 && counter < listPlaylist.size() && switcher==false) {
            s = (modelview.Song) listPlaylist.get(counter);
            counter++;
            return s;
        }*/
        return null;
    }

    @Override
    public void closeReadable() {
        e.close();
        fac.close();
    }

    @Override
    public void closeWriteable() {
        e.close();
        fac.close();
    }
}
