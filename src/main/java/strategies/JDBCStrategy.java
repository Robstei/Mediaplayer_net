package strategies;

import interfaces.SerializableStrategy;


import java.io.IOException;
import java.sql.*;
import modelview.Song;

public class JDBCStrategy implements SerializableStrategy {


    Connection c;
    PreparedStatement stmt;
    String SQLString;
    ResultSet rs;
    private int songCount;
    private int playlistCount;

    public JDBCStrategy() {
        try {
            c = DriverManager.getConnection("jdbc:sqlite:SongsLibary.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openWriteableSongs() throws IOException {
        try {
            SQLString = "create table if not exists Songs (Path String, Titel String, Album String, Interpret String, id int)";
            stmt = c.prepareStatement(SQLString);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQLString = "INSERT INTO SONGS (Path, Titel, Album, Interpret, id) Values (?,?,?,?,?);";
    }

    @Override
    public void openReadableSongs() throws IOException {
        try {
            SQLString = "select * from Songs";
            stmt = c.prepareStatement(SQLString);
            rs = stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openWriteablePlaylist() throws IOException {
        try {
            SQLString = "create table if not exists Playlist (Path String, Titel String, Album String, Interpret String, id int)";
            stmt = c.prepareStatement(SQLString);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SQLString = "INSERT INTO PLAYLIST (Path, Titel, Album, Interpret, id) Values (?,?,?,?,?);";
    }

    @Override
    public void openReadablePlaylist() throws IOException {
        try {
            SQLString = "select * from Playlist";
            stmt = c.prepareStatement(SQLString);
            rs = stmt.executeQuery();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeSong(interfaces.Song s) throws IOException {
        try {
            stmt = c.prepareStatement(SQLString);
            stmt.setString(1, s.getPath());
            stmt.setString(2, s.getTitle());
            stmt.setString(3, s.getAlbum());
            stmt.setString(4, s.getInterpret());
            stmt.setInt(5, (int) s.getId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Song readSong() throws IOException, ClassNotFoundException {
        Song s = new Song();
        try {
            if (rs.next()) {
                s.setPath(rs.getString(1));
                s.setTitle(rs.getString(2));
                s.setAlbum(rs.getString(3));
                s.setInterpret(rs.getString(4));
                return s;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int songsCount() {
        SQLString = "select count(*) from Songs";
        try {
            stmt = c.prepareStatement(SQLString);
            rs = stmt.executeQuery();
            songCount = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songCount;
    }

    public int playlistCount() {
        SQLString = "select count(*) from Playlist";
        try {
            stmt = c.prepareStatement(SQLString);
            rs = stmt.executeQuery();
            playlistCount = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playlistCount;
    }

    @Override
    public void closeReadable() {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeWriteable() {
        try {
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
