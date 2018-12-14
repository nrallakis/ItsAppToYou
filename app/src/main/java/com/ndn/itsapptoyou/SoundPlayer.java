package com.ndn.itsapptoyou;

import android.content.Context;
import android.media.MediaPlayer;

import com.ndn.itsapptoyou.model.Sound;

public class SoundPlayer {

    private MediaPlayer player;
    private Context context;

    private static SoundPlayer sSoundPlayer;

    private Sound lose;
    private Sound wheelSpin;
    private Sound winning;

    private SoundPlayer(Context appContext) {
        context = appContext;
        lose = new Sound("Lose", R.raw.losing_sound);
        wheelSpin = new Sound("Wheel Spin", R.raw.wheel_spinning_sound);
        winning = new Sound("Winning", R.raw.winning_sound);
    }

    public static SoundPlayer get(Context c) {
        if (sSoundPlayer == null) {
            sSoundPlayer = new SoundPlayer(c.getApplicationContext());
        }
        return sSoundPlayer;
    }

    private void playSound(Sound sound) {
        if (player != null && player.isPlaying()) {
            player.stop();
        }

        int soundResource = sound.getResourceId();
        player = MediaPlayer.create(context, soundResource);
        player.start();
    }

    public void playWinningSound() { playSound(winning);}

    public void playWheelSound() {
        playSound(wheelSpin);
    }

    public void playLosingSound() {
        playSound(lose);
    }

    public void start() {
        if (player != null) {
            player.start();
        }
    }

    public boolean isPlaying() {
        return player != null && player.isPlaying();
    }

    public void pause() {
        if (player == null) return;
        if (player.isPlaying()) {
            player.pause();
        }
    }

    public void stop() {
        if (player == null) return;
        player.stop();
        player.release();
        player = null;
    }
}
