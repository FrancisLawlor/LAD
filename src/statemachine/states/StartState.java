package statemachine.states;

import java.io.File;

import content.frame.core.Content;
import filemanagement.core.FileConstants;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import statemachine.core.StateMachine;
import statemachine.eventhandlers.ListViewEventHandler;
import statemachine.eventhandlers.StateMachineEventHandler;
import statemachine.utils.StateName;

public class StartState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	
	public StartState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		
		configureButtons();
		
		sceneContainerStage.setOnHiding(event -> {
			System.exit(0);
		});
		
		gui.getMyFilesScene().getFilesListView().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Content>() {
			@Override
			public void changed(ObservableValue<? extends Content> observable, Content oldValue, Content newValue) {  
				if (newValue != null) {
					stateMachine.execute(StateName.CLICK_FILE);
				}
		    }
		});
	}

	private void configureButtons() {
		gui.getDashBoardScene().getMyFilesButton().setOnAction(new StateMachineEventHandler(StateName.VIEWING_FILES, stateMachine));
		gui.getDashBoardScene().getRefreshButton().setOnAction(new StateMachineEventHandler(StateName.REFRESH, stateMachine));
		gui.getDashBoardScene().getAddFileButton().setOnAction(new StateMachineEventHandler(StateName.ADD_FILE, stateMachine));
		gui.getDashBoardScene().getListView().setOnMousePressed(new ListViewEventHandler(StateName.RETRIEVE_FILE_QUERY, stateMachine));
		gui.getFileRetrievalQueryScene().getNoButton().setOnAction(new StateMachineEventHandler(StateName.CLICK_NO, stateMachine));
		gui.getFileRetrievalQueryScene().getYesButton().setOnAction(new StateMachineEventHandler(StateName.CLICK_YES, stateMachine));
		gui.getMyFilesScene().getBackButton().setOnAction(new StateMachineEventHandler(StateName.CLICK_BACK, stateMachine));
		gui.getMyFilesScene().getFilesListView().setOnMousePressed(new ListViewEventHandler(StateName.CLICK_FILE, stateMachine));
		gui.getRatingScene().getBackButton().setOnAction(new StateMachineEventHandler(StateName.CLICK_BACK, stateMachine));
		gui.getRatingScene().getSubmitButton().setOnAction(new StateMachineEventHandler(StateName.CLICK_SUBMIT, stateMachine));
		gui.getSetupScene().getNextButton().setOnAction(new StateMachineEventHandler(StateName.CLICK_SUBMIT, stateMachine));
		gui.getAddFileScene().getSubmitButton().setOnAction(new StateMachineEventHandler(StateName.CLICK_SUBMIT, stateMachine));
	}

	@Override
	public void execute(StateName param) {
		if (!configFileExists()) {
			stateMachine.setCurrentState(StateName.SETUP.toString());
			sceneContainerStage.setTitle(GUIText.SETUP);
			stateMachine.execute(StateName.INIT);
			sceneContainerStage.show();
		} else {
			stateMachine.setCurrentState(StateName.RETRIEVE_RECOMMENDATIONS.toString());
			sceneContainerStage.setTitle(GUIText.SETUP);
			stateMachine.execute(StateName.INIT);
			sceneContainerStage.show();
		}
	}
	
	private boolean configFileExists() {
		return new File(FileConstants.CONFIG_FILE_NAME).exists();
	}

}
