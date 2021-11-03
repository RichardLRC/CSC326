Feature: Upload a list of passengers from csv file
	As an iTrust2 HCP
	I want to upload passenger data
	So that a list of record of patients is in the system
	
Scenario Outline: Upload a valid file
	Given I am logged in as a general HCP
	When I click Upload Passengers from the drop down Patient menu
	And I Upload <list> to the system
	Then I check the result saying <uploaded> uploaded and <skipped> skipped
	
	Examples:
	|                       list                         | uploaded | skipped |
	| src/main/resources/passenger-data.csv              | 1209     | 0       |


	
Scenario Outline: Upload a duplicated file
	Given I am logged in as a general HCP
	When I click Upload Passengers from the drop down Patient menu
	And I Upload <list> to the system
	Then I check the result saying <uploaded> uploaded and <skipped> skipped
	
	Examples:
	|                       list                         | uploaded | skipped |
	| src/main/resources/passenger-data.csv              | 0        | 1209    |
	