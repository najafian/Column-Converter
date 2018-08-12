package douran;

import douran.service.ExecuteRunner;
import douran.service.OA_Model;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.ArrayList;


public class MainController {
    int currentIndex;
    int modelSize;
    Connection dbConnection;
    @FXML
    public Label progressLabel;
    @FXML
    public ProgressBar progressBar;
    @FXML
    public TextField password;
    @FXML
    public TextField username;
    @FXML
    public TextField driverType;
    @FXML
    public TextField serviceName;
    @FXML
    public TextField serverName;
    @FXML
    public TextField portNumber;
    @FXML
    public TextField databaseName;
    @FXML
    public Button execute;
    @FXML
    public Button createAndCopy;
    @FXML
    private Button checkAndConnect;

    @FXML
    public void createAndCopy(ActionEvent actionEvent) {
        dbConnection = getDbConnection();
        new Thread(() -> Platform.runLater(() -> {
            if (!ExecuteRunner.isColumnExist()) {
                boolean isCreated = ExecuteRunner.createBackupColumns();
                if (isCreated) {
                    progressLabel.setText("back up columns are created!!!");
                    progressLabel.setStyle("-fx-text-fill: #055ba8");
                    createAndCopy.setStyle("-fx-text-fill: #50a85d");
                    createAndCopy.setDisable(true);
                    execute.setDisable(false);
                } else {
                    progressLabel.setText("back up columns are failed!!!");
                    progressLabel.setStyle("-fx-text-fill: #a80d04");
                    createAndCopy.setStyle("-fx-text-fill: #a84e38");
                    createAndCopy.setDisable(false);
                }
            } else {
                execute.setDisable(false);
                progressLabel.setText("back up columns are existed!!!");
                progressLabel.setStyle("-fx-text-fill: #055ba8");
                createAndCopy.setStyle("-fx-text-fill: #50a85d");
                createAndCopy.setDisable(true);
                execute.setDisable(false);
            }
            progressBar.setProgress(1.0);
        })).start();
    }

    @FXML
    public void execute(ActionEvent actionEvent) {
        dbConnection = getDbConnection();
        new Thread(() -> {
            Platform.runLater(() -> {
                progressBar.setProgress(-1);
                progressBar.setStyle("-fx-text-fill: #00a800");
                progressLabel.setText("Utility is fetching columns, please wait!!!");
                execute.setDisable(true);
            });
            ArrayList<OA_Model> listOfAttachmentModels = ExecuteRunner.selectListOfOaLettersID(dbConnection);
            modelSize = listOfAttachmentModels.size();
            int partsize = modelSize / 100;
            for (int i = 0; partsize >= i; i++) {
                try {
                    dbConnection = getDbConnection();
                    dbConnection.setAutoCommit(false);
                    for (int j = 0; 100 > j; j++) {
                        currentIndex = ((i * 100) + j);
                        if (currentIndex < modelSize)
                            ExecuteRunner.convertModel(listOfAttachmentModels.get(currentIndex));
                    }
                    dbConnection.commit();
                    ExecuteRunner.closePrepareStatement(dbConnection, null);
                    Platform.runLater(() -> {
                        progressBar.setProgress(((double) currentIndex / (double) modelSize));
                        progressLabel.setText("Progress: " + (currentIndex + 1) + " out of " + modelSize);
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            progressLabel.setText("job is done!!!");
            progressLabel.setStyle("-fx-text-fill: #00a800");
            execute.setDisable(true);
        }).start();


    }

    private Connection getDbConnection() {
        return ExecuteRunner.getDBConnection(serverName.getText(), serviceName.getText(), driverType.getText(), Integer.parseInt(portNumber.getText()), databaseName.getText(), username.getText(), password.getText());
    }

    @FXML
    public void checkAndConnect(ActionEvent actionEvent) {
        dbConnection = getDbConnection();
        if (dbConnection == null) {
            checkAndConnect.setStyle(" -fx-text-fill: #ff6e0f");
        } else {
            createAndCopy.setDisable(false);
            checkAndConnect.setDisable(true);
            checkAndConnect.setStyle(" -fx-text-fill: #00a800");
            serverName.setDisable(true);
            serviceName.setDisable(true);
            portNumber.setDisable(true);
            username.setDisable(true);
            password.setDisable(true);
            databaseName.setDisable(true);
            driverType.setDisable(true);
            progressLabel.setText("database is connected!");
            progressLabel.setStyle("-fx-text-fill: #00a800");
            progressBar.setProgress(1);
        }
        ExecuteRunner.closePrepareStatement(dbConnection, null);

    }
}
