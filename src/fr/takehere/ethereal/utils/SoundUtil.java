package fr.takehere.ethereal.utils;

import javax.sound.sampled.*;

import java.io.IOException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class SoundUtil {

    public static AudioInputStream getSoundRessource(String path, Class mainClass){
        try {
            return getAudioInputStream(mainClass.getResource("ressources/" + path));
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void playSound(AudioInputStream audio, float volume){
        try {
            AudioFormat outDataFormat = new AudioFormat(44100.0f, 16, audio.getFormat().getChannels(), true, audio.getFormat().isBigEndian());
            AudioInputStream outputAudio = getAudioInputStream(outDataFormat, audio);

            Clip clip = AudioSystem.getClip();
            clip.open(outputAudio);

            setVolume(clip, volume);

            clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Clip playLoopSound(AudioInputStream audio, float volume){
        Thread thread = new Thread(() -> {
            try {
                AudioFormat outDataFormat = new AudioFormat(44100.0f, 16, audio.getFormat().getChannels(), true, audio.getFormat().isBigEndian());
                AudioInputStream outputAudio = getAudioInputStream(outDataFormat, audio);

                Clip clip = AudioSystem.getClip();
                clip.open(outputAudio);

                setVolume(clip, volume);

                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.run();
        return null;
    }

    public static Clip setVolume(Clip clip, float volume){
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));

        return clip;
    }
}
