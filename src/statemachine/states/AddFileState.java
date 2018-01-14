package statemachine.states;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import content.frame.core.Content;
import content.frame.core.ContentFile;
import content.frame.core.MediaAttributes;
import content.view.core.ContentViews;
import filemanagement.filewrapper.FileWrapper;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.stage.FileChooser;
import peer.frame.core.PeerToPeerActorSystem;
import peer.frame.core.ViewerToUIChannel;
import security.encryption.Encrypter;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class AddFileState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private File file;
	private PeerToPeerActorSystem p2pActorSystem;
	private ViewerToUIChannel viewer;
	
	public AddFileState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, PeerToPeerActorSystem p2pActorSystem) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.p2pActorSystem = p2pActorSystem;
	}

	@Override
	public void execute(StateName param) {
		switch (param) {
			case INIT:
                this.init();
				sceneContainerStage.changeScene(gui.getAddFileScene());
				sceneContainerStage.setTitle(GUIText.ADD_FILE);
				
				file = chooseFile();
				
				if (file == null) {
					stateMachine.setCurrentState(StateName.DASHBOARD.toString());
					stateMachine.execute(StateName.INIT);
				}
				break;
			case CLICK_SUBMIT:
				byte[] fileBytes = null;
				try {
					fileBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				Content content = new Content(Encrypter.hash(new String(fileBytes)), gui.getAddFileScene().getFileNameTextField().getText(), 
						gui.getAddFileScene().getFileFormatTextField().getText(), 
						Integer.parseInt(gui.getAddFileScene().getViewLengthTextField().getText()), 
							new MediaAttributes(gui.getAddFileScene().getGenreTextField().getText(), 
									gui.getAddFileScene().getYearTextField().getText(),
									gui.getAddFileScene().getCreatorTextField().getText()));
				
				ContentViews contentViewsHeader = new ContentViews(content);
				
				Gson gsonUtil = new Gson();
				
				byte[] wrappedFile = null;
				
				try {
					wrappedFile = FileWrapper.mergeHeaderDataWithMediaFile(gsonUtil.toJson(contentViewsHeader).getBytes(), file.getAbsolutePath());
				} catch (IOException e) {
					e.printStackTrace();
				}

				viewer.saveContentFile(new ContentFile(content, wrappedFile));
				
				stateMachine.setCurrentState(StateName.DASHBOARD.toString());
				stateMachine.execute(StateName.INIT);
				break;
			default:
				break;
		}
	}
	
	private void init() {
        this.viewer = this.p2pActorSystem.getViewerChannel(); 
	}
	
	private File chooseFile() {		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(GUIText.SELECT_FILE);
		File file = fileChooser.showOpenDialog(sceneContainerStage);
		
        if (file != null) {
            return file;
        }
		
        return null;		
	}
}
