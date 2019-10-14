package author.paulo.service;

import author.paulo.domain.Mixtape;
import author.paulo.domain.Playlist;
import author.paulo.service.ChangesFileProcessor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class ChangesFileProcessorTest {

    private ChangesFileProcessor changesFileProcessor;

    @Before
    public void setUp() throws IOException {
        InputStream mixtapeJson = getResourceAsStream("mixtape-data.json");
        InputStream changesJson = getResourceAsStream("changes.json");
        changesFileProcessor = new ChangesFileProcessor(mixtapeJson, changesJson);

    }

    @Test
    public void processAddSongToPlaylistCmd() {
        Playlist playlist = changesFileProcessor.getMixtape().findByPlaylistId("3");
        assertThat(playlist.getSongIds()).doesNotContain("1", "10");

        Mixtape mixtape = changesFileProcessor.processAddSongToPlaylistCmd();

        Playlist playlistAfterChanges = mixtape.findByPlaylistId("3");
        assertThat(playlistAfterChanges.getSongIds()).contains("1", "10");
    }

    @Test
    public void processAddNewPlaylistToExistingUserCmd() {
        List<Playlist> playlists = changesFileProcessor.getMixtape().findByUserId("2");
        assertThat(playlists.size()).isEqualTo(1);

        Mixtape mixtape = changesFileProcessor.processAddNewPlaylistToExistingUserCmd();
        List<Playlist> playlistAfterChanges = mixtape.findByUserId("2");

        List<Playlist> modifiedPlaylists = changesFileProcessor.getMixtape().findByUserId("2");
        assertThat(modifiedPlaylists.size()).isEqualTo(2);
        assertThat(playlistAfterChanges.get(1).getSongIds()).contains("1", "2");
    }

    @Test
    public void processRemoveExistingPlaylistCmd() {
        List<Playlist> playlists = changesFileProcessor.getMixtape().getPlaylists();
        assertThat(playlists.size()).isEqualTo(3);

        Mixtape modifiedMixtape = changesFileProcessor.processRemoveExistingPlaylistCmd();
        assertThat(modifiedMixtape.getPlaylists().size()).isEqualTo(1);
    }

    private InputStream getResourceAsStream(final String filename) {
        return getClass().getClassLoader().getResourceAsStream(filename);
    }
}