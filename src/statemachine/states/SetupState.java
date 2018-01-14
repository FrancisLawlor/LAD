package statemachine.states;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

import filemanagement.core.FileConstants;
import gui.core.GUI;
import gui.core.SceneContainerStage;
import gui.utilities.GUIText;
import peer.frame.core.PeerToPeerActorSystem;
import peer.frame.core.UniversalId;
import statemachine.core.StateMachine;
import statemachine.utils.StateName;

public class SetupState extends State {
	private StateMachine stateMachine;
	private SceneContainerStage sceneContainerStage;
	private GUI gui;
	private PeerToPeerActorSystem p2pActorSystem;
	
	public SetupState(StateMachine stateMachine, SceneContainerStage sceneContainerStage, GUI gui, PeerToPeerActorSystem p2pActorSystem) {
		this.stateMachine = stateMachine;
		this.sceneContainerStage = sceneContainerStage;
		this.gui = gui;
		this.p2pActorSystem = p2pActorSystem;
	}

	@Override
	public void execute(StateName param) {
		switch (param) {
			case INIT:
				initialise();
				break;
			case CLICK_SUBMIT:
				try {
					clicksSubmit();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception ex) {
				    ex.printStackTrace();
				}
				break;
			default:
				break;
		}
	}
	
	private void clicksSubmit() throws Exception {
		String portNumber = gui.getSetupScene().getPortNumberTextField().getText();
		
		if (portIsAvailable(portNumber)) {
			try {
				createConfigFile();
				createFilesDirectory();
				createActorSystem(portNumber);
				storeEncryptionKey();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
			writePortNumberToConfigFile(portNumber);
			
			stateMachine.setCurrentState(StateName.RETRIEVE_RECOMMENDATIONS.toString());
			stateMachine.execute(StateName.INIT);
		} else {
			gui.getSetupScene().getErrorLabel().setText(GUIText.PORT_UNAVAILABLE);
		}
	}

	private void storeEncryptionKey() throws NoSuchAlgorithmException, IOException {
		SecretKey secretKey = KeyGenerator.getInstance("DES").generateKey();
		String encodedKey = Base64.encodeBase64String(secretKey.getEncoded());
		
		FileWriter configFile = new FileWriter(FileConstants.CONFIG_FILE_NAME, true);
		
		Properties props = new Properties();
		props.setProperty(FileConstants.ENCRYPTION_KEY, encodedKey);
		props.store(configFile, FileConstants.ADDED_ENCRYPTION_KEY);
		configFile.close();
	}

	private void createConfigFile() throws IOException, URISyntaxException {
		FileWriter configFile = new FileWriter(FileConstants.CONFIG_FILE_NAME, true);
		String filesPath = new File("").getAbsolutePath();
		
		Properties props = new Properties();
		props.setProperty(FileConstants.DIRECTORY_KEY, filesPath + "/" + FileConstants.FILES_DIRECTORY_NAME);
		props.store(configFile, FileConstants.INITIALISATION_COMMENT);
		configFile.close();
	}
	
	private void createFilesDirectory() throws IOException {
		FileReader configFile = new FileReader(FileConstants.CONFIG_FILE_NAME);
		
		Properties props = new Properties();
		props.load(configFile);
		
		File fileDirectory = new File(props.getProperty(FileConstants.DIRECTORY_KEY));
		
		if (fileDirectory.mkdir()) {
			System.out.println(FileConstants.CREATED_FILE_DIRECTORY);
		} else {
			System.out.println(FileConstants.FAILED_TO_CREATE_FILES_DIRECTORY);
		}
	}
	
	private void createActorSystem(String portNumber) throws Exception {
	    UniversalId peerId = new UniversalId("localhost:" + portNumber);
	    this.p2pActorSystem.createActors(peerId);
	    Thread.sleep(2000);
	}
	
	private void writePortNumberToConfigFile(String portNumber) throws IOException {
		FileWriter configFile = new FileWriter(FileConstants.CONFIG_FILE_NAME, true);
		
		Properties props = new Properties();
		props.setProperty(FileConstants.PORT_NUMBER, portNumber);
		props.store(configFile, FileConstants.ADDED_PORTNUMBER);
		configFile.close();
	}
	
	private boolean portIsAvailable(String portNumber) {		
		try (Socket ignored = new Socket("localhost", Integer.parseInt(portNumber))) {
	        return false;
	    } catch (IOException ignored) {
	        return true;
	    }
	}
	
	private void initialise() {
		sceneContainerStage.changeScene(gui.getSetupScene());
		sceneContainerStage.setTitle(GUIText.SETUP);
	}
}
