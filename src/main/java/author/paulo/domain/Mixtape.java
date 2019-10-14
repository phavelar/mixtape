
package author.paulo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "users",
    "playlists",
    "songs"
})
public class Mixtape {

    @JsonProperty("users")
    private List<User> users = null;
    @JsonProperty("playlists")
    private List<Playlist> playlists = null;
    @JsonProperty("songs")
    private List<Song> songs = null;
    @JsonProperty("users")
    public List<User> getUsers() {
        return users;
    }

    @JsonProperty("playlists")
    public List<Playlist> getPlaylists() {
        return playlists;
    }

    @JsonProperty("songs")
    public List<Song> getSongs() {
        return songs;
    }


    public Playlist findByPlaylistId(final String playlistId) {
        for (Playlist playlist : playlists) {
            if(playlist.getId().equals(playlistId)) {
                return playlist;
            }
        }
        return null;
    }

    public List<Playlist> findByUserId(final String userId) {
        List<Playlist> result = new ArrayList<>();
        for (Playlist playlist : playlists) {
            if(playlist.getUserId().equals(userId)) {
                result.add(playlist);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mixtape mixtape = (Mixtape) o;
        return Objects.equals(users, mixtape.users) &&
                Objects.equals(playlists, mixtape.playlists) &&
                Objects.equals(songs, mixtape.songs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(users, playlists, songs);
    }

    private Mixtape() {
    }

    public static final class MixtapeBuilder {
        private List<User> users = null;
        private List<Playlist> playlists = null;
        private List<Song> songs = null;

        private MixtapeBuilder() {
        }

        public static MixtapeBuilder aMixtape() {
            return new MixtapeBuilder();
        }

        public MixtapeBuilder withUsers(List<User> users) {
            this.users = users;
            return this;
        }

        public MixtapeBuilder withPlaylists(List<Playlist> playlists) {
            this.playlists = playlists;
            return this;
        }

        public MixtapeBuilder withSongs(List<Song> songs) {
            this.songs = songs;
            return this;
        }

        public Mixtape build() {
            Mixtape mixtape = new Mixtape();
            mixtape.songs = this.songs;
            mixtape.users = this.users;
            mixtape.playlists = this.playlists;
            return mixtape;
        }
    }
}
