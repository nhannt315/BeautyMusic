package nhannt.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.interfaces.IMusicServiceConnection;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.service.MusicService;
import nhannt.musicplayer.service.MusicServiceConnection;
import nhannt.musicplayer.ui.dialog.PlaylistDialog;
import nhannt.musicplayer.utils.App;

/**
 * Created by nhannt on 03/03/2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context mContext;
    private ArrayList<Song> mData;
    private LayoutInflater mLayoutInflater;
    private RecyclerItemClickListener recyclerItemClickListener;
    private MusicServiceConnection mConnection;
    private int layoutId = R.layout.item_song;
    private boolean[] playState;

    public SongAdapter(Context mContext, ArrayList<Song> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
        if (mData != null)
            playState = new boolean[mData.size()];
//        for(boolean b : playState){
//            b = false;
//        }
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(layoutId, parent, false);
        return new SongViewHolder(view);
    }

    public void updatePlayPosition(String songId) {
        if (songId == null || mData == null || songId.isEmpty()) return;
        for (int i = 0; i < playState.length; i++) {
            playState[i] = false;
        }

        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getId().equals(songId)) {
                playState[i] = true;
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song item = mData.get(position);
        holder.tvSongTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
        if (playState[position]) {
            holder.tvSongTitle.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
            holder.tvArtist.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
        } else {
            holder.tvSongTitle.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
            holder.tvArtist.setTextColor(ContextCompat.getColor(mContext, R.color.blue));
        }
        Glide.with(mContext).load(item.getCoverPath())
                .placeholder(R.drawable.music_background)
                .centerCrop()
                .dontAnimate()
                .into(holder.ivAlbumCover);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }


    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private class OnItemClicked implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                if (recyclerItemClickListener != null)
                    recyclerItemClickListener.onItemClickListener(v, position);
            }
        }

        int position;
        @BindView(R.id.iv_cover_item_song)
        protected ImageView ivAlbumCover;
        @BindView(R.id.tv_song_title_item)
        protected TextView tvSongTitle;
        @BindView(R.id.tv_artist_song_item)
        protected TextView tvArtist;
        @BindView(R.id.btn_menu_song_item)
        protected ImageView btnPopupMenu;

        public SongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            OnItemClicked onItemClicked = new OnItemClicked();
            itemView.setOnClickListener(onItemClicked);
            btnPopupMenu.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_menu_song_item) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu_popup_song_item, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final int itemId = item.getItemId();
                        if (itemId == R.id.bt_play_popup_song || itemId == R.id.bt_play_next_popup_song ||
                                itemId == R.id.bt_add_queue_popup_song) {
                            mConnection = new MusicServiceConnection(App.getContext());
                            Intent iPlay = new Intent(App.getContext(), MusicService.class);
                            mConnection.connect(iPlay, new IMusicServiceConnection() {
                                @Override
                                public void onConnected(MusicService service) {
                                    switch (itemId) {
                                        case R.id.bt_play_popup_song:
                                            service.setLstSong(mData);
                                            service.setSongPos(position);
                                            service.playSong();
                                            break;
                                        case R.id.bt_play_next_popup_song:
                                            service.playNext(mData.get(position));
                                            break;
                                        case R.id.bt_add_queue_popup_song:
                                            service.addToQueue(mData.get(position));
                                            break;
                                    }
                                }
                            });
                        }
                        switch (item.getItemId()) {
                            case R.id.bt_add_playlist_popup_song:
                                new PlaylistDialog(mContext, mData.get(position)).show();
                                break;
                            case R.id.bt_to_album_popup_song:
                                break;
                            case R.id.bt_to_artist_popup_song:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        }
    }


}
