package author.paulo.service;

import author.paulo.domain.Mixtape;
import author.paulo.domain.Playlist;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static author.paulo.domain.Playlist.PlaylistBuilder.aPlaylist;

public class ChangesFileProcessor {

    private MixtapeService mixtapeService;
    private Mixtape mixtape;
    private final JsonNode rootNode;

    public ChangesFileProcessor(final InputStream mixtapeJson, final InputStream changesJson) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.mixtape = objectMapper.readValue(mixtapeJson, Mixtape.class);
        this.mixtapeService = new MixtapeServiceImpl();
        this.rootNode = objectMapper.readTree(changesJson);
    }

    public Mixtape getMixtape() {
        return mixtape;
    }

    public Mixtape processAllChanges() {
        mixtape = processAddSongToPlaylistCmd();
        mixtape = processAddNewPlaylistToExistingUserCmd();
        mixtape = processRemoveExistingPlaylistCmd();
        return  mixtape;
    }


    public Mixtape processAddSongToPlaylistCmd() {
        JsonNode addSongToPlaylistPath = rootNode.findPath("addSongToPlaylist");
        addSongToPlaylistPath.elements().forEachRemaining(
                it -> mixtape = mixtapeService.addSongToPlaylist(it.get("songId").textValue(), it.get("playlistId").textValue(), mixtape)
        );
        return mixtape;
    }

    public Mixtape processAddNewPlaylistToExistingUserCmd() {
        JsonNode addNewPlaylistToExistingUserPath = rootNode.findPath("addNewPlaylistToExistingUser");
        addNewPlaylistToExistingUserPath.elements().forEachRemaining(
                it -> {
                    String userId = it.get("user_id").textValue();
                    List<String> songIds = new ArrayList<>();
                    it.get("song_ids").elements().forEachRemaining(id -> songIds.add(id.textValue()));
                    Playlist playlist = aPlaylist()
                            .withUserId(userId)
                            .withSongIds(songIds)
                            .build();
                    mixtape = mixtapeService.addPlaylistToUser(playlist, userId, mixtape);
                }
        );
        return mixtape;
    }

    public Mixtape processRemoveExistingPlaylistCmd() {
        JsonNode removeExistingPlaylistPath = rootNode.findPath("removeExistingPlaylist");
        List<String> playlistIds = new ArrayList<>();
        removeExistingPlaylistPath.elements().forEachRemaining(it -> it.elements().forEachRemaining(id -> playlistIds.add(id.textValue())));
        mixtape = mixtapeService.removePlaylist(playlistIds, mixtape);
        return mixtape;
    }
}
