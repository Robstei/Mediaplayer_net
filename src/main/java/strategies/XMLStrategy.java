package strategies;

import modelview.Song;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class XMLStrategy implements interfaces.SerializableStrategy {

    FileInputStream fis;
    FileOutputStream fos;
    XMLEncoder enc;
    XMLDecoder dec;
    private int count;


/*   public strategies.XMLStrategy() {
        BeanInfo info = null;
        try {
            info = Introspector.getBeanInfo(modelview.Song.class);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        PropertyDescriptor[] propertyDescriptors =
                info.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; ++i) {
            PropertyDescriptor pd = propertyDescriptors[i];
            if (pd.getName().equals("path") ||
                    pd.getName().equals("titel") ||
                    pd.getName().equals("album") ||
                    pd.getName().equals("interpret")) {
                pd.setValue("transient", Boolean.TRUE);
            }
        }
    }*/

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void openWriteableSongs() throws IOException {
        fos = new FileOutputStream("SongsLibary.xml");
        enc = new XMLEncoder(fos);
    }

    @Override
    public void openReadableSongs() throws IOException {
        fis = new FileInputStream("SongsLibary.xml");
        dec = new XMLDecoder(fis);
    }

    @Override
    public void openWriteablePlaylist() throws IOException {
        fos = new FileOutputStream("PlaylistLibary.xml");
        enc = new XMLEncoder(fos);
    }

    @Override
    public void openReadablePlaylist() throws IOException {
        fis = new FileInputStream("PlaylistLibary.xml");
        dec = new XMLDecoder(fis);
    }

    @Override
    public void writeSong(interfaces.Song s) throws IOException {
        enc.writeObject(s);
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        return (Song) dec.readObject();
    }

    public void writeCount(int count) throws IOException {
        enc.writeObject(count);
    }

    public int readCount() {
        return (int) dec.readObject();
    }

    @Override
    public void closeReadable() {
        dec.close();
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeWriteable() {
        enc.close();
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
