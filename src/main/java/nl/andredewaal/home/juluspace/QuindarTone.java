/**
 * 
 */
package nl.andredewaal.home.juluspace;

import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author awaal
 *
 */
public class QuindarTone {

	/**
	 * @param myMixer
	 */
	protected QuindarTone() {
		
		//This is here to ensure compatibility with the Hifiberry hat
		for (Mixer.Info currentMixer : AudioSystem.getMixerInfo())
		{
			if (currentMixer.getName().contains("plughw")) this.myMixer=currentMixer;
		}
	}

	private float introFreq = 2525;
	private float outroFreq = 2475;
	@SuppressWarnings("unused")
	private long broadcastStatus = 0;
	private static Logger log = LogManager.getLogger(QuindarTone.class);

	/*
	 * OscillatorPlayer.java
	 *
	 * This file is part of jsresources.org
	 */

	/*
	 * Copyright (c) 1999 -2001 by Matthias Pfisterer All rights reserved.
	 *
	 * Redistribution and use in source and binary forms, with or without
	 * modification, are permitted provided that the following conditions are met:
	 *
	 * - Redistributions of source code must retain the above copyright notice, this
	 * list of conditions and the following disclaimer. - Redistributions in binary
	 * form must reproduce the above copyright notice, this list of conditions and
	 * the following disclaimer in the documentation and/or other materials provided
	 * with the distribution.
	 *
	 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
	 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
	 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
	 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
	 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
	 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
	 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
	 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
	 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
	 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
	 * POSSIBILITY OF SUCH DAMAGE.
	 */

	private static final int BUFFER_SIZE = 22000;
	private static boolean DEBUG = false;
	private Mixer.Info myMixer = null;

	private byte[] oscData;
	AudioFormat audioFormat = null;
	int nWaveformType = Oscillator.WAVEFORM_SINE;
	float fSampleRate = 44100.0F;
	float fAmplitude = 0.2F;
	long playTime = 250;
	
	public static  void main(String[] args) {
		QuindarTone myQT = new QuindarTone();
		Mixer.Info[] installedMixers = AudioSystem.getMixerInfo();
        for(int n = 0; n < installedMixers.length; n++) 
        {
            log.info("[" + n + "] " + installedMixers[n].toString());
        }
        while(myQT.myMixer == null) {
            int choice;
            try {
                log.info("Choose a mixer: ");
                Scanner s = new Scanner(System.in);
                choice = s.nextInt();
                if(choice >= 0 && choice < installedMixers.length)
                    myQT.myMixer = AudioSystem.getMixer(installedMixers[choice]).getMixerInfo();
                else
                    log.error("Invalid Choice!");
                s.close();
            } catch(RuntimeException e) {
                log.error("Please input an integer corresponding to your mixer choice.");
            }
        }
		
		
		myQT.intro();
		myQT.outro();
		myQT.intro();
		myQT.outro();
		
		
	}

	public void intro() {
		long current = System.currentTimeMillis();
		audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fSampleRate, 16, 2, 4, fSampleRate, false);
		AudioInputStream oscillator = new Oscillator(nWaveformType, introFreq, fAmplitude, audioFormat,
				AudioSystem.NOT_SPECIFIED);
		play(oscillator);
		log.debug("INTRO playing lasted: " + (System.currentTimeMillis() - current));

	}

	public void outro() {
		long current = System.currentTimeMillis();
		audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, fSampleRate, 16, 2, 4, fSampleRate, false);
		AudioInputStream oscillator = new Oscillator(nWaveformType, outroFreq, fAmplitude, audioFormat,
				AudioSystem.NOT_SPECIFIED);
		play(oscillator);
		log.debug("OUTRO playing lasted: " + (System.currentTimeMillis() - current));
	}

	private void play(AudioInputStream osc) {
		play(osc, playTime);
	}

	private void play(AudioInputStream osc, long playTimems) {
		log.debug("QuindarTone play start");

		float frameLength = (fSampleRate * 0.6f);
		SourceDataLine line = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
    	if (DEBUG) {
			for (AudioFormat af : info.getFormats())
				log.debug("Audioformat supported: " + af.toString());
		}
    	try {
			line = AudioSystem.getSourceDataLine(audioFormat, myMixer);
			line.open(audioFormat);
		} catch (LineUnavailableException e) {
			log.error("Line unavailable: " + e.getLocalizedMessage());
		} catch (Exception e) {
			log.error("Generic error: " + e.getClass().getName() + e.getLocalizedMessage());
		}
		line.start();

		oscData = new byte[BUFFER_SIZE];
		int totalWritten = 0;
		while (totalWritten < frameLength) {
			// log.debug("OscillatorPlayer.main(): trying to read (bytes): " +
			// oscData.length);
			int nRead = 0;
			try {
				nRead = osc.read(oscData, 0, oscData.length);
			} catch (IOException e) {
				log.error(e.getLocalizedMessage());
			}
			// if ((totalWritten + nRead) > frameLength) break;
			// log.debug("OscillatorPlayer.main(): in loop, read (bytes): " + nRead);
			totalWritten += line.write(oscData, 0, nRead);
			if (DEBUG) {
				log.debug("OscillatorPlayer.main(): written: " + totalWritten);
			}
		}
		try {
			line.drain();
			line.stop();
			line.close();
			osc.close();
			oscData = null;
			// line.flush();
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}

	}
}

/*** OscillatorPlayer.java ***/
