Feature: Refund item

# 1 field error
@developing
Scenario: address is empty
  Given address is ""
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"

@developing
Scenario: subject is empty
  Given address is "xxx@gmail.com"
  And subject is ""
  And body is "message"
  When send
  Then error_area is "error"

@developing
Scenario: body is empty
  Given address is "xxx@gmail.com"
  And subject is "hello"
  And body is ""
  When send
  Then error_area is "error"


# 2 field error
@developing
Scenario: address, subject is empty
  Given address is ""
  And subject is ""
  And body is "message"
  When send
  Then error_area is "error"

@developing
Scenario: address, body is empty
  Given address is ""
  And subject is "hello"
  And body is ""
  When send
  Then error_area is "error"

@developing
Scenario: subject, body is empty
  Given address is "xxx@gmail.com"
  And subject is ""
  And body is ""
  When send
  Then error_area is "error"


# 3 field error
@developing
Scenario: address, subject, body is empty
  Given address is ""
  And subject is ""
  And body is ""
  When send
  Then error_area is "error"


# address format error
@developing
Scenario: address format error: not include @
  Given address is "xxx"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"

@developing
Scenario: address format error: only @
  Given address is "@"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"

@developing
Scenario: address format error: end with @
  Given address is "xxx@"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"

@developing
Scenario: address format error: start with @
  Given address is "@xxx"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"


# success case
Scenario: send mail success one
  Given address is "xxx@gmail.com"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is ""
  And should receive the following emails:
    | from                        | to            | subject       | body      |
    | gadget.mailsender@gmail.com | xxx@gmail.com | hello         | message   |

@developing
Scenario: send mail success two
  Given address is "xxx@gmail.com;yyy@gmail.com"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is ""
  And should receive the following emails:
    | from                        | to            | subject       | body      |
    | gadget.mailsender@gmail.com | xxx@gmail.com | hello         | message   |
    | gadget.mailsender@gmail.com | yyy@gmail.com | hello         | message   |
