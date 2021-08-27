package duke;

import duke.task.Deadline;
import duke.task.Event;
import duke.task.Task;
import duke.task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A list that is created when the program starts running. The list is generated
 * at first from the local memory and later on can be modified while the program is
 * running.
 */
public class TaskList {
    private ArrayList<Task> xs = new ArrayList<>();

    public TaskList(File file) throws DukeException {
        try {
            fileCopy(file);
        } catch (FileNotFoundException e) {
            throw new DukeException("Error, file cannot be found, creating new destination");
        }
    }

    public TaskList() {
        File data = new File("data/duke.txt");
        File directory = new File("data");
        try {
            if (!directory.exists()) {
                directory.mkdir();
            }
            if (!data.exists()) {
                data.createNewFile();
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public TaskList(ArrayList<Task> xs) {
        this.xs = xs;
    }

    public LocalDateTime dateFormatting(String stringDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDate = LocalDateTime.parse(stringDate, dateTimeFormatter);
        return localDate;
    }

    /**
     * A method that copies the list that is stored in the local file when the program starts running
     * so that it is stored in the list in TaskList.
     *
     * @param dukeData The file that resides in the user's local memory.
     * @throws DukeException An exception thrown as Scanner requires a FileNotFoundException to be handled.
     */
    public void fileCopy(File dukeData) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(dukeData);
        while (fileScanner.hasNextLine()) {
            String s = fileScanner.nextLine();
            if (s.equals("") || s.equals((" "))) {
                continue;
            }
            String parts[] = s.split("\\|", 5);
            if (parts[0].equals("D")) {
                Deadline dl = new Deadline(parts[2], dateFormatting(parts[3]));
                if (parts[1].equals("1")) {
                    dl.changeIsDone(true);
                }
                xs.add(dl);
            } else if (parts[0].equals("T")) {
                Todo td = new Todo(parts[2]);
                if (parts[1].equals("1")) {
                    td.changeIsDone(true);
                }
                xs.add(td);
            } else if (parts[0].equals("E")) {
                Event e = new Event(parts[2], dateFormatting(parts[3]));
                if (parts[1].equals("1")) {
                    e.changeIsDone(true);
                }
                xs.add(e);
            }
        }
        fileScanner.close();
    }

    /**
     * A method that prints a list of tasks that the user currently has in his list.
     *
     * @return A boolean that states whether all tasks in the list have been completed.
     * @throws DukeException A message thrown as there are no items that currently are in the list.
     */
    public boolean printItems() throws DukeException {
        boolean allDone = true;
        if (xs.size() == 0) {
            throw new DukeException("    There are no items in the list!");
        }
        for (int i = 0; i < xs.size(); i++) {
            System.out.println("    " + (i + 1) + ": " + xs.get(i));
            if (!xs.get(i).getIsDone() || xs.size() == 0) {
                allDone = false;
            }
        }
        return allDone;
    }

    /**
     * A method that changes the task completion status to 'done' while returning the task so Storage and
     * Ui can perform other actions with regards to the task to be changed.
     *
     * @param startOfString An integer that tells us the item number to change in the list.
     * @return The task that has been modified so that other parts can use it to change.
     * @throws DukeException A thrown exception as the number exceeds the total number of tasks or is negative.
     */
    public Task retrieveTask(int startOfString) throws DukeException {
        if (startOfString > xs.size() || startOfString < 0) {
            throw new DukeException("    Uh oh! Item " + startOfString + " does not seem to exist!");
        }
        Task taskToChange = xs.get(startOfString - 1);
        taskToChange.changeIsDone(true);
        return taskToChange;
    }

    /**
     * A method that deletes the task from the current list of tasks while returning the task so Storage and
     * Ui can perform other ractions with regards to the task to be changed.
     *
     * @param startOfString An integer that tells us the item number so we can delete it.
     * @return The task that has been deleted so other functions can be done with regards to deleting the task.
     * @throws DukeException A thrown exception as the number exceeds the total number of tasks or is negative.
     */
    public Task deleteTask(int startOfString) throws DukeException {
        if (startOfString > xs.size() || startOfString < 0) {
            throw new DukeException("    Uh oh! Item " + startOfString + " does not seem to exist!");
        }
        Task taskToDelete = xs.get(startOfString - 1);
        xs.remove(startOfString - 1);
        return taskToDelete;
    }

    /**
     * A method adds a task to the list of tasks the user currently has.
     *
     * @param type The type of task that is to be added (Deadline, Event or To do).
     * @param description The description of the task the user gave.
     * @param time The time which the task is to be completed(Deadline) or when the task will begin(Event).
     * @return An integer that tells us how many items are currently on the list.
     * @throws DukeException
     */
    public int addToList(String type, String description, String time) throws DukeException {
        try {
            LocalDateTime ld = LocalDateTime.now();
            if (!(time.equals("NA"))) {
                ld = dateFormatting(time);
            }
            if (ld.compareTo(LocalDateTime.now()) < 0 && (type.equals("D") || type.equals("E"))) {
                throw new DukeException("    Please key in a date that's not in the past!");
            }
            Task toAdd;
            if (type.equals("D")) {
                toAdd = new Deadline(description, ld);
            } else if (type.equals("E")) {
                toAdd = new Event(description, ld);
            } else {
                toAdd = new Todo(description);
            }
            xs.add(toAdd);
            return xs.size();
        } catch (DateTimeParseException e) {
            throw new DukeException("    Oh oh! Please follow the format strictly and key in a suitable date!");
        }
    }

    public void findSimilarTasks(String keyWords) {
        System.out.println("    Here are the matching tasks in your list:");
        int count = 0;
        for (Task task : xs) {
            if (task.getItemName().contains(keyWords)) {
                System.out.println("    " + task);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("    Sorry! There does not seem to be any matching tasks!");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof TaskList)) {
            return false;
        }
        TaskList other = (TaskList) obj;
        return this.xs.equals(other.xs);
    }
}
