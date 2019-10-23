package controllers;

public class Credits extends Controller {
    private String _previousfxmlpath = "/fxml/MainMenu.fxml";

    @Override
    public String returnFXMLPath() {
        return _previousfxmlpath;
    }

    @Override
    public String returnForwardFXMLPath() {
        return null;
    }
}
