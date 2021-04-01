# Pet
Group project for Solution Challenge 2021.
Group member:
- 🧑Chris Li (Sihan Li)
- 👧Haolin Liu
- 🧑Liam Xue (Yiping Xue)
- 👧Linda Wang

# New Features (this branch)
- New UI for calendar

# Todo
- Beautify Calendar
  - Change generation method
  - (✔) change icon / colored blob
- Add pet dialog
  - Phrases
- Statistics Fragment
  - need to show all goals, with proper message
- CustomUser profile to showcase the app is needed
  - can be edited in Tester.java
- More reasonable Tester.java
- Home Activity
  - (✔) upcoming task: show DDL
  - upcoming task: ripple effect on task itself
  - upcoming task: change colors and checkbox state due to DDL
- Todo Activity
  - (✔) display fixed color to side bar
  - (✔) display random color to side bar
  - (✔) display more colors
  - "more" might needs a button style
- (✔) New Font

# Bugs
- Navbar
  - floating action button animation cancellation
    - will be ignored for the time being
- homeFragment.java
  - After creating a new goal/task, in order to show the new one, we need to switch to another fragment and switch back.
- AddTaskActivity
  - consistency issue when update the rule
    - will not be resolved unless major structure change on file system
    - may be ignored
  - (✔) adding task will encounter an error when selecting what goals belongs to
- Todo Fragment
  - unfinished show more render empty spaces
  - (✔)Date display formatting
