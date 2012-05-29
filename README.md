# Play 2.0 Database Sharding Example

This is a simple example application that demonstrates one way to shard data across two postgres databases using Play 2.0 and JDBC.

## Running the application on Heroku

You can run this application on Heroku under your own account. Start by cloning the source code and creating a new Heroku app. From the cloned directory:

    $ heroku create --stack cedar
    $ git push heroku master

Add two databases to the application by executing the following command twice:

    $ heroku addons:add heroku-postgresql:dev
    ----> Adding heroku-postgresql:dev to play20shardexample... done, v11 (free)
      Attached as HEROKU_POSTGRESQL_CHARCOAL
      Database has been created and is available
        ! WARNING: dev is in beta
        !          increased risk of data loss and downtime
        !          send feedback to dod-feedback@heroku.com

Create the schema in both databases using the `heroku pg:psql` command. For each database run:

    $ heroku pg:psql db_color

Then run the DDL in the ddl file at the psql prompt. Replace `db_color` with the color of each of your databases.
   
Edit the application's configuration. In `conf/application.conf` you'll find:

    # DB1
    db.db1.url=${HEROKU_POSTGRESQL_AMBER_URL}
    db.db1.driver=org.postgresql.Driver

    # DB2
    db.db2.url=${HEROKU_POSTGRESQL_CHARCOAL_URL}
    db.db2.driver=org.postgresql.Driver 

Replace the colors with the two colors in the databases you added.

Now add the changes to git and push the app to Heroku:

    $ git add .
    $ git commit -m 'update database colors'
    $ git push heroku master

## See a live example

You can see a live example of the application at:  http://play20shardexample.herokuapp.com
