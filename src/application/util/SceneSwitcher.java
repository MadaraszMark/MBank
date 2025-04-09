package application.util;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneSwitcher {
	
	public static void switchSceneWithFade(Node currentNode, String fxmlPath, String windowTitle) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), currentNode);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
                Parent newRoot = loader.load();

                Stage stage = (Stage) currentNode.getScene().getWindow();
                Scene newScene = new Scene(newRoot);
                
                stage.setTitle(windowTitle);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), newRoot);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

                stage.setScene(newScene);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        fadeOut.play();
    }

}
