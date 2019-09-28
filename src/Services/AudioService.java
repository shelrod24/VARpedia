package Services;

import java.io.IOException;

public class AudioService {
	public static void playAudio(String folder, String audio) throws InterruptedException, IOException {
		ProcessBuilder preview = new ProcessBuilder("sh", "-c", "./scripts/play_audio.sh \"" + audio +"\" \""+ folder + "\"");
        Process process = preview.start();
        process.waitFor();
	}
}
