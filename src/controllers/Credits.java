package controllers;

public class Credits extends Controller {
    private String _previousfxmlpath = "/fxml/MainMenu.fxml";

    @Override
    public String ReturnFXMLPath() {
        return _previousfxmlpath;
    }

    @Override
    public String ReturnForwardFXMLPath() {
        return null;
    }
}
