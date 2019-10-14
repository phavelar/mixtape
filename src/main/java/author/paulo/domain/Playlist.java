
package author.paulo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "user_id",
        "song_ids"
})
public class Playlist  {

    @JsonProperty("id")
    private String id;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("song_ids")
    private List<String> songIds = null;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("song_ids")
    public List<String> getSongIds() {
        return songIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Playlist playlist = (Playlist) o;
        return Objects.equals(id, playlist.id) &&
                Objects.equals(userId, playlist.userId) &&
                Objects.equals(songIds, playlist.songIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, songIds);
    }

    private Playlist() {
    }

    public static final class PlaylistBuilder {
        private String id;
        private String userId;
        private List<String> songIds = null;

        private PlaylistBuilder() {
        }

        public static PlaylistBuilder aPlaylist() {
            return new PlaylistBuilder();
        }

        public PlaylistBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public PlaylistBuilder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public PlaylistBuilder withSongIds(List<String> songIds) {
            this.songIds = songIds;
            return this;
        }

        public Playlist build() {
            Playlist playlist = new Playlist();
            playlist.songIds = this.songIds;
            playlist.userId = this.userId;
            playlist.id = this.id;
            return playlist;
        }
    }
}
