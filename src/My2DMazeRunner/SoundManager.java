package My2DMazeRunner;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class SoundManager {
    private Clip menuMusicClip;
    private Clip gameMusicClip;
    
    private boolean menuMusicPlaying = false;
    public boolean gameMusicPlaying = false;
    
    public SoundManager() {
        // Initialize sound manager
    }
    
    /**
     * Play background music for main menu (looping)
     */
    public void playMenuMusic() {
        if (menuMusicPlaying) {
            return; // Already playing
        }
        
        stopGameMusic(); // Stop game music if playing
        
        try {
            InputStream audioStream = getClass().getResourceAsStream("/music/MainMenu.wav");
            if (audioStream == null) {
                System.err.println("MainMenu.wav not found in resources");
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioStream);
            menuMusicClip = AudioSystem.getClip();
            menuMusicClip.open(audioInputStream);
            menuMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            menuMusicPlaying = true;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error loading MainMenu.wav: " + e.getMessage());
        }
    }
    
    /**
     * Play background music for game (looping)
     */
    public void playGameMusic() {
        if (gameMusicPlaying) {
            return; // Already playing
        }
        
        stopMenuMusic(); // Stop menu music if playing
        
        try {
            InputStream audioStream = getClass().getResourceAsStream("/music/MainGame1.wav");
            if (audioStream == null) {
                System.err.println("MainGame1.wav not found in resources");
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioStream);
            gameMusicClip = AudioSystem.getClip();
            gameMusicClip.open(audioInputStream);
            gameMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
            gameMusicPlaying = true;
        } catch (UnsupportedAudioFileException e) {
            System.err.println("Unsupported audio format: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error loading MainGame1.wav: " + e.getMessage());
        }
    }
    
    /**
     * Play click sound effect (one time)
     */
    public void playClickSound() {
        try {
            InputStream audioStream = getClass().getResourceAsStream("/music/Clickmenu.wav");
            if (audioStream == null) {
                System.err.println("Clickmenu.wav not found in resources");
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            // Silently fail for click sound to avoid spam in console
        } catch (LineUnavailableException e) {
            // Silently fail for click sound
        } catch (IOException e) {
            // Silently fail for click sound
        }
    }
    
    /**
     * Stop menu music
     */
    public void stopMenuMusic() {
        if (menuMusicClip != null && menuMusicClip.isRunning()) {
            menuMusicClip.stop();
            menuMusicClip.close();
            menuMusicClip = null;
        }
        menuMusicPlaying = false;
    }
    
    /**
     * Stop game music
     */
    public void stopGameMusic() {
        if (gameMusicClip != null && gameMusicClip.isRunning()) {
            gameMusicClip.stop();
            gameMusicClip.close();
            gameMusicClip = null;
        }
        gameMusicPlaying = false;
    }
    
    /**
     * Stop all sounds
     */
    public void stopAll() {
        stopMenuMusic();
        stopGameMusic();
    }
}

