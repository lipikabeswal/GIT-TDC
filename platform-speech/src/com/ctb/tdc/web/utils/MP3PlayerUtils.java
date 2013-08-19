package com.ctb.tdc.web.utils;


import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.CompoundControl;
import javax.sound.sampled.Control;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Mixer.Info;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javazoom.jl.player.Player;

public class MP3PlayerUtils {
	private String filename;
	private static Player player;

	// constructor that takes the name of an MP3 file
	public MP3PlayerUtils(String filename) {
		this.filename = filename;
	}

	public MP3PlayerUtils() {

	}

	public void stop() {
		if (player != null)
			player.close();
		player = null;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void play(String filename) {
		this.filename = filename;
		play();
	}

	// play the MP3 file to the sound card
	public void play() {
		try {
			FileInputStream fis = new FileInputStream(filename);
			BufferedInputStream bis = new BufferedInputStream(fis);
			player = new Player(bis);
		} catch (Exception e) {
			System.out.println("Problem playing file " + filename);
			System.out.println(e);
		}

		// run in new thread to play in background
		new Thread() {
			public void run() {
				try {
					player.play();
					player = null;
				} catch (Exception e) {
					System.out.println(e.getMessage() + " frame "
							+ player.getPosition());
				}
			}
		}.start();
	}

	public boolean isPlaying() {
		if (player == null) {
			return false;
		} else {
			return !player.isComplete();
		}
	}

	// play the MP3 file to the sound card
	public void play(InputStream io) {
		try {
			BufferedInputStream bis = new BufferedInputStream(io);
			player = new Player(bis);
		} catch (Exception e) {
			System.out.println("Problem playing file " + filename);
			System.out.println(e);
		}

		// run in new thread to play in background
		new Thread() {
			public void run() {
				try {
					player.play();
					player = null;
				} catch (Exception e) {
					System.out.println(e.getMessage() + " frame "
							+ player.getPosition());
				}
			}
		}.start();
	}

	public Player getPlayer() {
		return player;
	}
	
	public void setVolume(String vol) {
	float volume =Float.parseFloat(vol)/100;
	Audio.setMasterOutputVolume(volume);
	}
	
/*
	public void setVolume(String vol) {
		System.out.println("Inside setvolume!!!!!");
		Info source[] = { Port.Info.SPEAKER, Port.Info.LINE_OUT,
				Port.Info.HEADPHONE,  Port.Info.COMPACT_DISC};
		int i = 0;
		while (i < 4) {
			System.out.println("Inside While Loop!!!");
			if (AudioSystem.isLineSupported(source[i])) {
				System.out.println("source "+source[i]+ " i "+i);
				try {
					Port outline = (Port) AudioSystem.getLine(source[i]);
					outline.open();
					FloatControl volumeControl = (FloatControl) outline
							.getControl(FloatControl.Type.VOLUME);
					System.out.println("       volume: "
							+ volumeControl.getValue());
					float v = Float.parseFloat(vol);
					System.out.println("volume from tdc "+v);
					v=v/100;
					volumeControl.setValue(v);
					System.out.println("   new volume: "
							+ volumeControl.getValue());
				} catch (LineUnavailableException ex) {
					System.err.println("source not supported");
					ex.printStackTrace();
				}
			}
			i++;
		}
	}
*/
}


 class Audio {

	public static void main(String[] args) throws Exception {
		System.out.println(getHierarchyInfo());
		System.out.println(getMasterOutputVolume());
	}

	public static void setMasterOutputVolume(float value) {
		if (value < 0 || value > 1)
			throw new IllegalArgumentException(
					"Volume can only be set to a value from 0 to 1. Given value is illegal: " + value);
		Line line = getMasterOutputLine();
		if (line == null) throw new RuntimeException("Master output port not found");
		boolean opened = open(line);
		try {
			FloatControl control = getVolumeControl(line);
			if (control == null)
				throw new RuntimeException("Volume control not found in master port: " + toString(line));
			control.setValue(value);
		} finally {
			if (opened) line.close();
		}
	}

	public static Float getMasterOutputVolume() {
		Line line = getMasterOutputLine();
		if (line == null) return null;
		boolean opened = open(line);
		try {
			FloatControl control = getVolumeControl(line);
			if (control == null) return null;
			return control.getValue();
		} finally {
			if (opened) line.close();
		}
	}

	public static void setMasterOutputMute(boolean value) {
		Line line = getMasterOutputLine();
		if (line == null) throw new RuntimeException("Master output port not found");
		boolean opened = open(line);
		try {
			BooleanControl control = getMuteControl(line);
			if (control == null)
				throw new RuntimeException("Mute control not found in master port: " + toString(line));
			control.setValue(value);
		} finally {
			if (opened) line.close();
		}
	}

	public static Boolean getMasterOutputMute() {
		Line line = getMasterOutputLine();
		if (line == null) return null;
		boolean opened = open(line);
		try {
			BooleanControl control = getMuteControl(line);
			if (control == null) return null;
			return control.getValue();
		} finally {
			if (opened) line.close();
		}
	}

	public static Line getMasterOutputLine() {
		System.out.println("mixers : "+getMixers().size());
		for (Mixer mixer : getMixers()) {
			System.out.println(getAvailableOutputLines(mixer).size()+" available output lines in "+mixer.toString());
			for (Line line : getAvailableOutputLines(mixer)) {
				System.out.println("Line :"+line.getLineInfo().toString());
				if (line.getLineInfo().toString().contains("Master") || line.getLineInfo().toString().contains("SPEAKER")) return line;
			}
		}
		return null;
	}

	public static FloatControl getVolumeControl(Line line) {
		if (!line.isOpen()) throw new RuntimeException("Line is closed: " + toString(line));
		return (FloatControl) findControl(FloatControl.Type.VOLUME, line.getControls());
	}

	public static BooleanControl getMuteControl(Line line) {
		if (!line.isOpen()) throw new RuntimeException("Line is closed: " + toString(line));
		return (BooleanControl) findControl(BooleanControl.Type.MUTE, line.getControls());
	}

	private static Control findControl(Type type, Control... controls) {
		if (controls == null || controls.length == 0) return null;
		for (Control control : controls) {
			if (control.getType().equals(type)) return control;
			if (control instanceof CompoundControl) {
				CompoundControl compoundControl = (CompoundControl) control;
				Control member = findControl(type, compoundControl.getMemberControls());
				if (member != null) return member;
			}
		}
		return null;
	}

	public static List<Mixer> getMixers() {
		Info[] infos = AudioSystem.getMixerInfo();
		List<Mixer> mixers = new ArrayList<Mixer>(infos.length);
		for (Info info : infos) {
			Mixer mixer = AudioSystem.getMixer(info);
			mixers.add(mixer);
		}
		return mixers;
	}

	public static List<Line> getAvailableOutputLines(Mixer mixer) {
		return getAvailableLines(mixer, mixer.getTargetLineInfo());
	}

	public static List<Line> getAvailableInputLines(Mixer mixer) {
		return getAvailableLines(mixer, mixer.getSourceLineInfo());
	}

	private static List<Line> getAvailableLines(Mixer mixer, Line.Info[] lineInfos) {
		List<Line> lines = new ArrayList<Line>(lineInfos.length);
		for (Line.Info lineInfo : lineInfos) {
			Line line;
			line = getLineIfAvailable(mixer, lineInfo);
			if (line != null) lines.add(line);
		}
		return lines;
	}

	public static Line getLineIfAvailable(Mixer mixer, Line.Info lineInfo) {
		try {
			return mixer.getLine(lineInfo);
		} catch (LineUnavailableException ex) {
			return null;
		}
	}

	public static String getHierarchyInfo() {
		StringBuilder sb = new StringBuilder();
		for (Mixer mixer : getMixers()) {
			sb.append("Mixer: ").append(toString(mixer)).append("\n");

			for (Line line : getAvailableOutputLines(mixer)) {
				sb.append("  OUT: ").append(toString(line)).append("\n");
				boolean opened = open(line);
				for (Control control : line.getControls()) {
					sb.append("    Control: ").append(toString(control)).append("\n");
					if (control instanceof CompoundControl) {
						CompoundControl compoundControl = (CompoundControl) control;
						for (Control subControl : compoundControl.getMemberControls()) {
							sb.append("      Sub-Control: ").append(toString(subControl)).append("\n");
						}
					}
				}
				if (opened) line.close();
			}

			for (Line line : getAvailableOutputLines(mixer)) {
				sb.append("  IN: ").append(toString(line)).append("\n");
				boolean opened = open(line);
				for (Control control : line.getControls()) {
					sb.append("    Control: ").append(toString(control)).append("\n");
					if (control instanceof CompoundControl) {
						CompoundControl compoundControl = (CompoundControl) control;
						for (Control subControl : compoundControl.getMemberControls()) {
							sb.append("      Sub-Control: ").append(toString(subControl)).append("\n");
						}
					}
				}
				if (opened) line.close();
			}

			sb.append("\n");
		}
		return sb.toString();
	}

	public static boolean open(Line line) {
		if (line.isOpen()) return false;
		try {
			line.open();
		} catch (LineUnavailableException ex) {
			return false;
		}
		return true;
	}

	public static String toString(Control control) {
		if (control == null) return null;
		return control.toString() + " (" + control.getType().toString() + ")";
	}

	public static String toString(Line line) {
		if (line == null) return null;
		Line.Info info = line.getLineInfo();
		return info.toString();// + " (" + line.getClass().getSimpleName() + ")";
	}

	public static String toString(Mixer mixer) {
		if (mixer == null) return null;
		StringBuilder sb = new StringBuilder();
		Info info = mixer.getMixerInfo();
		sb.append(info.getName());
		sb.append(" (").append(info.getDescription()).append(")");
		sb.append(mixer.isOpen() ? " [open]" : " [closed]");
		return sb.toString();
	}

}
