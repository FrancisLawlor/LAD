package statemachine.states;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import content.frame.core.Content;
import filemanagement.core.FileConstants;
import filemanagement.fileretrieval.MediaFileSaver;
import filemanagement.fileretrieval.RetrievedFile;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.concurrent.Task;
import peer.data.messages.LoadedContent;
import peer.frame.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class ViewingFilesState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private ViewerToUIChannel viewer;
	
	public ViewingFilesState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, ViewerToUIChannel viewer) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.viewer = viewer;
	}

	@Override
	public void execute(StateName param) {
		sceneContainerStage.changeScene(gui.getMyFilesScene());
		sceneContainerStage.setTitle(GUIText.MY_FILES);
				
		switch (param) {
			case CLICK_BACK:
				clicksBack();
				break;
			case CLICK_FILE:
				clicksFile();
				break;
			default:
				break;
			}
	}

	private void clicksBack() {
		stateMachine.setCurrentState(StateName.DASHBOARD.toString());
		stateMachine.execute(StateName.INIT);
	}
	
	private void clicksFile() {
		RetrievedFile retrievedFile = new RetrievedFile();

		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
			    LoadedContent loadedContent;
			    Content content = gui.getMyFilesScene().getFilesListView().getSelectionModel().getSelectedItem();
			    viewer.requestRetrievalOfSavedContentFile(content);
			    loadedContent = viewer.getLoadedContent();
			    viewer.createNewContentView(loadedContent.getContent());
			    String fileName = loadedContent.getContent().getFileName();
			    String fileFormat = loadedContent.getContent().getFileFormat();
			    File file = MediaFileSaver.getFile(fileName, fileFormat);
			    retrievedFile.setFile(file);
				return null;
			}
		};
		task.setOnSucceeded(e -> {
			try {
				openFile(retrievedFile);
				rateFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		new Thread(task).start();
	}
	
	private void openFile(RetrievedFile retrievedFile) throws IOException {
	    if (Desktop.isDesktopSupported()) {
            if (retrievedFile.getFile().exists()) {
                new Thread(() -> {
                    try {
                        Desktop desktop = Desktop.getDesktop();
                        desktop.open(retrievedFile.getFile()); 
                    }
                    catch (IOException e) { }
                }).start();
            }
	    }
	    else {
			System.err.println(FileConstants.DESKTOP_NOT_SUPPORTED);
			return;
		}
	}
	
	private void rateFile() {
		stateMachine.setCurrentState(StateName.RATING.toString());
		stateMachine.execute(StateName.INIT);
	}

}
