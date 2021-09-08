package fr.takehere.ethereal.utils;

import javax.sound.sampled.*;

import java.io.IOException;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class SoundUtil {

    public static AudioInputStream getSoundRessource(String name, Class mainClass){
        try {
            return getAudioInputStream(mainClass.getResource("ressources/" + name));
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void playSound(AudioInputStream audio){
        try {
            AudioFormat outDataFormat = new AudioFormat(44100.0f, 16, audio.getFormat().getChannels(), true, audio.getFormat().isBigEndian());
            AudioInputStream outputAudio = getAudioInputStream(outDataFormat, audio);

            Clip clip = AudioSystem.getClip();
            clip.open(outputAudio);
            clip.start();
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}
