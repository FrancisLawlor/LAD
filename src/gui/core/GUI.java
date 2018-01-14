package gui.core;

import content.frame.core.Content;
import content.recommend.core.Recommendation;
import gui.panes.AddFilePane;
import gui.panes.DashBoardPane;
import gui.panes.FileRetrievalQueryPane;
import gui.panes.MyFilesPane;
import gui.panes.RatingPane;
import gui.panes.SetupPane;
import gui.panes.WaitingPane;
import gui.scenes.AddFileScene;
import gui.scenes.DashBoardScene;
import gui.scenes.FileRetrievalQueryScene;
import gui.scenes.FileRetrievalScene;
import gui.scenes.LoadingFilesScene;
import gui.scenes.MyFilesScene;
import gui.scenes.RatingScene;
import gui.scenes.RetrieveRecommendationsScene;
import gui.scenes.SetupScene;
import gui.utilities.GUIText;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

public class GUI {
	private DashBoardScene dashBoardScene;
	private FileRetrievalQueryScene fileRetrievalQueryScene;
	private FileRetrievalScene fileRetrievalScene;
	private MyFilesScene myFilesScene;
	private RatingScene ratingScene;
	private RetrieveRecommendationsScene retrieveRecommendationsScene;
	private SetupScene setupScene;
	private AddFileScene addFileScene;
	private LoadingFilesScene loadingSavedContentScene;

	private DashBoardPane dashBoardPane;
	private FileRetrievalQueryPane fileRetrievalQueryPane;
	private WaitingPane fileRetrievalPane;
	private MyFilesPane myFilesPane;
	private RatingPane ratingPane;
	private WaitingPane retrieveRecommendationsPane;
	private SetupPane setupPane;
	private AddFilePane addFilePane;
	private WaitingPane loadingSavedContentPane;
	
	public GUI(SceneContainerStage containerStage) {
	    ObservableList<Recommendation> recommendationsData = FXCollections.observableArrayList();
	    ObservableList<Content> storedFilesData = FXCollections.observableArrayList();

		this.dashBoardPane = new DashBoardPane(recommendationsData);
		this.fileRetrievalQueryPane = new FileRetrievalQueryPane();
		this.fileRetrievalPane = new WaitingPane(GUIText.RETRIEVING_FILE);
		this.myFilesPane = new MyFilesPane(storedFilesData);
		this.ratingPane = new RatingPane();
		this.retrieveRecommendationsPane = new WaitingPane(GUIText.FINDING_RECOMMENDATIONS);
		this.setupPane = new SetupPane(containerStage);
		this.addFilePane = new AddFilePane();
		this.loadingSavedContentPane = new WaitingPane(GUIText.LOADING_SAVED_FILES);
		
		this.dashBoardScene = new DashBoardScene(this.dashBoardPane);
		this.fileRetrievalQueryScene = new FileRetrievalQueryScene(this.fileRetrievalQueryPane);
		this.fileRetrievalScene = new FileRetrievalScene(this.fileRetrievalPane);
		this.myFilesScene = new MyFilesScene(this.myFilesPane);
		this.ratingScene = new RatingScene(this.ratingPane);
		this.retrieveRecommendationsScene = new RetrieveRecommendationsScene(this.retrieveRecommendationsPane);
		this.setupScene = new SetupScene(this.setupPane);
		this.addFileScene = new AddFileScene(this.addFilePane);
		this.loadingSavedContentScene = new LoadingFilesScene(this.loadingSavedContentPane);
	}
	
	public DashBoardScene getDashBoardScene() {
		return this.dashBoardScene;
	}
	
	public FileRetrievalScene getFileRetrievalScene() {
		return this.fileRetrievalScene;
	}
	
	public FileRetrievalQueryScene getFileRetrievalQueryScene() {
		return this.fileRetrievalQueryScene;
	}

	public MyFilesScene getMyFilesScene() {
		return this.myFilesScene;
	}

	public RatingScene getRatingScene() {
		return this.ratingScene;
	}

	public RetrieveRecommendationsScene getRetrieveRecommendationsScene() {
		return this.retrieveRecommendationsScene;
	}

	public SetupScene getSetupScene() {
		return this.setupScene;
	}

	public AddFileScene getAddFileScene() {
		return this.addFileScene;
	}

	public Scene getLoadingSavedContentScene() {
		return this.loadingSavedContentScene;
	}
}
