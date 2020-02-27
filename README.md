# Introduction to BDD Test Automation Tutorial

This tutorial will give you a basic taste of BDD-style UI test automation in Java using Cucumber. The tutorial also uses Serenity BDD,  
a library that makes it easier to write high quality automated acceptance tests, with powerful reporting and living documentation features. 

Serenity strongly encourages good test automation design, and supports several design patterns, including modern approaches such as the Action Classes pattern and the Screenplay pattern. In this tutorial you will be using the Action Classes pattern.

## Get the code

Git:

    git clone https://github.com/serenity-bdd/serenity-cucumber4-starter.git
    cd serenity-cucumber4-starter


Or simply [download a zip](https://github.com/serenity-bdd/serenity-cucumber4-starter/archive/master.zip) file.


## The project directory structure
The project has build scripts for both Maven and Gradle, and follows the standard directory structure used in most Serenity projects:
```Gherkin
src
  + main
  + test
    + java                          Test runners and supporting code
    + resources
      + features                    Feature files
      + webdriver                   Bundled webdriver binaries
        + linux
        + mac
        + windows
          chromedriver.exe          OS-specific Webdriver binaries
          geckodriver.exe
```

## The TodoMVC application

The application we will be testing is a simple Todo list applicaton implemented in AngularJS. You can find this application on the [TodoMVC website](http://todomvc.com/examples/angularjs/#/). In this tutorial we will start with an existing test framework with a number of tests, first extending these tests, then adding our own.

# The Tutorial

Before you start the tutorial, familiarise yourself with the feature files. 
You can run the tests by running the `CucumberTestSuite` class in your IDE, or by running the following command on the command line:
```
$ mvn clean verify
```
When you run the maven command, test results and living documentation will also be generated. 
You can view these in the [target/site/serenity](target/site/serenity/index.html) folder

## Lesson 1

The first Cucumber scenario we will work on can be found in the [adding_new_todos.feature](src/test/resources/features/creating_todos/adding_new_todos.feature) feature file. The scenario is a simple one:
```gherkin
  Scenario: Adding a single todo item
    Given Trudy has not entered any todo items
    When she adds "Walk the dog"
    Then her todo list should contain:
      | Walk the dog |
```

The aim of this exercise is to extend this scenario so that it checks the remaining items count displayed at the bottom of the screen. Do this by adding the following line to the end of the scenario:

```gherkin
    And the remaining item count should show "1 item left"
```

Now rerun the tests and open the [test report](target/site/serenity/3b7ffeb00e2a1e447a8d28249be9a70d0717d07672376cfd0c43ae664fd4db07.html) to see if it worked.

## Lesson 2
In this lesson you will create a new scenario that builds on the previous scenario by adding two items to the todo list. Look for the "Adding several todo items" scenario and add the following lines:

```gherkin
    Given Trudy has not entered any todo items
    When she adds "Walk the dog"
    And she adds "Feed the cat"
    Then her todo list should contain:
      | Walk the dog |
      | Feed the cat |
```

Note how this builds on and reuses the steps in the previous scenario, so no new code needs to be written. 

Now rerun the tests and open the [test report](target/site/serenity/a3d9d5dd3efda69ee00d28c0ebbfdbd97fc2014cb0eeef1997a76db0059e1ce8.html) to see if it worked.

## Lesson 3

In this lesson we will write some glue code. Find the "Adding todo items to an existing list" scenario:
```gherkin
     Given Trudy has not entered any todo items
     When she adds "Walk the dog"
     And she adds "Feed the cat"
     Then her todo list should contain:
       | Walk the dog |
       | Feed the cat |
```
 
 This scenario is incomplete: the first line has no step definition. Go to the [StepDefinitions.java](src/test/java/todos/stepdefinitions/StepDefinitions.java) class and add the following method to make Trudy open the TodoMVC application:
 
```java
    @Given("Trudy has a todo list containing")
    public void has_a_list_containing(List<String> expectedItems) {
        navigate.toTheTodoMVCApplication();
        addTodo.itemsCalled(expectedItems);
    }
```

Now rerun the tests and open the [test report](target/site/serenity/3b7ffeb00e2a1e447a8d28249be9a70d0717d07672376cfd0c43ae664fd4db07.html) to see if it worked.

## Lesson 4

In this lesson we will implement a new scenario.

```gherkin
  Scenario: User should be assisted when adding todo items for the first time
    Given Trudy has not entered any todo items
    Then the application should suggest how to add them
```

The first step is the same as in the previous scenario, but you will need to implement the second step. 
This step will check that the prompt message in the input box contains the words "What needs to be done?"
Go to the [StepDefinitions.java](src/test/java/todos/stepdefinitions/StepDefinitions.java) class and add the following method 

```java
    @Then("the application should suggest how to add them")
    public void the_application_should_suggest_how_to_add_them() {
        assertThat(addTodo.prompt()).isEqualTo("What needs to be done?");
    }
```

Now rerun the tests and open the [test report](target/site/serenity/d33fcc9784b4b0dc4d3f3e7738942da1a5604a0c3442a88acb66985cbb1e57f0.html) to see if it worked.

## Lesson 5 (Advanced)

In this lesson we take a closer look at how the automation code works under the hood. The scenario we will work on can be found in the [site_title_and_credits.feature](src/test/resources/features/layout/site_title_and_credits.feature) feature file.

The scenario is a simple one:
```gherkin
  Scenario: The application credits should appear in the footer
    When Todd opens the Todo Application
    Then he should see the credits in the footer
```

The first step has already been implemented for you. 
If you open the [StepDefinitions](src/test/java/todo/stepdefinitions/StepDefinitions.java) class, you can see how it works.

The first thing you will see in this class are two _action_ classes. Action classes model how a user behaves and what they can observe about the state of the system. In this case, the `NavigateActions` class is describes how a user navigates to various parts of the application, whereas the `LayoutAction` page class models the questions the user might have about the current system.
```java
    @Steps
    NavigateActions navigate;

    @Steps
    LayoutQuestions pageLayout;
```

Each line in the scenario we saw above maps to a special method, called a _step definition_ in this class.  The first step definition method is shown below:
```java
    @Given("(.*) opens the Todo Application")
    public void opens_the_Todo_Application(String actor) {
        navigate.toTheTodoMVCApplication();
    }
```

This method takes a single parameter which represents the name of the actor ("Trudy" in this case). In future versions of the tests we might need to cater for different actors, but for now we can ignore the `actor` parameter for now. All we need to do is to open the TodoMVC page. We do this by calling the `toTheTodoMVCApplication()` method in the `NavigateActions` class.

Let's look at the `NavigateActions` class. This class is responsible for opening the browser on the correct page, so that the user can perform the tasks they need to do. 

```java
public class NavigateActions extends UIInteractionSteps {

    @Step("Navigate to the TodoMVC application")
    public void toTheTodoMVCApplication() {
        navigateToPageNamed("angular.app");
    }
}
```

The `UIInteractionSteps` is the base class for action classes that interact with the web UI This page URL is defined in the [serenity.conf](src/test/resources/serenity.conf) configuration file:
```hocon
pages {
  angular.app = "http://todomvc.com/examples/angularjs/#/"
}
```

Now complete the implementation by adding a step definition for the last step. You can use the `pageLayout` action class, and it's built-in `getTitle()` method:

```java
    @Then("the page title should include {string}")
    public void thePageTitleShouldInclude(String expectedTitle) {
        assertThat(pageLayout.getTitle()).contains(expectedTitle);
    }
```

Now rerun the tests and open the [test report](target/site/serenity/84872811fa16ead9e62527005e46ae5a5924ca4534cc4d32f6e11d7058f0c8b4.html) to see if it worked.

## Want to learn more?
For more information about Serenity BDD, you can read the [**Serenity BDD Book**](https://serenity-bdd.github.io/theserenitybook/latest/index.html), the official online Serenity documentation source. Other sources include:
* **[Byte-sized Serenity BDD](https://www.youtube.com/channel/UCav6-dPEUiLbnu-rgpy7_bw/featured)** - tips and tricks about Serenity BDD
* [**Serenity BDD Blog**](https://johnfergusonsmart.com/category/serenity-bdd/) - regular articles about Serenity BDD



