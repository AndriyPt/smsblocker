# Why do we need such application #

Aim of this application is to add ability to block unwanted SMS using minimum subset of permissions and be maximum safe in case of error.

Similar applications

  * require too many rights
  * are for blocking SMS and Phone calls not only SMS
  * too complicated

SMS Blocker requires only three permission
  * **RECEIVE\_BOOT\_COMPLETED** to start filtering service when device boots
  * **READ\_SMS** to get phones of the recent SMS
  * **RECEIVE\_SMS** to delete unwanted SMS and do not show notification

