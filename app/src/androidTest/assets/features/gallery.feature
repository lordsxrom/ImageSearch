Feature: Test something in Gallery fragment
  Here we can give a description of the tested screen, testing features, expectations, etc.

  Background: Do something before each scenario
    Given visible activity

  Scenario: Find and click on item in RecyclerView
    Then check view Gallery is visible
    And check view Recycler is visible
    When check Recycler has 30 items
    Then scroll Recycler to 10 item
    And click Recycler 10 item
    Then check view Details is visible

  Scenario Outline: Find something in search
    Then check view Gallery is visible
    And click Find
    Then write <query> in SearchField
    When check Recycler has 30 items
    Examples:
      | query  |
      | "dogs" |
      | "cows" |