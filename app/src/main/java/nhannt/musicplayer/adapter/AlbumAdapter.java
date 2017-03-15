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
import de.hdodenhof.circleimageview.CircleImageView;
import nhannt.musicplayer.R;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.model.Album;

/**
 * Created by nhannt on 03/03/2017.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {
    public static int LAYOUT_ITEM_LIST = 0;
    public static int LAYOUT_ITEM_GRID = 1;

    private Context mContext;
    private ArrayList<Album> mData;
    private int layoutType;
    private LayoutInflater mLayoutInflater;
    private RecyclerItemClickListener recyclerItemClickListener;

    public AlbumAdapter(Context mContext, ArrayList<Album> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public void setRecyclerItemClickListener(RecyclerItemClickListener recyclerItemClickListener) {
        this.recyclerItemClickListener = recyclerItemClickListener;
    }

    public int getLayoutType() {
        return layoutType;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AlbumViewHolder holder;
        View itemView;
        if (layoutType == LAYOUT_ITEM_LIST) {
            itemView = mLayoutInflater.inflate(R.layout.item_album_list, parent, false);
        } else {
            itemView = mLayoutInflater.inflate(R.layout.item_album_grid, parent, false);
        }
        holder = new AlbumViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        Album item = mData.get(position);
        holder.tvAlbumTitle.setText(item.getTitle());
        holder.tvArtistName.setText(item.getArtist());
        Glide.with(mContext).load(item.getCoverPath())
                .placeholder(R.drawable.music_background)
                .centerCrop()
                .into(holder.albumCover);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_cover_item_album)
        protected ImageView albumCover;
        @BindView(R.id.tv_album_title_item)
        protected TextView tvAlbumTitle;
        @BindView(R.id.tv_artist_album_item)
        protected TextView tvArtistName;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
