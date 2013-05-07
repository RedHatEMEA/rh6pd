# rh6pd

===

This is the source for the "Red Hat Middleware 6 Products Demo". 

This is an open source project to pull togther a demo of six Red Hat JBoss products.

PLEAE NOTE - This project is a sandbox for ideas and test code and is not meant for production environments.
             All of the example will require Red Hat Enterprise Versions of JBoss Application Server and the BRMS (rules)   
             Platform. see https://access.redhat.com/downloads to sign up for an evaluation. 

#Description#

Welcome to the JBoss Middleware six products demonstration suite. If your new to JBoss middleware this suite of simple demos will allow you to get up and running with six of the most popular JBoss products very quickly.

The first of the Six Products Demos from JBoss Middleware is a simple business process for a car insurance quote. Essentially we will demonstrate how easy it is to build an I.T platform for selling car insurance. Although a simple representation of the actual insurance industry process it will cover all aspects of Enterprise architecture required to get to a fully functioning quote engine in place.

The  car insurance quote is implemented with JBoss Business Rules Management System (BRMS). This version of BRMS also includes a powerful embeddable Business Process Management system which is used to control the business process .


#Build Information - Developers and contributers#
If you want to build the project yourself (to add code later), then git clone 
using the normal repo URL. 

Please commit back to the project using Git pull requests via GitHub.

# Build Information - "I just want a demo!"
If you want to get the most recent stable version of the project, view our git 
tags here; https://github.com/RedHatEMEA/rh6pd/tags

At the time of writing, the most recent tag is 0.1.0-1. You can clone this tag 
in to your own repo like this; 

        mkdir rh6pd/
        cd rh6pd 
       	git init 
       	git remote add origin git@github.com:RedHatUKI/rh6pd.git
       	git fetch
       	git checkout tags/v0.1.0

This will create a completely seperate instance at version 0.1.0 for testing.
You will not be able to commit any changes to the master branch from here but 
will give you a good view of the project

## Building a package ##

Once you have a Git Repository, you can build a package. Either ZIP or RPM is 
supported (with the maven profile enabled - "RPM"). We recommend ZIP because it
is more flexible. 

## zip ##
From the root of your Git repo, run;

	maven install 
	
This will create a zip file in the (rh6pd-packages/rh6pd-package-zip/target/) directory

From now, you can install the car insurance demo - use the demo guide found in 
"rh6pd/documentation/demoguide/demoguide.odt"

You will need to pull the latest version of EAP 6 and BRMS to run the demo.

There is also a video of the demo being presented: 
"documentation/BRMS Car Insurance Demo.m4v"

## rpm ##
To build an RPM, run `maven clean install -P rpm` - the `rpm` profile needs to be activated.
