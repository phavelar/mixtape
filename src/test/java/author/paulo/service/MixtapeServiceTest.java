package author.paulo.service;

import author.paulo.domain.Mixtape;
import author.paulo.domain.Playlist;
import author.paulo.domain.Song;
import author.paulo.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import static author.paulo.domain.Playlist.PlaylistBuilder.aPlaylist;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

public class MixtapeServiceTest {

    private MixtapeService mixtapeService;
    private Mixtape mixtape;

    @Before
    public void setUp() throws IOException {
        mixtapeService = new MixtapeServiceImpl();
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream json = getClass().getClassLoader().getResourceAsStream("mixtape-data.json");
        mixtape = objectMapper.readValue(json, Mixtape.class);
    }

    @Test
    public void addExistingSongToExistingPlaylist_works() {
        Mixtape modifiedMixtape = mixtapeService.addSongToPlaylist("1", "3", mixtape);
        assertThat(findPlaylistById(modifiedMixtape.getPlaylists(), "3")).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNonExistingSongToExistingPlaylist_throwsIllegalArgumentException() {
        mixtapeService.addSongToPlaylist("99", "3", mixtape);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addExistingSongToNonExistingPlaylist_throwsIllegalArgumentException() {
        mixtapeService.addSongToPlaylist("1", "99", mixtape);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addSongToPlaylistWithNullArguments_throwsIllegalArgumentException() {
        mixtapeService.addSongToPlaylist(null, "2", mixtape);
    }

    @Test
    public void addPlaylistToUser_works() {
        List<Song> songs = mixtape.getSongs();
        User user = mixtape.getUsers().get(0);
        Song song = songs.get(0);

        Playlist playlist = aPlaylist()
                .withUserId(user.getId())
                .withSongIds(singletonList(song.getId()))
                .build();

        int numberOfPlaylists = mixtape.getPlaylists().size();
        Mixtape modifiedMixtape = mixtapeService.addPlaylistToUser(playlist, user.getId(), mixtape);
        assertThat(modifiedMixtape.getPlaylists().size()).isEqualTo(numberOfPlaylists + 1);
    }

    @Test
    public void removePlaylist_works() {
        Mixtape modifiedMixtape = mixtapeService.removePlaylist(singletonList("3"), mixtape);
        List<Playlist> playlists = modifiedMixtape.getPlaylists();
        assertThat(playlists).isNotEmpty();

        for (Playlist playlist : playlists) {
            assertThat(playlist.getId()).isNotEqualTo("3");
        }
    }

    @Test
    public void removePlaylistWithNonExistingId_doesNothing() {
        Mixtape modifiedMixtape = mixtapeService.removePlaylist(singletonList("99"), mixtape);

        assertThat(modifiedMixtape).isEqualTo(mixtape);
    }

     private Playlist findPlaylistById(final List<Playlist> playlists, final String playlistId) {
        Playlist foundPlaylist = null;
        for (Playlist playlist : playlists) {
            if (playlist.getId().equals(playlistId)) {
                foundPlaylist = playlist;
            }
        }
        return foundPlaylist;
    }
}