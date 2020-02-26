package todos.actions.filter;

import net.serenitybdd.core.steps.UIInteractionSteps;
import net.thucydides.core.annotations.Step;
import todos.actions.todolist.TodoList;

public class FilterItemsActions extends UIInteractionSteps {
    @Step("Filter items by {0}")
    public void by(String filter) {
        $(TodoList.filterButtonWithLabel(filter)).click();
    }
}
