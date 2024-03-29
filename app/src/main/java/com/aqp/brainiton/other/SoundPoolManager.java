package com.aqp.brainiton.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.aqp.brainiton.R;

public class SoundPoolManager {
    private static final String TAG = SoundPoolManager.class.getSimpleName();

    private static SoundPool soundPool;
    private static int[] sm;
    private final Context context;
    private static float mVolume;

    private static SoundPoolManager instance;
    private static final int SOUND_TOTAL = 4;

    int soundRawId, soundRawId1, soundRawId2, soundRawId3;

    private SoundPoolManager(Context context) {
        this.context = context;
        initSound();

        // add sound here
        // here the sample audio file which can be use with your audio file
        soundRawId = R.raw.cluck;
        soundRawId1 = R.raw.clack;
        soundRawId2 = R.raw.correct;
        soundRawId3 = R.raw.wrong;

        //you need to change SOUND_TOTAL for the size of the audio samples.
        sm[sm.length - 4] = soundPool.load(context, soundRawId, 1);
        sm[sm.length - 3] = soundPool.load(context, soundRawId1, 1);
        sm[sm.length - 2] = soundPool.load(context, soundRawId2, 1);
        sm[sm.length - 1] = soundPool.load(context, soundRawId3, 1);
    }

    public static void instantiate(Context context) {
        if(instance == null) instance = new SoundPoolManager(context.getApplicationContext());
    }

    private void initSound() {
        sm = new int[SOUND_TOTAL];
        int maxStreams = 2;
        soundPool = new SoundPool.Builder()
                .setMaxStreams(maxStreams)
                .build();
        mVolume = setupVolume(context);
    }

    private float setupVolume(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(am == null) {
            Log.e(TAG, "Can't access AudioManager!");
            return 0;
        }

        float actualVolume = (float) am.getStreamVolume(AudioManager.STREAM_ALARM);
        float maxVolume = (float) am.getStreamMaxVolume(AudioManager.STREAM_ALARM);

        return actualVolume / maxVolume;
    }

    public static void playSound(int index) {
        if(sm == null) {
            Log.e(TAG, "sm is null, this should not happened!");
            return;
        }

        if(soundPool == null) {
            Log.e(TAG, "SoundPool is null, this should not happened!");
            return;
        }

        if(sm.length <= index) {
            Log.e(TAG, "No sound with index = " + index);
            return;
        }

        if(mVolume > 0) {
            soundPool.play(sm[index], mVolume, mVolume, 1, 0, 1f);
        }
    }

    public static void cleanUp() {
        sm = null;
        if(soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
    public static void stop() {
        soundPool.release();
    }
}
