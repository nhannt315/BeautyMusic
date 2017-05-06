package nhannt.musicplayer.ui.base;


import android.support.annotation.NonNull;

/**
 * Created by Jhordan on 13/10/15.
 */
public interface BasePresenter<V> {

    void attachedView(@NonNull V view);

    void detachView();

    void onResume();

    void onDestroy();

}
