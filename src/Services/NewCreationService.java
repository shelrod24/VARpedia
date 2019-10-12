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
		ProcessBuilder builder = new ProcessBuilder("./scripts/combine_chunks.sh", _term, audio);
		Process process = builder.start();
		process.waitFor();
	}
	
	public void mixAudio() throws IOException, InterruptedException {
		// mix music with audio
		// will also mix music with redacted audio
		ProcessBuilder builder = new ProcessBuilder("./scripts/mix_audio.sh", _music);
		Process process = builder.start();
		process.waitFor();
	}
	
	public void formatImages() throws IOException, InterruptedException {
		// combines list into string separated by spaces
		String images = String.join(" ", _imageList).trim();
		ProcessBuilder builder = new ProcessBuilder("./scripts/format_images.sh", images);
		Process process = builder.start();
		process.waitFor();
	}
	
	public void makeVideos() throws IOException, InterruptedException {
		// will make both question and creation videos
		ProcessBuilder builder = new ProcessBuilder("./scripts/make_videos.sh", _term);
		Process process = builder.start();
		process.waitFor();
	}
	
	
	public void makeCreation(String filename) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder("./scripts/make_creation.sh", filename);
		Process process = builder.start();
		process.waitFor();
	}
	
	public void makeQuestion(String filename) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder("./scripts/make_question.sh", filename, _term);
		Process process = builder.start();
		process.waitFor();
	}
	
}
