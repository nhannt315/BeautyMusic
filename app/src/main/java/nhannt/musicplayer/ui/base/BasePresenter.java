package nhannt.musicplayer.ui.base;

/**
 * Created by Jhordan on 13/10/15.
 */
public interface BasePresenter<V> {

    void attachedView(V view);

    void detachView();

    void onResume();

    void onItemSelected(int position);
}
