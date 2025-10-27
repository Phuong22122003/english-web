# English microservice

## Services
- User service
- Learning service
- Content service
- Agent service
## Jenkins
docker run -d --name jenkins -u root -p 8085:8080 -p 50000:50000 -v /var/run/docker.sock:/var/run/docker.sock -v jenkins_home:/var/jenkins_home -v /usr/bin/docker:/usr/bin/docker jenkins/jenkins:lts  