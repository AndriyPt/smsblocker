# How it works #

Application consists from service which run in background and listen for SMS receive event.

Service check if there is any new SMS and if sender is in blacklist.
In case if sender is in the blacklist then delete SMS and put information into log.