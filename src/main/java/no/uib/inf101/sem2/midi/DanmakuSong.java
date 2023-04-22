package no.uib.inf101.sem2.midi;

import java.io.InputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

public class DanmakuSong implements Runnable{
  
  private String DANMAKUMUSIC;
  private Sequencer sequencer;

  /**
   * TetrisSong constructor takes String version of a midi file 
   * to play it's music. 
   * Used to change music at the same time the program is running.
   */
  public DanmakuSong(String DANMAKUMUSIC) {
      this.DANMAKUMUSIC = DANMAKUMUSIC;
  } 

  @Override
  public void run() {
      InputStream song = DanmakuSong.class.getClassLoader().getResourceAsStream(this.DANMAKUMUSIC);
      this.doPlayMidi(song, true);
  }

  private void doPlayMidi(final InputStream is, final boolean loop) {
      try {
          this.doStopMidiSounds();
          (this.sequencer = MidiSystem.getSequencer()).setSequence(MidiSystem.getSequence(is));
          if (loop) {
              this.sequencer.setLoopCount(-1);
          }
          this.sequencer.open();
          this.sequencer.start();
      }
      catch (Exception e) {
          this.midiError("" + e);
      }
  }

  public void doStopMidiSounds() {
      try {
          if (this.sequencer == null || !this.sequencer.isRunning()) {
              return;
          }
          this.sequencer.stop();
          this.sequencer.close();
      }
      catch (Exception e) {
          this.midiError("" + e);
      }
      this.sequencer = null;
  }

  public void doPauseMidiSounds() {
      try {
          if (this.sequencer == null || !this.sequencer.isRunning()) {
              return;
          }
          this.sequencer.stop();
      }
      catch (Exception e) {
          this.midiError("" + e);
      }
  }
    
  public void doUnpauseMidiSounds() {
      try {
          if (this.sequencer == null) {
              return;
          }
          this.sequencer.start();
      }
      catch (Exception e) {
          this.midiError("" + e);
      }
  }

  private void midiError(final String msg) {
      System.err.println("Midi error: " + msg);
      this.sequencer = null;
  }


}
