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



#Build Information#
To build the project :-

  maven install - will create a zip file in the rh6pd/rh6pd-packages target directory

	to install the car insurance demo - use the demo guide found in rh6pd/documentation/demoguide/demoguide.odt
        you will need to pull the latest version of EAP 6 and BRMS to run the demo
        
	There is also a video of the demo being presented in the same documentation directory - BRMS Car Insurance Demo.m4v

