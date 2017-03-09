package nhannt.musicplayer.interfaces;

/**
 * Created by nhannt on 01/03/2017.
 */

public interface IPresenter<T> {
    void onCreate();
    void onStart();
    void onStop();
    void onPause();
    void attachView(T view);
}
