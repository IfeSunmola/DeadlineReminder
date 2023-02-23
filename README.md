# Deadline Reminder

## If you're running locally, you need:

1. Java 17
2. npm - download from https://nodejs.org/en/download/
3. Angular CLI - download with `npm install -g @angular/cli`
4. MailDev (spring boot won't start without this). Download with `npm install -g maildev`
    1. When testing/developing, This is where all the emails that are sent goes to
    2. After installing start by running `maildev`
    3. Open `http://0.0.0.0:1080/#/` to see the emails that will be sent

## Intellij Idea Ultimate

1. If you want to run locally, and you have Intellij Idea Ultimate, open the cloned folder
   as a project.
2. A notification should appear in the bottom right corner. Asking you to
   `load maven build scripts` and  `configure angular cli framework`
3. The run configuration in ./run folder will be automatically loaded, and you should see
   `Dev - Frontend` in the top right. Running it will start the client on port `4200`
4. Click on the drop-down and switch to `Dev - Backend` to run the server on port `8080`

## Other IDEs

1. The process should be similar with both angular and spring boot 