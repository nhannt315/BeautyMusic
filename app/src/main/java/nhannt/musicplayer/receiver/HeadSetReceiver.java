package nhannt.musicplayer.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import nhannt.musicplayer.service.MusicService;

/**
 * Created by nhannt on 30/03/2017.
 * A broadcast receiver will pause music if its playing when headphone is unplugged
 */

public class HeadSetReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            Log.d("headset received","");
            Intent intent1 = new Intent(context, MusicService.class);
            intent1.setAction(MusicService.ACTION_PAUSE);
            context.startService(intent1);
        }

    }
}
