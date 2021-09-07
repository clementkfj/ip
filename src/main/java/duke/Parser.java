package duke;

import duke.command.Command;
import duke.command.DeadlineAddCommand;
import duke.command.DeleteCommand;
import duke.command.DoneCommand;
import duke.command.EventAddCommand;
import duke.command.ExitCommand;
import duke.command.FindCommand;
import duke.command.HelpCommand;
import duke.command.ListCommand;
import duke.command.TodoAddCommand;


/**
 * An interpreter that evaluates the user input and produces a Command based on
 * what the user has requested for.
 */
public class Parser {
    /**
     * This method evaluates a string given to it by the program and produces a Command
     * that does whatever the user has requested for.
     *
     * @param str String that is inputted by the user when prompted and passed into this method.
     * @return A Command that makes the Ui, Storage and TaskList perform various actions.
     * @throws DukeException An error thrown due to user input error.
     */
    public static Command parse(String str) throws DukeException {
        if (str.equals("bye")) {
            return new ExitCommand();
        } else if (str.equals("list")) {
            return new ListCommand();
        } else if (str.split("\\s+")[0].equals("done")) {
            if (str.split("\\s+").length == 1) {
                throw new DukeException("Sorry please indicate a task number to update!");
            }
            assert str.split("\\s+").length > 1 : "No task indicated.";
            int doneNumber = Integer.parseInt(str.substring(5));
            return new DoneCommand(doneNumber);
        } else if (str.split("\\s+")[0].equals("deadline")) {
            if (!str.contains("/")) {
                throw new DukeException("Sorry please indicate your deadline time with a '/by' after your "
                        + "deadline title!");
            }
            int endDescription = str.indexOf("/");
            if (endDescription <= 9) {
                throw new DukeException("Oh no! Deadlines cannot be empty! Please try again");
            }
            assert str.substring(9).length() > 0 : "No deadlines given";
            String description = str.substring(9, endDescription - 1);
            if (description.equals("") || description.equals(" ")) {
                throw new DukeException("Oh no! Deadlines cannot be empty! Please try again");
            }
            String endTime = str.substring(endDescription + 4);
            return new DeadlineAddCommand(description, endTime);
        } else if (str.split("\\s+")[0].equals("event")) {
            if (!str.contains("/")) {
                throw new DukeException("Sorry please indicate a time your event begins with a '/on' after"
                        + " your event title!");
            }
            int endDescription = str.indexOf("/");
            if (endDescription < 6) {
                throw new DukeException("Oh no! Events cannot be empty! Please try again");
            }
            String description = str.substring(6, endDescription - 1);
            if (description.equals("") || description.equals(" ")) {
                throw new DukeException("Oh no! Events cannot be empty! Please try again");
            }
            String startTime = str.substring(endDescription + 4);
            return new EventAddCommand(description, startTime);
        } else if (str.split("\\s+")[0].equals("todo")) {
            if (str.length() < 5) {
                throw new DukeException("Oh no! ToDos cannot be empty! Please try again");
            }
            assert str.split("\\s+").length > 1 : "No todo specified";
            String description = str.substring(5);
            if (description.equals("") || description.equals(" ")) {
                throw new DukeException("Oh no! ToDos cannot be empty! Please try again");
            }
            return new TodoAddCommand(description);
        } else if (str.split("\\s+")[0].equals("delete")) {
            if (str.split("\\s+").length == 1) {
                throw new DukeException("Sorry please indicate a task number to delete!");
            }
            assert str.split("\\s+").length > 1 : "No task specified to delete";
            int deleteNumber = Integer.parseInt(str.substring(7));
            return new DeleteCommand(deleteNumber);
        } else if (str.split("\\s+")[0].equals("help")) {
            return new HelpCommand();
        } else if (str.split("\\s+")[0].equals("find")) {
            if (str.split("\\s+").length == 1) {
                throw new DukeException("Sorry please indicate a keyword to find!");
            }
            assert str.split("\\s+", 2).length == 2 : "No task specified to find";
            return new FindCommand(str.split("\\s+", 2)[1]);
        }
        throw new DukeException("I'm sorry :( I don't quite seem to understand, try again pls!");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Parser)) {
            return false;
        }
        return true;
    }
}
