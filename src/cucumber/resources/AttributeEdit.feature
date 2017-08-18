Feature: Edit Attributes

@developing
Scenario Outline: save attribute success.
  Given the first_name is <first_name_value>
  And the last_name is <last_name_value>
  And the attr1_field is <free_attr1>
  And the attr1_value field is <free_attr1_value>
  And the attr2_field is <free_attr2>
  And the attr2_value field is <free_attr2_value>
  When save
  Then attribute saved.

  Examples:
    | first_name_value | last_name_value | free_attr1 | free_attr1_value | free_attr2 | free_attr2_value |
    | George  | Washington | country | usa  | birth | 2.22.1732 |
    | George  | Washington | country | null | birth | null      |
    | null    | null       | null    | null | null  | null      |

@developing
Scenario Outline: Free attribute key is empty.
  Given the first_name is <first_name_value>
  And the last_name is <last_name_value>
  And the attr1_field is <free_attr1>
  And the attr1_value field is <free_attr1_value>
  And the attr2_field is <free_attr2>
  And the attr2_value field is <free_attr2_value>
  When save
  Then attribute edit page error_area is "error"

  Examples:
   | first_name_value | last_name_value | free_attr1 | free_attr1_value | free_attr2 | free_attr2_value |
   | George  | Washington | null    | usa | null  | 2.22.1732 |
   | George  | Washington | null    | usa | birth | 2.22.1732 |
   | George  | Washington | country | usa | null  | 2.22.1732 |
