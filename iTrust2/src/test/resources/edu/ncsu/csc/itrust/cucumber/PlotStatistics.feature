#Author: jsu6

@tag
Feature: HCP plot passenger statistics
	As an HCP
	I want to look statistics of passengers who were infected by COVID-19
	So I can help more  
	
  @tag1
  Scenario: Layout the infected passengers list
    Given AAAA has logged in and chosen to plot passenger statistics
    Then Infected passenger list is shown


