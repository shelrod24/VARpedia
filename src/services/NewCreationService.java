package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewCreationService {
	private final String _term;
	private List<String> _audioList;
	private String _music;
	private List<String> _imageList;

	/**
	 * consrtuctor that sets the current term of the creation
	 * @param term the current term of the creation
	 */
	public NewCreationService (String term) {
		_term=term;
	}
	
	public String getTerm() {
		return _term;
	}
	
	/**
	 * Sets the audio list of the creation
	 * @param audioList the list of audio to be used in the creation
	 */
	public void setAudioList(List<String> audioList) {
		_audioList = audioList;
	}
	
	public List<String> getAudioList() {
		return _audioList;
	}
	
	/**
	 * Sets the music to be used in the creation
	 * @param music to be used in the creation
	 */
	public void setMusic(String music) {
		_music=music;
	}
	
	public String getMusic() {
		return _music;
	}
	
	/**
	 * Sets the images to be used in the creation
	 * @param imageList the list of images to be displayed in the creation, in the order to be displayed
	 */
	public void setImageList(List<String> imageList) {
		_imageList = imageList;
	}
	
	public List<String> getImageList() {
		return _imageList;
	}
	
	/**
	 * Deletes all previous files in the finals directory
	 */
	public void deleteFinals() throws InterruptedException, IOException {
		ProcessBuilder builder = new ProcessBuilder("./scripts/delete_finals.sh");
		Process process = builder.start();
		process.waitFor();
	}
	
	/**
	 * Combines all audio files listed in _audioList into a single audio file
	 */
	public void combineChunks() throws IOException, InterruptedException {
		// combines list into string separated by spaces
		String audio = String.join(" ", _audioList).trim();
		ProcessBuilder builder = new ProcessBuilder("./scripts/combine_chunks.sh", _term, audio);
		Process process = builder.start();
		process.waitFor();
	}
	
	/**
	 * Combines the combined audio file beforehand with the music specified in _music
	 */
	public void mixAudio() throws IOException, InterruptedException {
		// mix music with audio
		// will also mix music with redacted audio
		ProcessBuilder builder = new ProcessBuilder("./scripts/mix_audio.sh", _music);
		Process process = builder.start();
		process.waitFor();
	}
	
	/**
	 * Renames the images into the format required by ffmpeg
	 */
	public void formatImages() throws IOException, InterruptedException {
		// combines list into string separated by spaces
		String images = String.join(" ", _imageList).trim();
		ProcessBuilder builder = new ProcessBuilder("./scripts/format_images.sh", images);
		Process process = builder.start();
		process.waitFor();
	}
	
	/**
	 * Combines images in the specified format into an video
	 */
	public void makeVideos() throws IOException, InterruptedException {
		// will make both question and creation videos
		ProcessBuilder builder = new ProcessBuilder("./scripts/make_videos.sh", _term);
		Process process = builder.start();
		process.waitFor();
	}
	
	/**
	 * Combines the audio and visual components to create a creation with the specified filename
	 * @param filename the filename of the creation to be created
	 */
	public void makeCreation(String filename) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder("./scripts/make_creation.sh", filename);
		Process process = builder.start();
		process.waitFor();
	}
	
	/**
	 * Combines the audio and visual components to create a question
	 * @param filename the filename of the creation to make the filename of the question
	 */
	public void makeQuestion(String filename) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder("./scripts/make_question.sh", filename, _term);
		Process process = builder.start();
		process.waitFor();
	}
	
}
