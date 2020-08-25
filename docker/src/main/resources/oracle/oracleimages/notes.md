Download [Linux x86-64 zip] from here:
https://www.oracle.com/database/technologies/oracle-database-software-downloads.html
you will need to register Oracle Account and accept Oracle License.
So you will get LINUX.X64_193000_db_home.zip it is 2.8 GB. Make sure that you keep it zip because Safari unzips things unless restricted.

Copy LINUX.X64_193000_db_home.zip into Horus horus-db-migration/src/main/resources/oracleimages/19.3.0 Do not add it to GIT repository.
 
Now you will create docker image. Open horus-db-migration/src/main/resources/oracleimages/ in terminal and execute following command:
 
sh buildDockerImage.sh -v 19.3.0 -e
 
this will run docker file with default configurations and prepare docker image. Approximately 10 minutes.

When image is done start it from docker composer.
 
This will start Container creation and databases initial setup. Container /oracledb you may see in services tab. Database instantiation progress you may see in logs. You may need to connect to docker.
 
Whole process will take around 30 minutes while executing you will see following:
 
 
Prepare for db operation
8% complete
Copying database files
31% complete
Creating and starting Oracle instance
32% complete
36% complete
40% complete
43% complete
46% complete
Completing Database Creation
51% complete
54% complete
Creating Pluggable Databases
58% complete
77% complete
Executing Post Configuration Actions
100% complete
Database creation complete. For details check the logfiles at:
 
This will be printed when its ready.
 
#########################
DATABASE IS READY TO USE!
#########################

If all went well local Oracle instance supposed to be running fine inside docker container.
And should be accessible via:
 
-- URL: jdbc:oracle:thin:@localhost:1521:oracledb
-- USER: SYSTEM
-- PASSWORD: oracle_admin
