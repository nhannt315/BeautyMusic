package nhannt.musicplayer.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import nhannt.musicplayer.R;
import nhannt.musicplayer.data.network.lastfmapi.LastFmApi;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Album;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.utils.Navigator;

/**
 * Created by NhanNT on 05/09/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    private static final int ITEM_SONG_TYPE = 1;
    private static final int ITEM_ALBUM_TYPE = 2;
    private static final int ITEM_ARTIST_TYPE = 3;
    private static final int ITEM_HEADER_TYPE = 4;
    private ArrayList lstResult;

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private RecyclerItemClickListener itemClickListener;




    public SearchAdapter(Context context) {

        this.mContext = context;
        lstResult = new ArrayList();
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public SearchAdapter(Context context, ArrayList data) {
        this.mContext = context;
        this.lstResult = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setItemClickListener(RecyclerItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (lstResult.get(position) instanceof Song) {
            return ITEM_SONG_TYPE;
        }
        if (lstResult.get(position) instanceof Album) {
            return ITEM_ALBUM_TYPE;
        }
        if (lstResult.get(position) instanceof Artist) {
            return ITEM_ARTIST_TYPE;
        }
        if (lstResult.get(position) instanceof String) {
            return ITEM_HEADER_TYPE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_song, parent, false);
        switch (viewType) {
            case ITEM_SONG_TYPE:
                view = mLayoutInflater.inflate(R.layout.item_song, parent, false);
                break;
            case ITEM_ALBUM_TYPE:
                view = mLayoutInflater.inflate(R.layout.item_album_list, parent, false);
                break;
            case ITEM_ARTIST_TYPE:
                view = mLayoutInflater.inflate(R.layout.item_artist_list, parent, false);
                break;
            case ITEM_HEADER_TYPE:
                view = mLayoutInflater.inflate(R.layout.search_header_item, parent, false);
                break;
        }
        return new SearchHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        holder.pos = position;
        switch (getItemViewType(position)) {
            case ITEM_SONG_TYPE:
                Song song = (Song) lstResult.get(position);

                holder.songArtist.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                holder.songTitle.setTextColor(ContextCompat.getColor(mContext, R.color.black));

                holder.songTitle.setText(song.getTitle());
                holder.songArtist.setText(song.getArtist());


                Glide.with(mContext).load(song.getCoverPath()).dontAnimate()
                        .placeholder(R.drawable.google_play_music_logo).into(holder.albumCoverSong);
                break;
            case ITEM_ALBUM_TYPE:
                Album album = (Album) lstResult.get(position);
                holder.albumTitle.setText(album.getTitle());
                holder.albumArtist.setText(album.getArtist());
                Glide.with(mContext).load(album.getCoverPath()).dontAnimate()
                        .placeholder(R.drawable.music_background_new).into(holder.albumCoverAlbum);
                break;
            case ITEM_ARTIST_TYPE:
                Artist artist = (Artist) lstResult.get(position);
                holder.artistName.setText(artist.getName());
                holder.artistInfo.setText(artist.getNumberOfSong() + " " + mContext.getString(R.string.song_low_case) +
                        " | " + artist.getNumberOfAlbum() + " " + mContext.getString(R.string.album_low_case));
                new LastFmApi(mContext).getArtistPhoto(artist.getName(), holder.artistCover, false);
                break;
            case ITEM_HEADER_TYPE:
                String header = (String) lstResult.get(position);
                holder.headerTitle.setText(header);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return lstResult.size();
    }

    public void updateSearchResult(ArrayList lstResult) {
        this.lstResult = lstResult;
        notifyDataSetChanged();
    }

    public class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int pos;

         TextView songTitle;
         TextView songArtist;
         ImageView albumCoverSong;
         ImageView popupMenuSong;

         ImageView albumCoverAlbum;
         TextView albumArtist;
         TextView albumTitle;

         ImageView artistCover;
         TextView artistName;
         TextView artistInfo;

         TextView headerTitle;

        public SearchHolder(View itemView) {
            super(itemView);
            songTitle = (TextView) itemView.findViewById(R.id.tv_song_title_item);
            songArtist = (TextView) itemView.findViewById(R.id.tv_artist_song_item);
            albumCoverSong = (ImageView) itemView.findViewById(R.id.iv_cover_item_song);
            popupMenuSong = (ImageView) itemView.findViewById(R.id.btn_menu_song_item);

            albumCoverAlbum = (ImageView) itemView.findViewById(R.id.iv_cover_item_album);
            albumTitle = (TextView) itemView.findViewById(R.id.tv_album_title_item);
            albumArtist = (TextView) itemView.findViewById(R.id.tv_artist_album_item);

            artistName = (TextView) itemView.findViewById(R.id.tv_artist_title_item);
            artistInfo = (TextView) itemView.findViewById(R.id.tv_album_song_artist_item);
            artistCover = (ImageView) itemView.findViewById(R.id.iv_cover_item_artist);

            headerTitle = (TextView) itemView.findViewById(R.id.tv_search_header);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null)
                itemClickListener.onItemClickListener(itemView, pos);
            switch (getItemViewType()) {
                case ITEM_SONG_TYPE:
                    break;
                case ITEM_ALBUM_TYPE:
                    Navigator.gotoAlbumDetail(mContext, (Album) lstResult.get(pos));
                    break;
                case ITEM_ARTIST_TYPE:
                    Navigator.gotoArtistDetail(mContext, (Artist) lstResult.get(pos));
                    break;
            }
        }
    }
}
