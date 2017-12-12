Feature: Mail Send


# success case
Scenario: send mail success one
  Given address is "xxx@gmail.com"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is none
  And should receive the following emails:
    | from                        | to            | subject       | body      |
    | gadget.mailsender@gmail.com | xxx@gmail.com | hello         | message   |


Scenario: send mail success two
  Given address is "xxx@gmail.com;yyy@gmail.com"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is none
  And should receive the following emails:
    | from                        | to            | subject       | body      |
    | gadget.mailsender@gmail.com | xxx@gmail.com | hello         | message   |
    | gadget.mailsender@gmail.com | yyy@gmail.com | hello         | message   |


# 1 field error
Scenario: address is empty
  Given address is ""
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"


Scenario: subject is empty
  Given address is "xxx@gmail.com"
  And subject is ""
  And body is "message"
  When send
  Then error_area is "error"


Scenario: body is empty
  Given address is "xxx@gmail.com"
  And subject is "hello"
  And body is ""
  When send
  Then error_area is "error"


# 2 field error
Scenario: address, subject is empty
  Given address is ""
  And subject is ""
  And body is "message"
  When send
  Then error_area is "error"

Scenario: address, body is empty
  Given address is ""
  And subject is "hello"
  And body is ""
  When send
  Then error_area is "error"

Scenario: subject, body is empty
  Given address is "xxx@gmail.com"
  And subject is ""
  And body is ""
  When send
  Then error_area is "error"


# 3 field error
Scenario: address, subject, body is empty
  Given address is ""
  And subject is ""
  And body is ""
  When send
  Then error_area is "error"


# address format error
Scenario: address format error: not include @
  Given address is "xxx"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"

Scenario: address format error: only @
  Given address is "@"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"

Scenario: address format error: end with @
  Given address is "xxx@"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"

Scenario: address format error: start with @
  Given address is "@xxx"
  And subject is "hello"
  And body is "message"
  When send
  Then error_area is "error"

### replace subject placeholder
## success case
Scenario: replace subject success one person
  Given subject is "Hi $name"
  And address is "user1@gmail.com"
  And body is "message"
  When send
  Then error_area is none
  And should receive the following emails:
      | from                        | to            | subject       | body      |
      | gadget.mailsender@gmail.com | user1@gmail.com | Hi user1    | message   |

Scenario: replace subject success two person
  Given subject is "Hi consumers"
  And address is "user1@gmail.com;user2@gmail.com"
  And body is "message"
  When send
  Then error_area is none
  And should receive the following emails:
      | from                        | to            | subject       | body      |
      | gadget.mailsender@gmail.com | user1@gmail.com | Hi consumers    | message   |
      | gadget.mailsender@gmail.com | user2@gmail.com | Hi consumers    | message   |

Scenario: replace subject success two person but no name attribute
  Given subject is "Hi consumers"
  And address is "user1@gmail.com;noname@gmail.com"
  And body is "message"
  When send
  Then error_area is none
  And should receive the following emails:
      | from                        | to            | subject       | body      |
      | gadget.mailsender@gmail.com | user1@gmail.com | Hi consumers    | message   |
      | gadget.mailsender@gmail.com | noname@gmail.com | Hi consumers    | message   |

### replace body placeholder
## success case
@developing
Scenario: replace body success one person
  Given body is "Hi $name"
  And subject is "Hi"
  And address is "user1@gmail.com"
  When send
  Then error_area is none
  And should receive the following emails:
      | from                        | to            | subject       | body      |
      | gadget.mailsender@gmail.com | user1@gmail.com | Hi    | Hi user1   |

@developing
Scenario: replace body success two person
  Given subject is "Hi"
  And address is "user1@gmail.com;user2@gmail.com"
  And body is "Hi $name"
  When send
  Then error_area is none
  And should receive the following emails:
      | from                        | to            | subject       | body      |
      | gadget.mailsender@gmail.com | user1@gmail.com | Hi    | Hi user1   |
      | gadget.mailsender@gmail.com | user2@gmail.com | Hi    | Hi user2   |

@developing
Scenario: replace body success two person but no name attribute
  Given subject is "Hi consumers"
  And address is "user1@gmail.com;noname@gmail.com"
  And body is "message"
  When send
  Then error_area is none
  And should receive the following emails:
      | from                        | to            | subject       | body      |
      | gadget.mailsender@gmail.com | user1@gmail.com | Hi consumers    | message   |
      | gadget.mailsender@gmail.com | noname@gmail.com | Hi consumers    | message   |

## error case
@developing
Scenario Outline: error case
  Given subject is "<subject>"
  And address is "<addresses>"
  And body is "<body>"
  When send
  Then error_area is "error"

  Examples:
  | subject | addresses | body |
  | Hi $name | noname@gmail.com | hello |
  | Hi $name | noname@gmail.com;user1@gmail.com | hello |
  | Hi $name | noregisterd@gmail.com | hello |
  | Hi       | noname@gmail.com | Hi $name |
  | Hi       | noname@gmail.com;user1@gmail.com | Hi $name |
  | Hi       | noregisterd@gmail.com| Hi $name |

