package duke;

import duke.command.Command;

import duke.exception.DukeException;

import java.nio.file.Paths;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

public class Duke extends Application {
    // Duke components

    /** The save/loading mechanism. */
    private Storage storage;
    /** The list of tasks. */
    private TaskList tasks;
    /** The user interface for console displays. */
    private Ui ui;

    // GUI components

    /** The scroll bar. */
    private ScrollPane scrollPane;
    /** The scrollable chat history. */
    private VBox dialogContainer;
    /** The text input bar for user input. */
    private TextField userInput;
    /** The send message button. */
    private Button sendButton;
    /** The main layout for the GUI. */
    private Scene scene;

    // Duke methods

    /**
     * Constructs a new chat-bot Duke.
     *
     * @param filePath the relative path to the save file of the chat-bot.
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load()); // Load from file if possible
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList(); // Start a brand new task list if file cannot be found/opened.
        }
    }

    /**
     * Constructs a new chat-bot Duke with the default save file location.
     */
    public Duke() {
        this(getDefaultPath());
    }

    /** Call this to begin using the chat-bot. */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        TaskList taskList = tasks;

        // Keep receiving user input until the exit command is entered
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.showLine(); // show the divider line ("_______")
                Command c = Parser.parse(fullCommand);
                taskList = c.execute(taskList, ui, storage); // Update the task list
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
    }

    @Override
    public void start(Stage stage) {
        //Step 1. Setting up required components

        //The container for the content of the chat to scroll.
        scrollPane = new ScrollPane();
        dialogContainer = new VBox();
        scrollPane.setContent(dialogContainer);

        userInput = new TextField();
        sendButton = new Button("Send");

        AnchorPane mainLayout = new AnchorPane();
        mainLayout.getChildren().addAll(scrollPane, userInput, sendButton);

        scene = new Scene(mainLayout);

        stage.setScene(scene);
        stage.show();

        //Step 2. Formatting the window to look as expected
        stage.setTitle("Duke");
        stage.setResizable(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);

        mainLayout.setPrefSize(400.0, 600.0);

        scrollPane.setPrefSize(385, 535);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        scrollPane.setVvalue(1.0);
        scrollPane.setFitToWidth(true);

        dialogContainer.setPrefHeight(Region.USE_COMPUTED_SIZE);

        userInput.setPrefWidth(325.0);

        sendButton.setPrefWidth(55.0);

        AnchorPane.setTopAnchor(scrollPane, 1.0);

        AnchorPane.setBottomAnchor(sendButton, 1.0);
        AnchorPane.setRightAnchor(sendButton, 1.0);

        AnchorPane.setLeftAnchor(userInput, 1.0);
        AnchorPane.setBottomAnchor(userInput, 1.0);

        // more code to be added here later
    }

    /** The main entry point of the program. */
    public static void main(String[] args) {
        // Save file named "tasks.txt"
        // Located in "data" folder, found in the root of this working directory.
        // Platform independent file directory
        new Duke(getDefaultPath()).run();
    }

    /**
     * Returns the os-dependent directory to the default save file location.
     * This default save file location is {folder_enclosing_duke_program}->data->tasks.txt.
     *
     * @return the os-dependent directory to the default save file location.
     */
    private static String getDefaultPath() {
        return Paths.get(System.getProperty("user.dir"), "data", "tasks.txt").toString();
    }
}