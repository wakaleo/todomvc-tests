@ux
Feature: Site Title and Credits

  Scenario: The page title should be shown
    When Todd opens the Todo Application
    Then the page title should include "TodoMVC"

  Scenario: The application credits should appear in the footer
    When Todd opens the Todo Application
    Then he should see the credits in the footer