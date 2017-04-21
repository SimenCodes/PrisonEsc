package no.ntnu.prisonesc;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by simen on 21.04.17.
 */

public class Audio {

    public static final String POP = "pop.mp3";
    public static final String LAND = "lande.m4a";
    public static final String PANG = "Pang.mp3";
    public static final String MONEY = "penge.mp3";

    public static void play(Context context, String fileName) {
        try {
            AssetFileDescriptor afd = context.getAssets().openFd(fileName);
            final MediaPlayer player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
