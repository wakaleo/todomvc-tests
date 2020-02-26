package todos.actions.navigate;

import net.serenitybdd.core.steps.UIInteractionSteps;
import net.thucydides.core.annotations.Step;

public class NavigateActions extends UIInteractionSteps {

    TodoMvcHomePage homePage;

    @Step("Navigate to the TodoMVC application")
    public void toTheTodoMVCApplication() {
        homePage.open();
    }
}
