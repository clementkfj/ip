import duke.*;
import duke.command.Command;

/**
 * The main class where the program starts running in.
 */
public class Duke {
    private Storage storage;
    private Ui ui;
    private TaskList tasks;

    /**
     * Initialises a Duke program to start chatting with the user.
     *
     * @param filePath The file to store the data that users input.
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (DukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * The program will begin when this method is called.
     */
    public void run() {
        ui.welcomeMessage();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                ui.lineProducer();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.lineProducer();
            }
        }
    }

    public static void main(String[] args) {
        new Duke("data/duke.txt").run();
    }
}
