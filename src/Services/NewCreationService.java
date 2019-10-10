package Services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewCreationService {
	private final String _term;
	private List<String> _audioList;
	private String _music;
	private List<String> _imageList;

	public NewCreationService (String term) {
		_term=term;
	}
	
	public String getTerm() {
		return _term;
	}
	
	public void setAudioList(List<String> audioList) {
		_audioList = audioList;
	}
	
	public List<String> getAudioList() {
		return _audioList;
	}
	
	public void setMusic(String music) {
		_music=music;
	}
	
	public String getMusic() {
		return _music;
	}
	
	public void setImageList(List<String> imageList) {
		_imageList = imageList;
	}
	
	public List<String> getImageList() {
		return _imageList;
	}
	
	public void deleteFinals() throws InterruptedException, IOException {
		ProcessBuilder builder = new ProcessBuilder("./scripts/delete_finals.sh");
		Process process = builder.start();
		process.waitFor();
	}
	
	public void combineChunks() throws IOException, InterruptedException {
		// combines list into string separated by spaces
		String audio = String.join(" ", _audioList).trim();
		System.out.println(audio);
		ProcessBuilder builder = new ProcessBuilder("./scripts/combine_chunks.sh", _term, audio);
		Process process = builder.start();
		process.waitFor();
	}
	
	public void mixAudio() throws IOException, InterruptedException {
		// mix music with audio
		System.out.println(_music);
		ProcessBuilder builder = new ProcessBuilder("./scripts/mix_audio.sh", _music);
		Process process = builder.start();
		process.waitFor();
	}
	
	public void formatImages() throws IOException, InterruptedException {
		// combines list into string separated by spaces
		String images = String.join(" ", _imageList).trim();
		System.out.println(images);
		ProcessBuilder builder = new ProcessBuilder("./scripts/format_images.sh", images);
		Process process = builder.start();
		process.waitFor();
	}
	
	public void makeVideo() throws IOException, InterruptedException {
		System.out.println(_term);
		ProcessBuilder builder = new ProcessBuilder("./scripts/make_video.sh", _term);
		Process process = builder.start();
		process.waitFor();
	}
	
	public void makeCreation(String filename) throws IOException, InterruptedException {
		System.out.println(filename);
		ProcessBuilder builder = new ProcessBuilder("./scripts/make_creation.sh", filename);
		Process process = builder.start();
		process.waitFor();
	}
	
}
