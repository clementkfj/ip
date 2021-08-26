package duke.command;

import duke.DukeException;
import duke.Storage;
import duke.TaskList;
import duke.Ui;

public class ListCommand implements Command {

    @Override
    public void execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        boolean allDone;
        allDone = taskList.printItems();
        if (allDone) {
            ui.printCompleted();
        }
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
        if (!(obj instanceof ListCommand)) {
            return false;
        }
        return true;
    }
}