Feature: Track a list of passengers that is infected and have contacts with others from a csv file
	As an iTrust2 HCP
	I want to upload a list of contacts
	So that I can track who has contacts with others that are infected
	
Scenario Outline: Successful sumbit a file
	Given I am logged in as a general HCP
	When I click Tracking Previous Contacts from the drop down Patient menu
	And I Upload <list> tracking list
	
	Examples:
	|                       list                         |
	| src/main/resources/passenger-contacts-new.csv      |
	
Scenario Outline: Submit a successful request
	Given I am logged in as a general HCP
	When I click Tracking Previous Contacts from the drop down Patient menu
	And I Upload <list> tracking list
	And I fill in the <ID>, <Date> and <Depth>
	Then <passenger> is found in the form
	Examples:
	|                       list                         | ID       | Date       | Depth | passenger |
	| src/main/resources/passenger-contacts-new.csv      | 3b9bba26 | 02/05/2020 | 2     | 3b9b7416  |
	
	
Scenario Outline: Submit a non-existent passenger
	Given I am logged in as a general HCP
	When I click Tracking Previous Contacts from the drop down Patient menu
	And I Upload <list> tracking list
	And I fill in the <ID>, <Date> and <Depth>
	Then no contact message is shown
	Examples:
	|                       list                         | ID       | Date       | Depth |
	| src/main/resources/passenger-contacts-new.csv      | 3b900000 | 02/05/2020 | 2     |
