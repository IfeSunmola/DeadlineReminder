# DeadlineReminder

I decided to make it public now because I probably won't finish it with how tight my schedule is, so if anyone wants to chip in ...

The app uses Angular (TypeScript) for its front end and spring (java) for back end. The front end is where the real problem is.
Html tags don't like staying where I want them to stay. I think they're conspiring with css to make me frustrated because why is div going to the
right when I said display flex and justify content left? And I'm so bad with anything ui/ux related. I finished the important part of the backend in
less than 4 hours, but I spent almost 6 hours just looking for what colors to use

The app also uses bootstrap and ng-bootstrap (your ide should automatically download them since it's part of node modules). I'm overriding the default
bootstrap colors in the styles.scss file

P.S: I'm learning both Angular and TypeScript as I'm doing this project so there are definitely bound to be some mistakes. Feel free to fix them

## A completed version would:

1. Have user login/registration, email verification, and forgot password. Authentication is done with JWT
2. Registered users can **_create a deadline_**. When creating deadlines, they have the option to choose how often they want to be reminded before the
   deadline.
3. Multiple options are supported
4. E.g. if they choose 6 hours, 2 days, and a week ....  6 hours, 2 days, and a week before the deadline, they will receive an email reminding them
   about ... well, the deadline
5. On the user's homepage, an "Upcoming deadlines" and "previous deadlines" should be shown. Maybe an accordion?
6. For the reminders to be accurate up to the last minute, we would need to hit the database regularly. I'm talking every 1 to 2 minutes, which can
   get really expensive; so, some form of caching must be used. Spring provides its own caching system. Redis is also an option
7. Another option is to only check the database every x minutes, and send the reminders if it happens x minutes into the future. So, reminders
   will be sent x minutes earlier than they are supposed to be, which is better than later.
8. Both client side and server side validations/sanitizing of user input. E.g. emails or stuff starting with something like // or # or ; should be
   rejected. Spring data jpa should automatically escape these characters before taking it to the database, but I have major trust issues so we gotta
   do this :)
9. I'll add more as I remember ...

## Download the following before opening the project:

1. MySql
2. npm
3. Angular cli
4. Java 17

The application.properties file is set to create the database if it doesn't exist. But, you'll still need to enter the username and password to your
MySql local server. I should have used an in memory database so all those wouldn't be an issue, but I wasn't expecting to make this public. I'll
change to H2 Database in time.

Your IDE should ask to download required node modules.

## Editor Config

To avoid merge issues over minor things like indentation space and braces positions, our editor configurations have to be similar. A copy of mine is
in the root. A `.editorconfig` file should have higher precedence over your default IDE settings so there shouldn't be any configuration done on your
part

------------------------------------------------------------------

# Yes, I know there must be services out there that already do something like this, but I still wanted to do this.