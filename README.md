# sm-exam-1
This repo is for exam Email Service with failover for 2 email sevice API

Requires java 11 runtime.

Steps to run:
1. download zip archive and extract
2. cd exam-1
3. (set JAVA_HOME)
4. provide APIKEYS for SendGrid and Mailgun (currrently both accounts are revoked and apikeys are not usable) in environment variables.
5. gradle bootRun

use POSTMAN to send email to: 
http://localhost:8080/send-email

json request format:

{
    "to": "<sampleemail@sample.com>",
    "subject": "My Subject",
    "body": "this is a test"
}

TODO:
1. Create Service Implementation for Email Service.
2. Create JUnit Tests:
   a. Test connectivity
   b. Test credentials
