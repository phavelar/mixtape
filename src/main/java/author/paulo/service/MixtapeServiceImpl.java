package author.paulo.service;

import author.paulo.domain.Mixtape;
import author.paulo.domain.Playlist;
import author.paulo.domain.Song;
import author.paulo.domain.User;

import java.util.ArrayList;
import java.util.List;

import static author.paulo.domain.Mixtape.MixtapeBuilder.aMixtape;
import static author.paulo.domain.Playlist.PlaylistBuilder.aPlaylist;
import static java.lang.String.valueOf;
import static java.util.Collections.unmodifiableList;

public class MixtapeServiceImpl implements MixtapeService {

    @Override
    public Mixtape addSongToPlaylist(final String songId, final String playlistId, Mixtape mixtape) {
        if (songId == null || playlistId == null) {
            throw new IllegalArgumentException("null argument(s) given!");
        }
        Song song = findSongById(songId, mixtape);
        Playlist playlist = findPlaylistById(playlistId, mixtape);
        if (song != null && playlist != null) {
            playlist.getSongIds().add(songId);
        } else if (song == null) {
            throw new IllegalArgumentException("songId not found!");
        } else if (playlist == null) {
            throw new IllegalArgumentException("playlistId not found!");
        }
        return aMixtape()
                .withPlaylists(mixtape.getPlaylists())
                .withSongs(mixtape.getSongs())
                .withUsers(mixtape.getUsers())
                .build();
    }

    @Override
    public Mixtape addPlaylistToUser(final Playlist playlist, final String userId, Mixtape mixtape) {
        if (playlist == null || userId == null) {
            throw new IllegalArgumentException("null argument(s) given!");
        }
        if (!userExists(userId, mixtape)) {
            throw new IllegalArgumentException("User identified by id " + userId + " does not exist.");
        }
        //operate on a copy of list to ensure immutability
        List<Playlist> copyPlaylists = new ArrayList<>(mixtape.getPlaylists());

        Playlist newPlaylist = aPlaylist()
                .withSongIds(playlist.getSongIds())
                .withUserId(playlist.getUserId())
                .withId(valueOf(copyPlaylists.size()))
                .build();

        copyPlaylists.add(newPlaylist);

        return aMixtape()
                .withPlaylists(copyPlaylists)
                .withSongs(mixtape.getSongs())
                .withUsers(mixtape.getUsers())
                .build();
    }

    @Override
    public Mixtape removePlaylist(final List<String> playlistIds, Mixtape mixtape) {

        List<Playlist> copyPlayList = new ArrayList<>(mixtape.getPlaylists());

        for (String playlistId : playlistIds) {
            Playlist playlist = findPlaylistById(playlistId, mixtape);
            if (playlist != null) {
                copyPlayList.remove(playlist);
            }
        }
        return aMixtape()
                .withPlaylists(copyPlayList)
                .withSongs(mixtape.getSongs())
                .withUsers(mixtape.getUsers())
                .build();
    }

    private boolean userExists(final String userId, final Mixtape mixtape) {
        List<User> users = unmodifiableList(mixtape.getUsers());
        for (User user : users) {
            if (user.getId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    private Playlist findPlaylistById(final String playlistId, final Mixtape mixtape) {
        List<Playlist> playlists = unmodifiableList(mixtape.getPlaylists());
        for (Playlist playlist : playlists) {
            if (playlist.getId().equals(playlistId)) {
                return playlist;
            }
        }
        return null;
    }

    private Song findSongById(final String songId, final Mixtape mixtape) {
        List<Song> songs = unmodifiableList(mixtape.getSongs());
        for (Song song : songs) {
            if (song.getId().equals(songId)) {
                return song;
            }
        }
        return null;
    }


}
