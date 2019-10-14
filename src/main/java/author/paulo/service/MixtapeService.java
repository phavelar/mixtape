package author.paulo.service;

import author.paulo.domain.Mixtape;
import author.paulo.domain.Playlist;

import java.util.List;

public interface MixtapeService {
    Mixtape addSongToPlaylist(final String songId, final String playlistId, Mixtape mixtape);
    Mixtape addPlaylistToUser(final Playlist playlist, final String userId, Mixtape mixtape);
    Mixtape removePlaylist(final List<String> playlistIds, Mixtape mixtape);
}
