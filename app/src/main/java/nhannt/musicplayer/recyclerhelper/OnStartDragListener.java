package nhannt.musicplayer.recyclerhelper;
import android.support.v7.widget.RecyclerView;

/**
 * Created by nhannt on 07/04/2017.
 */

public interface OnStartDragListener {
    /**
     * Called when a view is requesting a start of a drag.
     *
     * @param viewHolder The holder of the view to drag.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}
