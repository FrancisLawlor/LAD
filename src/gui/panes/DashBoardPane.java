package gui.panes;

import content.Content;
import gui.utilities.GUIText;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class DashBoardPane extends BorderPane {
	private Button myFilesButton;
	private Button addFileButton;
	private Button refreshButton;
	
	public DashBoardPane(ObservableList<Content> data) {
		VBox leftBar = configureLeftBar();
		this.setLeft(leftBar);
		
		ListView<Content> listView = configureListView(data);
		this.setCenter(listView);
		
		BorderPane rightBar = configureRightBar(data);
		this.setRight(rightBar);
	}
	
	private VBox configureLeftBar() {
		VBox content = new VBox();
	    content.setPadding(new Insets(10));
	    content.setSpacing(8);
	    
	    Button myFilesButton = new Button(GUIText.MY_FILES);
	    Button addFileButton = new Button(GUIText.ADD_FILE);
	    
	    this.myFilesButton = myFilesButton;
	    this.addFileButton = addFileButton;
	    
        content.getChildren().add(myFilesButton);
        content.getChildren().add(addFileButton);
	    
	    return content;
	}
	
	private ListView<Content> configureListView(ObservableList<Content> data) {
		final ListView<Content> listView = new ListView<Content>(data);
        listView.setCellFactory(new Callback<ListView<Content>, ListCell<Content>>() {

            @Override
            public ListCell<Content> call(ListView<Content> arg) {
                return new ListCell<Content>() {

                    @Override
                    protected void updateItem(Content item, boolean bln) {
                        super.updateItem(item, bln);
                        if (item != null) {
                            VBox vBox = new VBox(new Text(item.getFileName()), new Text(item.getFileFormat()));
                            HBox hBox = new HBox(new Label("[Graphic]"), vBox);
                            hBox.setSpacing(10);
                            setGraphic(hBox);
                        }
                    }

                };
            }

        });
        
        return listView;
	}
	
	private BorderPane configureRightBar(ObservableList<Content> data) {
		BorderPane content = new BorderPane();
		content.setPadding(new Insets(10));
	    
	    Button refreshButton = new Button(GUIText.REFRESH);
	    content.setBottom(refreshButton);
	    this.refreshButton = refreshButton;
        
		return content;
	}

	public Button getMyFilesButton() {
		return myFilesButton;
	}

	public Button getAddFileButton() {
		return addFileButton;
	}

	public Button getRefreshButton() {
		return refreshButton;
	}
}
