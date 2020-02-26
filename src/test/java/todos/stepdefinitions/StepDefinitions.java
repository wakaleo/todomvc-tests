package todos.stepdefinitions;

import cucumber.api.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import net.thucydides.core.annotations.Steps;
import todos.actions.addtodo.AddNewTodoActions;
import todos.actions.completetodos.CompleteTodoActions;
import todos.actions.filter.FilterItemsActions;
import todos.actions.layout.LayoutActions;
import todos.actions.navigate.NavigateActions;
import todos.actions.todolist.TodoListActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDefinitions {

    @Steps
    NavigateActions navigate;

    @Steps
    AddNewTodoActions addTodo;

    @Steps
    TodoListActions todoItems;

    @Steps
    LayoutActions pageLayout;

    @Given("(.*) opens the Todo Application")
    public void opens_the_Todo_Application(String actor) {
        navigate.toTheTodoMVCApplication();
    }

    @Then("he/she should see the credits in the footer")
    public void he_should_see_in_the_footer() {
        assertThat(pageLayout.footer()).contains("TodoMVC");
    }

    @Then("the page title should include {string}")
    public void thePageTitleShouldInclude(String expectedTitle) {
        assertThat(pageLayout.getTitle()).contains(expectedTitle);
    }


    @Given("(?:.*) has not entered any todo items")
    public void has_not_entered_any_todo_items() {
        navigate.toTheTodoMVCApplication();
    }

    @Given("(?:.*) has a todo list containing")
    public void has_a_list_containing(List<String> expectedItems) {
        navigate.toTheTodoMVCApplication();
        addTodo.itemsCalled(expectedItems);
    }
    @Then("the application should suggest how to add them")
    public void the_application_should_suggest_how_to_add_them() {
        assertThat(addTodo.prompt()).isEqualTo("What needs to be done?");
    }

    @When("she adds {string}")
    public void she_adds(String todoItem) {
        addTodo.itemCalled(todoItem);
    }

    @Then("his/her todo list should contain:")
    public void the_todo_list_should_contain(List<String> expectedItems) {
        assertThat(todoItems.currentItems()).containsAll(expectedItems);
    }

    @And("the remaining item count should show {string}")
    public void theRemainingItemCountShouldShow(String remainingItemCountText) {
        assertThat(todoItems.numberOfItemsLeftMessage()).isEqualTo(remainingItemCountText);
    }

    @Steps
    FilterItemsActions filterItems;

    @When("he/she filters the list to show {word} tasks")
    public void filtersBy(String filter) {
        filterItems.by(filter);
    }

    @Steps
    CompleteTodoActions completeTodo;

    @When("he/she completes {string}")
    public void sheCompletes(String completedTodo) {
        completeTodo.itemCalled(completedTodo);
    }

    @Then("the todo item called {string} should be marked as completed")
    public void theTodoItemCalledShouldBeMarkedAsCompleted(String todoItem) {
        // LESSON 6
        assertThat(completeTodo.isCompleted(todoItem)).isTrue();
    }

}
