
package author.paulo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "artist",
        "title"
})
public class Song {

    @JsonProperty("id")
    private String id;
    @JsonProperty("artist")
    private String artist;
    @JsonProperty("title")
    private String title;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("artist")
    public String getArtist() {
        return artist;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(title, song.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artist, title);
    }

    private Song() {
    }

    public static final class SongBuilder {
        private String id;
        private String artist;
        private String title;

        private SongBuilder() {
        }

        public static SongBuilder aSong() {
            return new SongBuilder();
        }

        public SongBuilder withId(String id) {
            this.id = id;
            return this;
        }

        public SongBuilder withArtist(String artist) {
            this.artist = artist;
            return this;
        }

        public SongBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Song build() {
            Song song = new Song();
            song.artist = this.artist;
            song.title = this.title;
            song.id = this.id;
            return song;
        }
    }
}
