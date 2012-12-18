package experiments.FX;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.animation.RotateTransitionBuilder;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author arnaud nouard
 */
public class SwingNodeController implements Initializable {

    @FXML
    private TreeView<String> treeNode;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Button btnTrick;
    @FXML
    private AnchorPane anchorPane;
    
    SwingNode swingNode;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TreeItem<String> treeRoot = new TreeItem<>("FX Tree");
        treeRoot.getChildren().addAll(Arrays.asList(
                new TreeItem<>("This is"),
                new TreeItem<>("a JFX tree"),
                new TreeItem<>("Node on the left")));

        treeNode.setRoot(treeRoot);
        treeNode.setShowRoot(true);
        treeNode.setRoot(treeRoot);
        treeRoot.setExpanded(true);
        treeNode.setOnDragOver(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent t) {
                t.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                t.consume();
            }
        });
        treeNode.setOnDragDropped(new EventHandler<DragEvent>() {

            @Override
            public void handle(DragEvent t) {
                String string = t.getDragboard().getString();
                TreeItem<String> root = treeNode.getRoot();
                root.getChildren().add(new TreeItem<>(string));
            }
        });
        btnTrick.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                swingNode.removeAllListeners();
                
                RotateTransition rotateTransition = RotateTransitionBuilder.create()
                    .node(anchorPane)
                    .duration(Duration.seconds(40))
                    .fromAngle(0)
                    .toAngle(360)
                    .cycleCount(Timeline.INDEFINITE)
                    .autoReverse(true)
                    .build();
                rotateTransition.play();
            }
        });
    }

    public void setSwingNode(SwingNode node) {
        swingNode=node;
        splitPane.getItems().add(node);
    }
}
