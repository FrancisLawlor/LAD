package statemachine.states;

import java.io.File;
import java.io.IOException;
import com.google.gson.Gson;
import content.frame.core.Content;
import content.frame.core.ContentFile;
import content.frame.core.MediaAttributes;
import filemanagement.filewrapper.FileWrapper;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.stage.FileChooser;
import peer.frame.core.PeerToPeerActorSystem;
import peer.frame.core.ViewerToUIChannel;
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
				Content content = new Content("todo", gui.getAddFileScene().getFileNameTextField().getText(), 
						gui.getAddFileScene().getFileFormatTextField().getText(), 
						Integer.parseInt(gui.getAddFileScene().getViewLengthTextField().getText()), 
							new MediaAttributes(gui.getAddFileScene().getGenreTextField().getText(), 
									gui.getAddFileScene().getYearTextField().getText(),
									gui.getAddFileScene().getCreatorTextField().getText()));
				
				Gson gsonUtil = new Gson();
				
				byte[] wrappedFile = null;
				
				try {
					wrappedFile = FileWrapper.mergeHeaderDataWithMediaFile(gsonUtil.toJson(content).getBytes(), file.getAbsolutePath());
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
