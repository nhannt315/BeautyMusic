package nhannt.musicplayer.ui.itemlist;

import java.util.ArrayList;

public interface LoaderListener<T> {

    void onFinished(ArrayList<T> itemList);
}