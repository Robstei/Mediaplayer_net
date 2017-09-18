import interfaces.SerializableStrategy;

import java.io.*;

public class BinaryStrategy implements SerializableStrategy {

    private FileInputStream fis;
    private FileOutputStream fos;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


    @Override
    public void openWriteableSongs() throws IOException {
        fos = new FileOutputStream("SongsLibary");
        oos = new ObjectOutputStream(fos);
    }

    @Override
    public void openReadableSongs() throws IOException {
        fis = new FileInputStream("SongsLibary");
        ois = new ObjectInputStream(fis);
    }

    @Override
    public void openWriteablePlaylist() throws IOException {
        fos = new FileOutputStream("PlaylistLibary");
        oos = new ObjectOutputStream(fos);
    }

    @Override
    public void openReadablePlaylist() throws IOException {
        fis = new FileInputStream("PlaylistLibary");
        ois = new ObjectInputStream(fis);
    }

    @Override
    public void writeSong(interfaces.Song s) throws IOException {
        oos.writeObject(s);
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        return (Song) ois.readObject();
    }


    @Override
    public void closeReadable() {
        try {
            ois.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeWriteable() {
        try {
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
