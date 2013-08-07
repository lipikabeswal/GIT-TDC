package com.ctb.tdc.web.utils;

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
			player=null;
	}

    public void setFilename(String filename) {
		this.filename=filename;
	}
    
    public void play(String filename) {
		this.filename=filename;
		play();
	}
    
    // play the MP3 file to the sound card
    public void play() {
        try {
            FileInputStream fis     = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { player.play();
                player=null;
                }
                catch (Exception e) { System.out.println(e.getMessage()+" frame "+player.getPosition()); }
            }
        }.start();
    }
    
    
    public boolean isPlaying()
    {
    	if(player == null){
    		return false;
    	}else{
    		return !player.isComplete();
    	}
    }
    
 // play the MP3 file to the sound card
    public void play(InputStream io) {
        try {
            BufferedInputStream bis = new BufferedInputStream(io);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }

        // run in new thread to play in background
        new Thread() {
            public void run() {
                try { player.play();
                player=null;
                }
                catch (Exception e) { System.out.println(e.getMessage()+" frame "+player.getPosition()); }
            }
        }.start();
    }

	public Player getPlayer() {
		return player;
	}
	
	 public void setVolume(String volume) {
		player.setVolume(volume);
	}
    

}
