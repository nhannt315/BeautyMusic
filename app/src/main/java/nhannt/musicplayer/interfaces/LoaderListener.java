package nhannt.musicplayer.interfaces;

import java.util.ArrayList;

public interface LoaderListener<T> {

    void onFinished(ArrayList<T> itemList);
}