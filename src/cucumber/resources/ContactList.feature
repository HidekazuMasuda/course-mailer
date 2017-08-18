Feature: Contact List

# address format error case
Scenario: add a wrong address
  Given ContactList address is "xxx"
  When add
  Then ContactList error_area is "error"

# duplicate address error case
Scenario: add a duplicate address
  Given ContactList address is "xxx@gmail.com"
  And ContactList has "xxx@gmail.com"
  When add
  Then ContactList error_area is "error"

# name only error case
Scenario: add a name only
  Given ContactList address is ""
  And ContactList name is "xxx"
  When add
  Then ContactList error_area is "error"

# success case
Scenario: add a address
  Given ContactList address is "xxx@gmail.com"
  And ContactList name is "xxx"
  And ContactList is empty
  When add
  Then ContactList error_area is ""
  And ContactList address is added "xxx@gmail.com"
  And ContactList name is added "xxx"
