package services;

import java.io.IOException;

public class AudioService {
	public static void playAudio(String folder, String audio) throws InterruptedException, IOException {
		ProcessBuilder preview = new ProcessBuilder("sh", "-c", "./scripts/play_audio.sh \"./audio/" + folder +"/"+ audio + "\"");
        Process process = preview.start();
        process.waitFor();
	}
	
	public static void playMusic(String music) throws InterruptedException, IOException {
		if(music.equals("None")) {
			return;
		}
		ProcessBuilder preview = new ProcessBuilder("sh", "-c", "./scripts/play_audio.sh \"./music/" + music +"\"");
        Process process = preview.start();
        process.waitFor();
	}
}
