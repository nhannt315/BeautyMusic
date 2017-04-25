package nhannt.musicplayer.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.PlayList;

/**
 * Created by NhanNT on 04/25/2017.
 */

public class PlaylistDialogAdapter extends RecyclerView.Adapter<PlaylistDialogAdapter.PlaylistViewHolder> {

    private Context mContext;
    private ArrayList<PlayList> mData;
    private LayoutInflater mLayoutInflater;
    private RecyclerItemClickListener mClickListener;

    public PlaylistDialogAdapter(Context mContext, ArrayList<PlayList> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setmClickListener(RecyclerItemClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_playlist_dialog, parent,false);
        PlaylistViewHolder holder = new PlaylistViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(PlaylistViewHolder holder, int position) {
        holder.pos = position;
        holder.tvPlaylistTitle.setText(mData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class PlaylistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        int pos;
        @BindView(R.id.tv_playlist_title_dialog)
        AppCompatTextView tvPlaylistTitle;

        public PlaylistViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null) {
                mClickListener.onItemClickListener(v, pos);
            }
        }
    }
}
