Feature: Refund item

# address format error case
@developing
Scenario: add a wrong address
  Given address is "xxx"
  When add
  Then contactlist error_area is "error"

# duplicate address error case
@developing
Scenario: add a duplicate address
  Given address is "xxx@gmail.com"
  And ContactList has "xxx@gmail.com"
  When add
  Then contactlist error_area is "error"

# success case
@developing
Scenario: add a address
  Given ContactList address is "xxx@gmail.com"
  And ContactList is empty
  When add
  Then error_area is ""
  And address is added "xxx@gmail.com"