# Meme
Page with memes :)
## Stack
- Kotlin
- Spring Boot
- JWT
- MySQL
- Hibernate 
## Endpoints 
### Guest
````
├── /auth
│ 	├── /login - POST
│	├── /register - POST
│       └── /reset - POST
│		└── /confirm - POST
├── /posts - GET
│	└── /{id} - GET
│		├── /comments - GET
│		└── /feedback - POST
├── /users - GET
│	└── /{nickname} - GET
│		├── /posts - GET
│		└── /comments - GET
├── /comments
│	└── /{id} - GET
└── /tags - GET
	└── /{name}
		└── /posts - GET
````
### Active User
````
├── /auth
│ 	├── /login - POST
│	├── /register - POST
│       └── /reset - POST
│		└── /confirm - POST
├── /posts - POST / GET
│	└── /{id} - GET
│		├── /comments - GET / POST
│		└── /feedback - POST
├── /users - GET
│	└── /{nickname} - GET
│		├── /posts - GET
│		├── /comments - GET
│		└── /feedback - POST
├── /comments
│	└── /{id} - GET
│		└── /feedback - POST
├── /tags - GET
│	└── /{name}
│		└── /posts - GET
└── /self - GET
	├── /active - POST
	│   └── /resend - POST
	├── /logout - POST
	├── /avatar - POST
	└── /password - POST
````
### Admin
```
├── /auth
│ 	├── /login - POST
│	├── /register - POST
│       └── /reset - POST
│		└── /confirm - POST
├── /posts - POST / GET
│	└── /{id} - GET / DELETE / PUT
│		├── /comments - GET / POST
│		└── /feedback - POST / GET
├── /users - GET
│	└── /{nickname} - GET
│		├── /bans - GET / POST
│		├── /warns - GET / POST
│		├── /posts - GET
│		├── /comments - GET
│		└── /feedback - POST / GET
├── /comments
│	└── /{id} - GET
│		└── /feedback - POST / GET
├── /tags - GET
│	└── /{name}
│		└── /posts - GET
└── /self - GET
	├── /active - POST
	│    └── /resend - POST
	├── /logout - POST
	├── /avatar - POST
	└── /password - POST
```
