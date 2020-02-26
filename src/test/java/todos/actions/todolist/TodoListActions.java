package todos.actions.todolist;

import net.serenitybdd.core.pages.WebElementFacade;
import net.serenitybdd.core.steps.UIInteractionSteps;

import java.util.List;
import java.util.stream.Collectors;

import static todos.actions.todolist.TodoList.*;

public class TodoListActions extends UIInteractionSteps {
    public List<String> currentItems() {
        return findAll(TODO_ITEMS)
                .stream()
                .map(WebElementFacade::getTextContent)
                .collect(Collectors.toList());
    }

    public Integer numberOfItemsLeft() {
        return Integer.parseInt($(NUMBER_OF_ITEMS_LEFT).getText());
    }

    public String numberOfItemsLeftMessage() {
        return $(NUMBER_OF_ITEMS_LEFT_MESSAGE).getText();
    }
}
