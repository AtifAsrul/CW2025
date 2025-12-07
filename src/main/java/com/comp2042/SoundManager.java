package com.comp2042;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;

/**
 * Manages sound effects and background music for the game using MIDI.
 */
public class SoundManager {

    private Synthesizer synthesizer;
    private MidiChannel[] channels;

    // Instrument Program Numbers (General MIDI 0-indexed)
    private static final int INSTRUMENT_ALTO_SAX = 65;
    private static final int INSTRUMENT_ELECTRIC_PIANO = 4;
    private static final int INSTRUMENT_ACOUSTIC_BASS = 32;
    private static final int INSTRUMENT_BRASS_SECTION = 61;

    // Channel Assignments
    private static final int CHAN_SAX = 0;
    private static final int CHAN_PIANO = 1;
    private static final int CHAN_BASS = 2;
    private static final int CHAN_BRASS = 3;
    private static final int CHAN_DRUMS = 9; // Standard GM Drum Channel

    // C Minor Blues Scale Notes for Hard Drop (starting from C3 = 48)
    private static final int[] C_MINOR_BLUES_SCALE = {
            48, // C3
            51, // Eb3
            53, // F3
            54, // Gb3
            55, // G3
            58, // Bb3
            60, // C4
            63, // Eb4
            65, // F4
            66 // Gb4
    };

    /**
     * Constructs a new SoundManager and initializes the MIDI synthesizer.
     */
    public SoundManager() {
        setupMidi();
    }

    /**
     * Sets up the MIDI synthesizer, loads sound fonts, and configures channels.
     */
    private void setupMidi() {
        try {
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();

            // Load Custom SoundFont
            File soundFontFile = new File("GeneralUserGS.sf2");
            if (!soundFontFile.exists()) {
                File resourceFile = new File("src/main/resources/GeneralUserGS.sf2");
                if (resourceFile.exists()) {
                    soundFontFile = resourceFile;
                }
            }

            if (soundFontFile.exists()) {
                Soundbank customSoundbank = MidiSystem.getSoundbank(soundFontFile);
                if (customSoundbank != null) {
                    boolean hasAltoSax = false;
                    for (Instrument instr : customSoundbank.getInstruments()) {
                        if (instr.getPatch().getProgram() == INSTRUMENT_ALTO_SAX) {
                            hasAltoSax = true;
                            break;
                        }
                    }
                    if (hasAltoSax) {
                        synthesizer.loadAllInstruments(customSoundbank);
                    } else {
                        System.err
                                .println("Warning: Custom Soundbank missing Alto Sax (Prog 65). Using system default.");
                    }
                }
            }

            // Get channels
            channels = synthesizer.getChannels();

            // Program Change
            setInstrument(CHAN_SAX, INSTRUMENT_ALTO_SAX);
            setInstrument(CHAN_PIANO, INSTRUMENT_ELECTRIC_PIANO);
            setInstrument(CHAN_BASS, INSTRUMENT_ACOUSTIC_BASS);
            setInstrument(CHAN_BRASS, INSTRUMENT_BRASS_SECTION);

        } catch (MidiUnavailableException | InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the instrument for a specific MIDI channel.
     *
     * @param channel the channel index.
     * @param program the instrument program number.
     */
    private void setInstrument(int channel, int program) {
        if (channels != null && channel < channels.length) {
            channels[channel].programChange(program);
        }
    }

    /**
     * Plays a hard drop sound effect based on the column index.
     * Uses a pentatonic scale for variation.
     *
     * @param column the column index where the drop occurred.
     */
    public void playHardDrop(int column) {
        if (channels == null)
            return;
        int effectiveColumn = Math.max(0, column);
        int noteIndex = Math.min(effectiveColumn, C_MINOR_BLUES_SCALE.length - 1);
        int note = C_MINOR_BLUES_SCALE[noteIndex];

        channels[CHAN_SAX].noteOn(note, 50); // Lowered from 110 to match Tetris volume

        new Thread(() -> {
            try {
                Thread.sleep(200);
                if (channels != null)
                    channels[CHAN_SAX].noteOff(note);
            } catch (InterruptedException e) {
            }
        }).start();
    }

    /**
     * Plays a sound effect for clearing lines.
     * The chord played depends on the number of lines cleared.
     *
     * @param lines the number of lines cleared.
     */
    public void playLineClear(int lines) {
        if (channels == null || lines <= 0)
            return;

        int[] chord = null;
        int channel = CHAN_PIANO;
        int velocity = 100;
        int duration = 800;

        switch (lines) {
            case 1: // Cm9
                chord = new int[] { 48, 51, 55, 58, 62 }; // C3, Eb3, G3, Bb3, D4
                break;
            case 2: // Fm9
                chord = new int[] { 53, 56, 60, 63, 67 }; // F3, Ab3, C4, Eb4, G4
                break;
            case 3: // G7(#9)
                chord = new int[] { 55, 59, 65, 70, 75 }; // G3, B3, F4, Bb4, Eb5
                break;
            case 4: // CmM7 (Brass)
                chord = new int[] { 60, 63, 67, 71, 72 }; // C4, Eb4, G4, B4, C5
                channel = CHAN_BRASS;
                velocity = 45; // Drastically reduced from 90 (was 127)
                duration = 1200;
                break;
            default:
                return;
        }

        final int[] finalChord = chord;
        final int finalChannel = channel;
        final int finalVelocity = velocity;
        final int finalDuration = duration;

        for (int note : finalChord) {
            channels[finalChannel].noteOn(note, finalVelocity);
        }

        new Thread(() -> {
            try {
                Thread.sleep(finalDuration);
                for (int note : finalChord) {
                    if (channels != null)
                        channels[finalChannel].noteOff(note);
                }
            } catch (InterruptedException e) {
            }
        }).start();
    }

    /**
     * Plays the game over sound effect.
     * Stops the background music and plays a dissonant brass cluster.
     */
    public void playGameOver() {
        stopBackgroundMusic(); // Stop BGM
        if (channels == null)
            return;

        int velocity = 40; // Drastically reduced from 127 to 40

        // Dissonant Cluster on Brass
        int[] brassCluster = { 48, 49, 54, 55 };
        for (int note : brassCluster) {
            channels[CHAN_BRASS].noteOn(note, velocity);
        }

        // Bass Thud
        channels[CHAN_BASS].noteOn(24, 50);

        // Sustain for a short while (3s) then cut
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                for (int note : brassCluster) {
                    if (channels != null)
                        channels[CHAN_BRASS].noteOff(note);
                }
                if (channels != null)
                    channels[CHAN_BASS].noteOff(24);
            } catch (InterruptedException e) {
            }
        }).start();
    }

    /**
     * Plays a sound effect when a brick is held.
     */
    public void playHold() {
        if (channels == null)
            return;
        // Channel 9 is Drums, Note 73 is Short Guiro
        if (channels.length > CHAN_DRUMS) {
            channels[CHAN_DRUMS].noteOn(73, 100);
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                    if (channels != null)
                        channels[CHAN_DRUMS].noteOff(73);
                } catch (InterruptedException e) {
                }
            }).start();
        }
    }

    /**
     * Stops all currently playing MIDI notes.
     */
    public void stopAllSounds() {
        if (channels == null)
            return;
        for (MidiChannel ch : channels) {
            ch.allNotesOff();
        }
    }

    private javafx.scene.media.MediaPlayer mediaPlayer;

    /**
     * Plays the background music from the resources.
     * loops indefinitely.
     */
    public void playBackgroundMusic() {
        try {
            String musicFile = "SwingBGM.mp3";
            java.net.URL resource = getClass().getClassLoader().getResource(musicFile);
            if (resource != null) {
                javafx.scene.media.Media sound = new javafx.scene.media.Media(resource.toExternalForm());
                mediaPlayer = new javafx.scene.media.MediaPlayer(sound);
                mediaPlayer.setCycleCount(javafx.scene.media.MediaPlayer.INDEFINITE);
                mediaPlayer.setVolume(0.5); // Set initial volume to 50%
                mediaPlayer.play();
            } else {
                System.err.println("Music file not found: " + musicFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the background music.
     */
    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    /**
     * Cleans up resources, closing the synthesizer and stopping music.
     */
    public void cleanup() {
        if (synthesizer != null && synthesizer.isOpen()) {
            synthesizer.close();
        }
        stopBackgroundMusic();
    }
}
