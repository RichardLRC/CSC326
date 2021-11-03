#Author: jsu6

Feature: HCP calculates the R0 value and corresponding population behaviours
	As an HCP
	I want to see the reproduction value and population behaviours
	So I can decide to take associated actions to prevent COVID-19
	
Scenario Outline: Upload an invalid file
  Given QWER has logged in
  When She clicks Calculated R0 from the drop down Patient menu
  And She uploads <data> to the database
  Then The result shows failed

  Examples:
	|                               data                       |
	|           src/main/resources/invalid-passenger-data.csv  |
	
	
Scenario Outline: Upload a valid file
    Given QWER has logged in
    When She clicks Calculated R0 from the drop down Patient menu
    And She uploads <data> to the database
    Then She checks the result saying success
    And She checks the value of R0 and behaviours of population

  Examples:
	|                    data                          |
	|           src/main/resources/passenger-data.csv  |
