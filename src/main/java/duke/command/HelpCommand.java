package duke.command;

import duke.DukeException;
import duke.Storage;
import duke.TaskList;
import duke.Ui;

public class HelpCommand implements Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        ui.helpMessage();
    }

    @Override
    public boolean isExit() {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HelpCommand)) {
            return false;
        }
        return true;
    }
}