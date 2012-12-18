/**
 * Demo purpose
 *
 */
package experiments.FX;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author arnaud nouard
 */
public class SwingNodeDemo extends Application {

    SwingNode       swingNode;
    SwingComponent  swingComponent;

    @Override
    public void start(final Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SwingNode.fxml"));

        final SwingNodeController webViewController = new SwingNodeController();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                swingComponent = new SwingComponent();
            }
        });
        swingNode = new SwingNode(stage,swingComponent);

        fxmlLoader.setController(webViewController);

        Parent parent = null;

        try {
            parent = (Parent) fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        webViewController.setSwingNode(swingNode);

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Swing component \"integration\" into JavaFX application");

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                swingNode.dispose();
                stage.close();
                Platform.exit();
            }
        });
        stage.toFront();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
