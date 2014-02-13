Feature: Change Counter
  A change counter which can tell you how much money you have.


  Scenario: Prompt input
    Given that I am prompted for "pennies"
    When I enter "13"
    Then the prompt code returns "13"

  Scenario: Prompt output
    Given that I request a prompt of "Where is my money?"
    Then "Where is my money?" should be written to the console

  Scenario: Gets a valid number
    Given a valid input of "11"
    When prompting for "quarters"
    Then I should not get an error
    When prompting for "pennies"
    Then I should get an amount of 11
    When prompting for "dimes"
    Then I should not get the string "11"

  Scenario: Gets an invalid number
    Given and invalid input of "$5"
    Then I should get the error "Not a valid amount"

  Scenario: Counts your money in pennies
    Given that I have 2 of each coin type
    Then I should have 382 pennies

  Scenario: Converting change into dollars
    Given that I have 1134.256344 pennies
    Then I should have "$11.34" dollars

