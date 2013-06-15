## Table of Contents
 - [How to run this project](#how-to-run-this-project)
 - [Developing in Eclipse](#developing-in-eclipse)
 - [How to create a brand new Maven/Eclipse project for the Jersey Framework](#how-to-create-a-brand-new-maveneclipse-project-for-the-jersey-framework)

### How to run this project
 - To run it on your local machine:
   - Clone it locally
     - mkdir ~/dev
     - cd ~/dev
     - git clone https://github.com/ShoppinPal/cloud-pos-proxy.git
   - Make sure that you have the [foreman](https://github.com/ddollar/foreman) gem installed:
     - It requires RubyGems version >= 1.3.6, you can update your version of rubygems via:
         - sudo gem update --system
     - To install foreman, run:
         - sudo gem install foreman
   - With foreman present, simply use:
     - create a file named **.env** inside the **~/dev/cloud-pos-proxy/** folder and configure the following environment variables:
         - cut & paste

                    CLOUDAMQP_URL=amqp://username:password@rabbitmq.host.com/instanceName
             - You can get your own free RabbitMQ instance with a URL that's ready-to-go at [CloudAMQP](http://www.cloudamqp.com/plans.html)
         - cut & paste into .env file

                    JAVA_OPTS=-Xmx384m -Xss512k -XX:+UseCompressedOops
             - If you want to remote debug this app while its running, you can use:
                 - cut & paste into .env file

                            JAVA_OPTS=-Xmx384m -Xss512k -XX:+UseCompressedOops -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=9000,server=y,suspend=n
                     - You may replace port 9000 with another port # if its already in use.
         - cut & paste into .env file

                    PORT=5001
         - cut & paste into .env file

                    MAVEN_OPTS=-Xmx384m -Xss512k -XX:+UseCompressedOops
         - cut & paste into .env file

                    PATH=/app/.jdk/bin:/usr/local/bin:/usr/bin:/bin

     - then to launch the server, simply run:
         - cut & paste onto cmd line terminal

                    mvn clean install && foreman start
 3. In order to run a test:
   - Launch its counterpart [cloud-pos-router](https://github.com/ShoppinPal/cloud-pos-router#how-to-run-this-project)
     - cd ~/dev/cloud-pos-router && mvn clean install && foreman start
   - Launch intranet-pos-proxy itself
     - cd ~/dev/intranet-pos-proxy && mvn clean install && foreman start
   - Use the following to ping cloud-pos-router:
     - http://localhost:5000/amqp/example
     - It will post a delegated request for intranet-pos-proxy to fulfill on RabbitMQ:
     ![alt text](https://raw.github.com/ShoppinPal/cloud-pos-router/master/sample.png "RabbitMQ Sample Message Snapshot")
     - intranet-pos-proxy will reach out to RabbitMQ on its own, pick up the request and fulfill it by proxying it to the POS sitting within the intranet and 

### Developing in Eclipse
 1. Clone it locally
   - mkdir ~/dev
   - cd ~/dev
   - git clone https://github.com/ShoppinPal/cloud-pos-proxy.git
 2. Import project into Eclipse as an already existing maven project
 3. You can enable direct Git access from within Eclipse if you follow these [instructions](http://stackoverflow.com/questions/7194877/how-make-eclipse-egit-recognize-existing-repository-information-after-update).

### How to create a brand new Maven/Eclipse project for the Jersey Framework
 1. Start Eclipse
 2. Right-Click in the Navigator pane
 3. New > Project > Maven Project
 4. Next
 5. Uncheck
    Use default Workspace location
 6. Set location to:
    ~/dev
 7. Next
 8. Catalog:
    All Catalogs
 9. Filter:
    webapp
 10. Select the one with the Artifact Id: maven-archetype-webapp
 11. Next
 12. Group Id: com.companyName
 13. Artifact Id: projectName
 14. Package: umbrella.project.name (optional - you can leave it empty)
 15. Finish
 16. Jersey 1.13 User Guide
   - Section [11.3.5. Servlet](http://jersey.java.net/nonav/documentation/latest/chapter_deps.html#d4e1712)
     - Deploying an application on a servlet container requires a deployment dependency with that container.
     - Using servlet: **com.sun.jersey.spi.container.servlet.ServletContainer** requires a dependency on the jersey-servlet module.
 17. Add dependencies on jersey-server and jersey-servlet
 18. Remove junit dependency as its not being used at this point in time.
