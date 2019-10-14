package author.paulo.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import static author.paulo.domain.Mixtape.MixtapeBuilder.aMixtape;
import static author.paulo.domain.Playlist.PlaylistBuilder.aPlaylist;
import static author.paulo.domain.Song.SongBuilder.aSong;
import static author.paulo.domain.User.UserBuilder.anUser;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * This test may seem debatable as we are testing JACKSON library, however it tests that the given
 * JSON document structure mapped as POJO object model.
 */
public class MixtapeTest {

    private ObjectMapper mapper;
    private User user;
    private Song song;
    private Playlist playList;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();

        user = anUser()
                .withId("1")
                .withName("userName")
                .build();
        song = aSong()
                .withId("1")
                .withTitle("songTitle")
                .withArtist("artist")
                .build();
        playList = aPlaylist()
                .withId("1")
                .withSongIds(singletonList("1"))
                .withUserId("1")
                .build();
    }

    @Test
    public void serializeMixtape() throws JsonProcessingException {
        Mixtape mixtape = aMixtape()
                .withUsers(singletonList(user))
                .withSongs(singletonList(song))
                .withPlaylists(singletonList(playList))
                .build();
        assertThat(mapper.writeValueAsString(mixtape))
                .isEqualTo("{\"users\":[{\"id\":\"1\",\"name\":\"userName\"}],\"" +
                        "playlists\":[{\"id\":\"1\",\"user_id\":\"1\",\"song_ids\":[\"1\"]}],\"" +
                        "songs\":[{\"id\":\"1\",\"artist\":\"artist\",\"title\":\"songTitle\"}]}");
    }

    @Test
    public void deserializeMixtape() throws JsonProcessingException {

        Mixtape mixtape = mapper.readValue("{\"users\":[{\"id\":\"1\",\"name\":\"userName\"}],\"" +
                "playlists\":[{\"id\":\"1\",\"user_id\":\"1\",\"song_ids\":[\"1\"]}],\"" +
                "songs\":[{\"id\":\"1\",\"artist\":\"artist\",\"title\":\"songTitle\"}]}", Mixtape.class);

        assertThat(mixtape.getPlaylists()).containsExactly(playList);
        assertThat(mixtape.getSongs()).containsExactly(song);
        assertThat(mixtape.getUsers()).containsExactly(user);
    }


}