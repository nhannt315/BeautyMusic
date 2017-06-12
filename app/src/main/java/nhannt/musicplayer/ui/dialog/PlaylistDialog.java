package nhannt.musicplayer.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.adapter.PlaylistDialogAdapter;
import nhannt.musicplayer.data.database.DBQuery;
import nhannt.musicplayer.interfaces.RecyclerItemClickListener;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.objectmodel.Song;
import nhannt.musicplayer.utils.SystemUtils;

/**
 * Created by NhanNT on 04/25/2017.
 */

public class PlaylistDialog extends Dialog implements RecyclerItemClickListener, View.OnClickListener {

    private final Context mContext;
    private final Song itemSongToAdd;
    @BindView(R.id.tv_create_new_playlist)
    protected AppCompatTextView tvCreatePlaylist;
    @BindView(R.id.rv_playlist_list_dialog)
    protected RecyclerView rvPlaylist;
    private ArrayList<PlayList> mPlaylistList;

    public PlaylistDialog(@NonNull Context context, Song song) {
        super(context);
        this.mContext = context;
        this.itemSongToAdd = song;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_playlist);
        ButterKnife.bind(this);
        int statusHeight = SystemUtils.getStatusBarHeight(mContext.getResources());
        int actionHeight = SystemUtils.getActionBarHeight(mContext);
        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.9);
        int height = (int) ((mContext.getResources().getDisplayMetrics().heightPixels - statusHeight - actionHeight) * 0.7);

        getWindow().setLayout(width, height);
        setUpPlaylist();
        tvCreatePlaylist.setOnClickListener(this);
    }

    private void setUpPlaylist() {
        mPlaylistList = DBQuery.getInstance().getAllPlayList();
        PlaylistDialogAdapter mAdapter = new PlaylistDialogAdapter(mContext, mPlaylistList);
        mAdapter.setmClickListener(this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        rvPlaylist.setLayoutManager(layoutManager);
        rvPlaylist.setAdapter(mAdapter);
        Log.d("dialog","playlist");
    }

    @Override
    public void onItemClickListener(View view, int position) {
        Toast.makeText(mContext, mPlaylistList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_create_new_playlist) {
            dismiss();
            new CreatePlaylistDialog(mContext,itemSongToAdd).show();
            dismiss();
        }
    }


}
