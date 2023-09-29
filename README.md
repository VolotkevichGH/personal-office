# Personal Office MITSO

___

## Scheme

* /Login
  * /student/profile
    *student/edit
  * /curator/profile
    * /curator/edit
  * /admin
    * /admin/givestudent/{user}
    * /admin/givecur/{user}
    * /admin/addstudents
      * /admin/students/data

___

## Data scheme

Login
![login](https://i.imgur.com/YWUsvYl.png)

Student Profile
Отображение данных студента и ссылка на редактирование профиля
![profilestudent](https://i.imgur.com/rllZAqj.png)

Student Edit
Редактирование профиля
![edit](https://i.imgur.com/EUYmxgM.png)

Curator Profile
Отображение всех студентов группы куриррования, редактирование профиля.
![curatorprofile](https://i.imgur.com/LPHybpb.png)

Admin
Отображение всех пользователей сайта, некоторая их информация, также редактирование их ролей и удаление аккаунта.
Ссылка на создание аккаунтов для списка студентов.
![admin](https://i.imgur.com/pCKbPET.png)

Admin add student
Добавление списка студентов в формате: Имя Фамилия, Имя Фамилия, Имя Фамилия, Имя Фамилия...
![adminaddstudent](https://i.imgur.com/sJOX2hN.png)

Admin students data
После создания аккаунтов студентов, автоматически генерируются логин и пароль для каждого студента, для дальнейшей рассылки пользователям.
![adminstudensdata](https://i.imgur.com/GEKyKLf.png)

Admin Give Curator
Добавление пользователю роли куратора и заполнение некоторых данных.
![admingivecurator](https://i.imgur.com/vuwk01Z.png)

Admin Give Student
Добавление пользователю роли студента и заполнение данных.
![admingivestudent](https://i.imgur.com/KuN26YI.png)

___

##EndPoints

###LOGIN

| Task | Method | Resource | Request Parameters |
|:----:|:------:|:--------:|:------------------:|
| Авторизация | GET | http://localhost:8080/login | |


###Student

| Task | Method | Resource | Request Parameters |
|:----:|:------:|:--------:|:------------------:|
| Личный профиль студента | GET | http://localhost:8080/student/profile | {"user": user} |
| Редактирование профиля | GET | http://localhost:8080/student/edit | {"user": user} |
| Редактирование профиля | POST | http://localhost:8080/student/edit | {"username":string, "email":string, "oldpassword":string, "password":string} |


###Curator

| Task | Method | Resource | Request Parameters |
|:----:|:------:|:--------:|:------------------:|
| Личный профиль куратора | GET | http://localhost:8080/curator/profile | Authorize user, List<User> |
| Редактирование профиля | GET | http://localhost:8080/curator/edit | Authorize user |
| Редактирование профиля | POST | http://localhost:8080/curator/edit | {"username":string, "email":string, "oldpassword":string, "password":string} |


###Administration

| Task | Method | Resource | Request Parameters |
|:----:|:------:|:--------:|:------------------:|
| Админ панель | GET | http://localhost:8080/admin | {"users": List<User>} |
| Добавление админа | POST | http://localhost:8080/admin/giveadmin/{user} | {"userID": number} |
| Добавление 1 студента | GET | http://localhost:8080/admin/givestudent/{user} | {"userID": number, "studentname": string, "studentsurname": string} |
| Добавление 1 студента | POST | http://localhost:8080/admin/givestudent/{user} | {"userID": number, "curatorname": string, "curatorsurname": string}, "specialization": string, "course": string, "faculty": string, "nameGroup": string,|
| Добавление куратора | GET | http://localhost:8080/admin/givecur/{user} | {"userID": number} |
| Добавление куратора | POST | http://localhost:8080/admin/givecur/{user} | {"userID": number, "lesson": string, "faculty": string, "nameGroup": string|
| Удаление аккаунта | POST | http://localhost:8080/admin/delete/{user} | {"userID": number} |
| Добавление списка пользователей | GET | http://localhost:8080/admin/addstudents | - |
| Добавление списка пользователей | POST | http://localhost:8080/admin/addstudents | {"students": string} |
| Получение логина и пароля новых пользователей без шифра | GET | http://localhost:8080/admin/students/data | "builder": list<string> |
| Получение логина и пароля новых пользователей без шифра | POST | http://localhost:8080/admin/students/data |  |

___


