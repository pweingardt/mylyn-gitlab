# Mylyn Gitlab Connector


The Mylyn Gitlab Connector allows you to connect Mylyn to your self hosted Gitlab instance in order to manage your issues on Gitlab with your local Eclipse instance.


## Features

* create, edit, close and reopen issues
* comment on issues
* search for issues using milestones, assignees, issue state and regular expressions for labels
* supports project milestones and assignees
* supports three priorities (low, normal, high) and three types (bug, feature, story) using labels. You don't have to use this feature, but if you want to use "type:bug" and "priority:high" as labels (comma separated). As of version 1.1.0 you don't need the prefixes "type:" and "priority:", just use "bug", "high".
* handles issues on a project basis


## Missing features

* Password prompt (I just don't know how...)
* Attachments. Downloading attachments should be possible, but is not implemented yet. Uploading attachments doesn't seem to be possible with the current Gitlab API.
* Project member management and milestone management. I won't implement this...


## Usage

1. Install the plugin obviously (you can use the http://pweingardt.github.com/mylyn-gitlab update site)
2. Add a new Connector, using the new Gitlab Connector
  1. enter the project URL (something like http(s)://my-gitlab-instance.org/myname/myproject)
  2. enter your usename and your password
  3. **Do not forget to check the "Save Password" checkbox**. I don't know how to create a password prompt...
3. You can now create queries and issues

If you use https instead of http (and you absolutely should use https), be sure you have a valid certificate. That means it is signed by a trusted CA. If you don't have a valid certificate (like a self signed certificate), the plugin will refuse to connect. If you want to add your CA certificate to the java keystore, you have to:

1. find the keystore which is used by your JVM (on my machine it is /etc/ssl/certs/java/cacerts)
2. find out the password for the keystore (the default is "changeit")
3. add the CA certificate to this keystore (root permissions might be necessary) with `keytool -import -alias A-UNIQUE-ALIAS -file YOUR-CA.crt -keystore $PATH_TO_YOUR_KEYSTORE`

I don't want to ignore ceritificate errors (it is possible though). It is not a good way to do those kind of things.

## Queries

Say you want to search for open bugs and features (issues that are either features or bugs). If you add the labels "bug" and "feature" in the query settings page, the plugin will search for issues, that have *both* labels. The plugin will search for issues that are bugs, and *also* features. If you want to search for bugs *or* features, you have to use regular expressions: `(bug|feature)` will do the job.

## Known issues

* If you created a new milestone or added a new project member via the web interface, you have to update the repository configuration, so that the connector reloads the project members and milestones. Right click on the gitlab repository in the Task repositories view and click on "Update Repository Configuration". 
* Offline mode does not work.
* Maven generally. This is my first project using Maven, so it might not work on a different machine
