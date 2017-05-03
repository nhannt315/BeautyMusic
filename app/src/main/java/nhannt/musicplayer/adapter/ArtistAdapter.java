package nhannt.musicplayer.adapter;

import android.app.Activity;
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
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.Artist;
import nhannt.musicplayer.utils.Common;
import nhannt.musicplayer.utils.Navigator;

/**
 * Created by nhannt on 03/03/2017.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {

    public static int LAYOUT_ITEM_LIST = 0;
    public static int LAYOUT_ITEM_GRID = 1;



    private Activity mContext;
    private ArrayList<Artist> mData;
    private LayoutInflater mLayoutInflater;
    private RecyclerItemClickListener recyclerItemClickListener;
    private int layoutType;

    public ArtistAdapter(Activity mContext, ArrayList<Artist> mData) {
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
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ArtistViewHolder holder;
        View itemView;
        if (layoutType == LAYOUT_ITEM_LIST) {
            itemView = mLayoutInflater.inflate(R.layout.item_artist_list, parent, false);
        } else {
            itemView = mLayoutInflater.inflate(R.layout.item_artist_grid, parent, false);
        }

        holder = new ArtistViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        Artist item = mData.get(position);
        holder.tvArtistName.setText(item.getName());
        holder.tvArtistInfo.setText(item.getNumberOfSong() + " " + mContext.getString(R.string.song_low_case) +
                " | " + item.getNumberOfAlbum() + " " + mContext.getString(R.string.album_low_case));
        holder.position = position;
        if(Common.isLollipop()){
            holder.imageArtistCover.setTransitionName("transition_artist"+position);
        }
        if(layoutType == LAYOUT_ITEM_LIST) {
            Glide.with(mContext).load(item.getImageUrl())
                    .centerCrop().placeholder(R.drawable.music_background)
                    .dontAnimate().into(holder.imageArtistCover);
        }else{
            Glide.with(mContext).load(item.getImageUrl())
                    .centerCrop().placeholder(R.drawable.music_background)
                    .into(holder.imageArtistCover);
        }
    }



    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int position;

        @BindView(R.id.iv_cover_item_artist)
        protected ImageView imageArtistCover;
        @BindView(R.id.tv_artist_title_item)
        protected TextView tvArtistName;
        @BindView(R.id.tv_album_song_artist_item)
        protected TextView tvArtistInfo;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(ArtistViewHolder.this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            Navigator.navigateToArtistDetail(mContext,mData.get(position), this.imageArtistCover);
        }
    }
}
