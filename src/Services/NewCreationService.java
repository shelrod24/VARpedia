package Services;

import java.util.ArrayList;
import java.util.List;

public class NewCreationService {
	private final String _term;
	private List<String> _audioList;
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
	
	public void addAudio(String audio) {
		_audioList.add(audio);
	}
	
	public void setImageList(List<String> audioList) {
		_audioList = audioList;
	}
	
	public void addImage(String image) {
		_imageList.add(image);
	}
	
}
