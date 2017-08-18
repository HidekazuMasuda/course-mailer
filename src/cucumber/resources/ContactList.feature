Feature: Contact List

# address format error case
@developing
Scenario: add a wrong address
  Given ContactList address is "xxx"
  When add
  Then ContactList error_area is "error"

# duplicate address error case
@developing
Scenario: add a duplicate address
  Given ContactList address is "xxx@gmail.com"
  And ContactList has "xxx@gmail.com"
  When add
  Then ContactList error_area is "error"

# success case
Scenario: add an address
  Given ContactList address is "xxx@gmail.com"
  And ContactList is empty
  When add
  Then ContactList address is added "xxx@gmail.com"
  And ContactList name is added ""

# case for adding name
# success case for both name and email exist
Scenario: add both name and email
  Given ContactList address is "xxx@gmail.com"
  And ContactList name is "xxx"
  And ContactList is empty
  When add
  Then ContactList address is added "xxx@gmail.com"
  And ContactList name is added "xxx"

# address format error case
@developing
Scenario: name and email are empty
  Given ContactList address is ""
  And ContactList name is ""
  When add
  Then ContactList error_area is "error"

# address format error case
@developing
Scenario: add a name only
  Given ContactList address is ""
  And ContactList name is "xxxxxx"
  When add
  Then ContactList error_area is "error"


