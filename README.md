### Time Spent Report Browser for Gitlab

This software allows you to browse, filter and group time spent reports for your self-hosted GitLab. We have developed this as the free version of Gitlab allows one to create time spent reports but the functionality to review them are very limited.

Note: This software is developed by a 3d party. Gitlab is the trademark of its respective owner.

Here is how it looks:

<img width="1719" alt="Screenshot 2023-06-19 at 17 58 50" src="https://github.com/SCD-company/gitlab-times/assets/5467120/ca146a41-77a7-49bd-8d8f-acc771e2c854">


To use this software you need:


* Self-hosted Gitlab and at least one user account in it
* Access to the Gitlab’s database
* Docker ([https://www.docker.com/](https://www.docker.com/))


### Features

The system allows browsing time-spent reports. It lets you:

* Filter the reports by project, person and time period,
* Group the reports by project, person, month and issues,
* Calculate the reports based on report creation time OR time spent,
* Download the result as a CSV file (The same format as Redmine),
* Download the result as a PDF file.


### Authorization and access rights

The system uses authentication via the same GitLab instance it works with. Hence, you authenticate via GitLab’s Oauth2 as one of the GitLab users.

The system supports three levels of access based on the permissions of the authenticated users.


<table>
  <tr>
   <td>Access level
   </td>
   <td>What user can see
   </td>
   <td>What GiLlab permissions he/she needs
   </td>
  </tr>
  <tr>
   <td>Admin
   </td>
   <td>All time spent reports
   </td>
   <td>User needs to be the Gitlab administrator
   </td>
  </tr>
  <tr>
   <td>Project owner
   </td>
   <td>Time report created for all the projects where he/she is the owner + all time reports created by them
   </td>
   <td>User needs be assigned the “owner” role for these project(s)
   </td>
  </tr>
  <tr>
   <td>Ordinary user
   </td>
   <td>Their personal time reports only
   </td>
   <td>User does not need any special permissions
   </td>
  </tr>
</table>


### Limitations and special notes

**The system only shows the time that were reported on issues**

Somehow, GitLab allows reporting also on merge requests. 

The system will ignore all such reports. 

**The system will use default time zone**

The system uses the time zone of the server where it is installed.

It affects the boundaries when filtering using periods.

If you need another time zone, use environment variables within docker-compose.yml

See [https://howtodoinjava.com/java/date-time/setting-jvm-timezone/](https://howtodoinjava.com/java/date-time/setting-jvm-timezone/) 

**The system displays a special behavior when it comes to archived projects and people without access (locked)**

On the one hand, if a project or person is archived or locked, they will not appear within the corresponding filters. On the other hand, they will appear in the result, if corresponding time reports are allowed by the filters. (E.g. if you use the “-all-” value of some filters.) 

To make it less confusing, archived projects and locked people are colored gray in the results section.

E.g. “PowerUpcheckout”, “The site”, “marketing” and “zonesmart” are the archived projects as shown below:

<img width="980" alt="Screenshot 2023-06-19 at 18 07 49" src="https://github.com/SCD-company/gitlab-times/assets/5467120/59ed9793-0ef7-4b8e-8e4f-adf102ae44de">

**The total amount of time can vary from the sum of time because of rounding**

The system rounds all time values to the two numbers after the dot.

When the system summarizes several lines it:

* Rounds each line separately
* Summarizes NOT rounded values and rounds the result

As a result, the total amount may vary slightly from summarizing **rounded** values. 

E.g. if you have

Project1:  2.055

Project2:  1.056 

It will show:

Project1:  2.06

Project2:  2.06

Total:    3.11 

Total will not be 3.12 as 2.055+1.056 = 3.111


### Installation

The simplest way to install the system is to use docker. The project is automatically built as two docker images.

Here is the sample docker-compose.yml to start it:


```
version: '3.6'

services:
 backend:
   image: scdcompany/gitlab-time-back:latest
   restart: always
   ports:
     - 127.0.0.1:8281:8080
   environment:
     - APP_PROTOCOL=https
     - SPRING_DATASOURCE_URL=${GITLAB_TIME_DB_URL}
     - SPRING_DATASOURCE_USERNAME=${GITLAB_TIME_DB_USER}
     - SPRING_DATASOURCE_PASSWORD=${GITLAB_TIME_DB_PASSWORD}
     - APP_SERVERDOMAIN={report.gitlab.softaria.com}
     - APP_APPLICATIONDOMAIN={report.gitlab.softaria.com}
     - GITLAB_CLIENT_CLIENTID={INSERT ID HERE}
     - GITLAB_CLIENT_CLIENTSECRET={INSERT SECRET HERE}
     - GITLAB_CLIENT_ACCESSTOKENURI=https://{yourgitlab.com}/oauth/token
     - GITLAB_CLIENT_USERAUTHORIZATIONURI=https://{yourgitlab.com}/oauth/authorize
     - GITLAB_RESOURCE_USERINFOURI=https://{yourgitlab.com}/api/v4/user
     # - LOGGING_LEVEL_COM_QUERYDSL_SQL=DEBUG
     # - SPRING_JPA_SHOW-SQL=true

 frontend:
   image: scdcompany/gitlab-time-front:latest
   restart: always
   ports:
     - 127.0.0.1:8280:80
   volumes:
     - ./nginx.conf:/etc/nginx/nginx.conf
```


As you can see in the sample, there are several environment variables that need to be set before starting the docker containers. They are listed in the docker-compose.yml file above.

Here is the meaning behind each one.

The first three variables need to point to the GitLab database.

     - SPRING_DATASOURCE_URL=${GITLAB_TIME_DB_URL}

     - SPRING_DATASOURCE_USERNAME=${GITLAB_TIME_DB_USER}

     - SPRING_DATASOURCE_PASSWORD=${GITLAB_TIME_DB_PASSWORD}

For example:

    - SPRING_DATASOURCE_URL=jdbc:postgresql://gitlab:5432/gitlabhq_production

  	- SPRING_DATASOURCE_USERNAME=times

  	- SPRING_DATASOURCE_PASSWORD=12345678

The next two variables allow you to specify domains for the application itself and for its Back-End’s RESTful API:

     - APP_SERVERDOMAIN=report.gitlab.scd-company.com

     - APP_APPLICATIONDOMAIN=report.gitlab.scd-company.com

Note, that the application itself starts at localhost, hence a type of HTTP server (such as nginx or Apache) is expected to be configured above and to forward requests from some of your domains. If you prefer using the system from localhost, skip these two variables.

Finally, as the system uses authorization via the same GitLab instance, it needs several variables to configure Oauth2 access to your Gitlab instance:

     - GITLAB_CLIENT_CLIENTID={INSERT ID HERE}

     - GITLAB_CLIENT_CLIENTSECRET={INSERT SECRET HERE}

     - GITLAB_CLIENT_ACCESSTOKENURI=https://{yourgitlab.com}/oauth/token

     - GITLAB_CLIENT_USERAUTHORIZATIONURI=https://{yourgitlab.com}/oauth/authorize

     - GITLAB_RESOURCE_USERINFOURI=https://{yourgitlab.com}/api/v4/user

Please, take a look at [https://docs.gitlab.com/ee/integration/oauth_provider.html](https://docs.gitlab.com/ee/integration/oauth_provider.html) to find how to turn on the Oauth2 provider on GitLab. Please replace {yourgitlab.com} with your gitlab URL.

When all the environment variables are set, just change directory to one with docker-compose.yml, place this nginx.conf file in the same directory and run

docker-compose up -d 

**Note**: In new docker environments, it may be without a dash, for example:

docker compose up -d 

The system should start at localhost, port 8280. (Feel free to change the port within docker-compose.yml, the frontend’s ports section)

**Important note 1**: If your GitLab is installed as a docker service, you just need to append the services from the docker-compose.yml above to the docker-compose.yml where your GitLab is described.

**Important note 2**: Even though you can use an existing database user (e.g. postgres) to access the GitLab database, we recommend creating a new one particularly for this purpose. The new user should be granted read-only access to the GitLab database to ensure the report browser will never change the database.

For example:

CREATE USER times WITH PASSWORD ‘12345678’;

GRANT CONNECT ON DATABASE gitlabhq_production TO times;

GRANT CONNECT ON SCHEMA gitlab_time TO times;

GRANT SELECT ON ALL TABLES IN SCHEMA gitlab_time TO times;


### Development

If you want to fork this project and make changes in its source code, please do the following steps:

1. Fork it and checkout
2. Make a database dump from your gitlab database [https://www.postgresql.org/docs/current/backup-dump.html](https://www.postgresql.org/docs/current/backup-dump.html)
3. Name the dump ‘gitlab_dump.sql’ and place the file at the docker/scripts directory

When you run development or test configuration, it will use this dump to restore a dev database from it. Now you need to provide your Oauth2 properties to the development/test environments. To do so, please 

4. Got to _gitlab-time-back/src/main/resources_ directory
5. Find file named _application-dev-template.properties_
6. Copy it as _application-dev.properties_
7. Edit it - insert your Oauth2 properties as stated inside the file

To run the **development** configuration:


1. Build _gitlab-time-back_ with your IDE (VSC, Idea or whatever works with java)
2. Find _com.scd.gitlabtimeback.GitlabTimeBackApplication_ java class and start it with your IDE
3. Go to _gitlab-time-front_ directory and run:

yarn install

yarn start

(you may use npm if you prefer)

The system will be accessible at localhost:3000

To run the **test** configuration you don’t need IDE, npm or yarn.

Instead, you need docker. This configuration was made for QA specialists.

Go to docker/test directory and run

docker-compose up –build

It will build the project from sources and run it. The system will be accessible at localhost:3000

If you have questions, create an issue and we will respond.

Thanks for reading, and we hope you will enjoy it!

[SCD Company team](https://scd-company.com)


 [![docker](https://img.shields.io/docker/pulls/scdcompany/gitlab-time-back?label=backend pulls)](https://hub.docker.com/repository/docker/scdcompany/gitlab-time-back) [![docker](https://img.shields.io/docker/pulls/scdcompany/gitlab-time-front?labe=frintend pulls)](https://hub.docker.com/repository/docker/scdcompany/gitlab-time-front)
