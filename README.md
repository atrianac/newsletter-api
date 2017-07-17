# newsletter-api - Springer Test

## Cloning the application

 ```bash
 git clone https://github.com/atrianac/newsletter-api.git
 ```


## Running the application

 Use the gradle tasks for Spring Boot to run the application (execute this command inside application directory):
 
 ```bash
 ./gradlew bootRun
 ```
 
 ## Testing the Endpoints
 
 1. Install Postman as is described in [Postman Installation and updates](https://www.getpostman.com/docs/postman/launching_postman/installation_and_updates)
 2. Import the `endpoints.xml` collection as is described in [Postman Sharing collections](https://www.getpostman.com/docs/postman/collections/sharing_collections)
 3. Execute the endpoints according to below description:
 

Endpoint   1. Handle   category   submissions,   a   category   has   a   unique   code,   a   title   and optionally   a   parent   category   (e.g.   science   ->   physics)

`POST /categories  {   “code”:   “string”,   “title”:   “string”,   “superCategoryCode”:   “string”   or  null }`

Endpoint   2. Handle   book   submissions,   a   book   has   a   title   and   a   list   of   category   codes   of categories   to   which   it   belongs   to

`POST /books  {   “title”:   “string”,   “categoryCodes”:   [“code1”,   “code2”,   ...]   }` 

Endpoint   3. Handle   subscriber   submissions,   a   subscriber   has   an   email   and   a   list   of category   codes   of   categories   for   which   he/she   is   interested   in 

`POST /subscribers  {   “email”:   “string”,   “categoryCodes”:   [“code1”,   “code2”,   ...]   }`

Endpoint   4. Return   a   list   of   “newsletters”,   each   newsletter   relates   to   a   subscriber,   a newsletter   has   a   recipient   address   (the   subscriber's   email)   and   a   list   of   notifications,   each notification   contains   the   name   of   a   book   and   a   list   of categoryPaths for   it,   a   categoryPath   is   a list   that   shows the relation of the books' category and the category for which is interested

`GET /newsletters`
