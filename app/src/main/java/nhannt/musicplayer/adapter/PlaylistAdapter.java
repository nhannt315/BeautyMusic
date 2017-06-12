package nhannt.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.ui.playlistdetail.PlaylistDetailActivity;
import nhannt.musicplayer.utils.Common;

/**
 * Created by NhanNT on 04/21/2017.
 */

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistBigViewHolder> {

    private final Context mContext;
    private final ArrayList<PlayList> mData;
    private final LayoutInflater mLayoutInflater;

    public PlaylistAdapter(Context mContext, ArrayList<PlayList> mData) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.mData = mData;

    }

    @Override
    public PlaylistBigViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.playlist_big_item, parent, false);
        return new PlaylistBigViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaylistBigViewHolder holder, int position) {
        PlayList item = mData.get(position);
        holder.pos = position;
        if (item.isAutoPlaylist())
            holder.tvAutoPlaylist.setVisibility(View.VISIBLE);
        else
            holder.tvAutoPlaylist.setVisibility(View.INVISIBLE);
        holder.tvPlaylistNumber.setText(String.valueOf(position));
        holder.tvPlaylistTitle.setText(item.getTitle());
        if (item.getLstSong() != null)
            holder.tvPlaylistSongNum.setText(item.getLstSong().size() + " " + mContext.getString(R.string.song_low_case));
        else
            holder.tvPlaylistSongNum.setText(mContext.getString(R.string.song));
        if (Common.isLollipop()) {
            holder.tvPlaylistTitle.setTransitionName("transition_playList_title" + position);
            holder.ivBackGround.setTransitionName("transition_playlist_cover" + position);
        }
        if (item.getLstSong() != null && item.getLstSong().size() > 0) {
            String backgroundPath = item.getLstSong()
                    .get(0).getCoverPath();
            Log.d(item.getTitle(), item.getLstSong().size() + "");
            Glide.with(mContext).load(backgroundPath)
                    .placeholder(R.drawable.music_background)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.ivBackGround);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class PlaylistBigViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_auto_playlist)
        TextView tvAutoPlaylist;
        @BindView(R.id.tv_playlist_title_item)
        TextView tvPlaylistTitle;
        @BindView(R.id.tv_playlist_number)
        TextView tvPlaylistNumber;
        @BindView(R.id.tv_song_nums_playlist_item)
        TextView tvPlaylistSongNum;
        @BindView(R.id.iv_background_playlist_item)
        ImageView ivBackGround;

        int pos;

        public PlaylistBigViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intentToDetail = new Intent(mContext, PlaylistDetailActivity.class);
            if (Common.isLollipop()) {
                intentToDetail.putExtra(PlaylistDetailActivity.KEY_COVER_TRANSITION, ivBackGround.getTransitionName());
                intentToDetail.putExtra(PlaylistDetailActivity.KEY_TITLE_TRANSITION, tvPlaylistTitle.getTransitionName());
            }
            intentToDetail.putExtra(PlaylistDetailActivity.KEY_PLAYLIST_ID, mData.get(pos).getId());
            String path = null;
            if (mData.get(pos).getLstSong().size() > 0)
                path = mData.get(pos).getLstSong().get(0).getCoverPath();
            intentToDetail.putExtra(PlaylistDetailActivity.KEY_COVER_PATH, path);

            if (Common.isLollipop()) {
                Pair<View, String> pairCover = Pair.create((View) ivBackGround, ivBackGround.getTransitionName());
                Pair<View, String> pairTitle = Pair.create((View) tvPlaylistTitle, tvPlaylistTitle.getTransitionName());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((AppCompatActivity) mContext, pairCover, pairTitle);
                mContext.startActivity(intentToDetail, options.toBundle());
            } else {
                mContext.startActivity(intentToDetail);
            }
        }
    }
}
