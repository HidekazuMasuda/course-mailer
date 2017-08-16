Feature: Refund item

Scenario: Jeff returns a faulty microwave
  Given headline is "Nasa replies to 9-year-old 'Guardian of the Galaxy' who applied for job"
  And censored word is "fun"
  When censor
  Then censored headline is "Nasa replies to 9-year-old 'Guardian of the Galaxy' who applied for job"