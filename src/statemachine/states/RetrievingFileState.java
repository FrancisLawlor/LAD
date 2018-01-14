package statemachine.states;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import content.recommend.core.Recommendation;
import content.retrieve.messages.RetrievedContent;
import filemanagement.core.FileConstants;
import filemanagement.fileretrieval.MediaFileSaver;
import filemanagement.fileretrieval.RetrievedFile;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.concurrent.Task;
import peer.frame.core.PeerToPeerActorSystem;
import peer.frame.core.ViewerToUIChannel;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class RetrievingFileState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private PeerToPeerActorSystem p2pActorSystem;
	private ViewerToUIChannel viewer;
	
	public RetrievingFileState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, PeerToPeerActorSystem p2pActorSystem) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.p2pActorSystem = p2pActorSystem;
	}

	@Override
	public void execute(StateName param) {
		sceneContainerStage.changeScene(gui.getFileRetrievalScene());
		sceneContainerStage.setTitle(GUIText.FILE_RETRIEVAL);
		
		switch (param) {
		    case INIT:
		        init();
		        break;
			case RETRIEVING_FILE:
				retrieveFileAndOpen();
				break;
			default:
				break;
		}
	}
	
	private void init() {
	    this.viewer = this.p2pActorSystem.getViewerChannel();
	}
	
	private void retrieveFileAndOpen() {
		RetrievedFile retrievedFile = new RetrievedFile();
		
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() throws Exception {
			    RetrievedContent retrievedContent;
			    Recommendation recommendation = gui.getDashBoardScene().getListView().getSelectionModel().getSelectedItem();
			    viewer.requestContent(recommendation);
			    retrievedContent = viewer.getRetrievedContent();
			    viewer.createNewContentView(retrievedContent.getContent());
			    String fileName = retrievedContent.getContent().getFileName();
			    String fileFormat = retrievedContent.getContent().getFileFormat();
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
