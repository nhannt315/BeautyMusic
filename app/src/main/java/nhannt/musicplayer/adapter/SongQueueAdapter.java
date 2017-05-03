package nhannt.musicplayer.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.recyclerhelper.ItemTouchHelperAdapter;
import nhannt.musicplayer.recyclerhelper.ItemTouchHelperViewHolder;
import nhannt.musicplayer.recyclerhelper.OnStartDragListener;
import nhannt.musicplayer.utils.App;

/**
 * Created by nhannt on 07/04/2017.
 */

public class SongQueueAdapter extends RecyclerView.Adapter<SongQueueAdapter.SongQueueViewHolder>
        implements ItemTouchHelperAdapter {
    private Context mContext;
    private ArrayList<Song> mData = new ArrayList<>();
    private final OnStartDragListener mStartDragListener;
    private LayoutInflater mInflater;

    public SongQueueAdapter(Context mContext, ArrayList<Song> mData, OnStartDragListener dragStartListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.mStartDragListener = dragStartListener;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public SongQueueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_song_queue, parent, false);
        SongQueueViewHolder holder = new SongQueueViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final SongQueueViewHolder holder, int position) {
        Song item = mData.get(position);
        holder.tvSongTitle.setText(item.getTitle());
        holder.tvArtist.setText(item.getArtist());
        Glide.with(mContext)
                .load(item.getCoverPath())
                .thumbnail(0.5f)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .placeholder(R.drawable.song)
                .into(holder.ivAlbumCover);
        holder.btnDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        App.getInstance().updateListSong(mData);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        if(mData.get(position).getId() != App.getInstance().getCurrentPlayingSong().getId()) {
            Song item = mData.get(position);
            mData.remove(position);

            Toast.makeText(mContext, item.getTitle() + " " + mContext.getString(R.string.deleted), Toast.LENGTH_SHORT).show();
            App.getInstance().updateListSong(mData);
        }
        notifyDataSetChanged();
    }

    private void showCustomToast() {

    }

    public class SongQueueViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        @BindView(R.id.bt_sort_queue)
        ImageView btnDrag;
        @BindView(R.id.tv_song_title_item_queue)
        TextView tvSongTitle;
        @BindView(R.id.tv_artist_song_item_queue)
        TextView tvArtist;
        @BindView(R.id.btn_menu_song_item_queue)
        ImageView btPopupMenu;
        @BindView(R.id.iv_cover_item_song_queue)
        ImageView ivAlbumCover;

        public SongQueueViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }


}
