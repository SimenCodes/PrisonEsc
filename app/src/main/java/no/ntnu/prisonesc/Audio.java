package no.ntnu.prisonesc;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by simen on 21.04.17.
 */

public class Audio {

    public static void play(Context context, String fileName) {
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(fileName);
            MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
