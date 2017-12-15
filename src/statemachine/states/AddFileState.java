package statemachine.states;

import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import javafx.stage.FileChooser;
import statemachine.core.StateMachine;
import statemachine.utils.StateNames;

public class AddFileState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	
	public AddFileState(StateMachine stateMachine, SceneContainerStage sceneContainerStage) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
	}

	@Override
	public void execute() {
		chooseFile();
	}
	
	private void chooseFile() {		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(GUIText.SELECT_FILE);
		fileChooser.showOpenDialog(sceneContainerStage);
		
		// TODO
		// Static object wraps file
		// Loading bar appears (Possibly new scene)
		
		stateMachine.setCurrentState(StateNames.DASHBOARD.toString());
	}
}
