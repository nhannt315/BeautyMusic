package nhannt.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.model.Song;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;

/**
 * Created by nhannt on 03/03/2017.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context mContext;
    private ArrayList<Song> mData;
    private LayoutInflater mLayoutInflater;
    private RecyclerItemClickListener recyclerItemClickListener;

    public SongAdapter(Context mContext, ArrayList<Song> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_song, parent, false);
        SongViewHolder holder = new SongViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song item = mData.get(position);
        holder.tvSongTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
        Glide.with(mContext).load(item.getCoverPath())
                .placeholder(R.drawable.music_background)
                .centerCrop()
                .dontAnimate()
                .into(holder.ivAlbumCover);
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{

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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerItemClickListener.onItemClickListener(position);
                }
            });
        }
    }
}
