package nhannt.musicplayer.ui.custom;

import android.annotation.TargetApi;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

/**
 * Created by nhannt on 28/03/2017.
 */
@TargetApi(21)
public class DetailTransition extends TransitionSet {
    public DetailTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).addTransition(new ChangeTransform()).addTransition(new ChangeImageTransform());
    }
}
