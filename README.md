# Trello API Test Suite

[![forthebadge](http://forthebadge.com/images/badges/powered-by-electricity.svg)](http://forthebadge.com)

This project provides automated API tests for Trello using Java, Maven, and TestNG.

## Features

- Automated tests for Trello boards, cards, and checklists
- TestNG-based test suite configuration
- Modular service classes for Trello API operations

## Project Structure

- `src/main/java/` - Source code for API services and utilities
- `src/test/java/` - Test classes (e.g., `BoardSetupTest`, `CardWorkflowTest`, `ChecklistTest`)
- `src/main/resources/trelloTestSuit.xml` - TestNG suite configuration

## Prerequisites

- Java 8 or higher
- Maven 3.x
- Trello API key and token

## Setup

1. Clone the repository.
2. Configure your Trello API credentials in the appropriate config file or environment variables.

## Running Tests

To run all tests using Maven and TestNG:

```sh
mvn clean test
Built With:
  TestNG - The automation test framework &
  Maven - Dependency Management
