package nhannt.musicplayer.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import nhannt.musicplayer.R;
import nhannt.musicplayer.data.database.DBQuery;
import nhannt.musicplayer.objectmodel.PlayList;
import nhannt.musicplayer.objectmodel.Song;

/**
 * Created by NhanNT on 04/25/2017.
 */

public class CreatePlaylistDialog extends Dialog implements View.OnClickListener {

    private final Context mContext;
    @BindView(R.id.tv_create_playlist)
    protected TextView tvCreate;
    @BindView(R.id.tv_cancel_create_playlist)
    protected TextView tvCancel;
    @BindView(R.id.edt_playlist)
    protected EditText edtPlaylist;

    private Song itemSongToAdd;
    private final DBQuery dbQuery = DBQuery.getInstance();
    private boolean isInsertSong = false;


    public CreatePlaylistDialog(@NonNull Context context, Song song) {
        super(context);
        this.mContext = context;
        this.itemSongToAdd = song;
        isInsertSong = true;
    }

    public CreatePlaylistDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        isInsertSong = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_playlist_dialog);

        int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.9);
        getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);
        tvCancel.setOnClickListener(this);
        tvCreate.setOnClickListener(this);
        edtPlaylist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (input == null && input.isEmpty()) {
                    tvCreate.setClickable(false);
                    tvCreate.setTextColor(ContextCompat.getColor(mContext, R.color.light_grey));
                } else {
                    tvCreate.setClickable(true);
                    tvCreate.setTextColor(ContextCompat.getColor(mContext, R.color.colorRed));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create_playlist:
                String title = edtPlaylist.getText().toString().trim();
                long result = dbQuery.insertPlaylist(title);
                if (result > 0) {
                    PlayList playList = dbQuery.getLastInsertedPlaylist();
                    if (isInsertSong) {
                        boolean isSuccess = dbQuery.insertSongToPlaylist(playList.getId(), itemSongToAdd.getId());
                        if (isSuccess)
                            Toast.makeText(mContext, "1" + mContext.getString(R.string.song_added_to_playlist), Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(mContext, mContext.getString(R.string.failed_insert_song), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, mContext.getString(R.string.failed_to_create_playlist), Toast.LENGTH_SHORT).show();
                }

                dismiss();
                break;
            case R.id.tv_cancel_create_playlist:
                dismiss();
                break;
        }
    }
}
