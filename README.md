#### SPRING BOOT

curl -X POST --location "http://localhost:9090/save/1?age=5" \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-d "{ \"name\": \"TEST\" }"

_

#### GRAILS

curl -X POST --location "http://localhost:8080/respondSave/1?age=5" \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-d "{ \"name\": \"TEST\" }"

curl -X POST --location "http://localhost:8080/renderSave/1?age=5" \
-H "Content-Type: application/json" \
-H "Accept: application/json" \
-d "{ \"name\": \"TEST\" }"