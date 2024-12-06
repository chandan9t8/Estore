The file structure of this project is

├── Estore/
│   ├── bin/                   # Directory for compiled .class files and JARs
│   │
│   ├── client/
│   │   └── EstoreClient.java  # Client-side main class
│   │
│   ├── common/                # Common classes used by both client and server
│   │   ├── Admin.java
│   │   ├── Estore.java
│   │   ├── FrontController.java
│   │   ├── Person.java
│   │   ├── PersonFactory.java
│   │   ├── Product.java
│   │   ├── ShoppingCart.java
│   │   └── User.java
│   │
│   └── server/
│       ├── EstoreImpl.java        # Server implementation of Estore
│       ├── EstoreServer.java      # Server-side main class
│       └── FrontControllerImpl.java  # Implementation of FrontController
│
├── Makefile                   # Makefile for building and running the project
│
└── README.txt                 # Documentation and general information



Instructions to execute the project:

- `make all` to compile all the files
- `rmiregistry &` to start the rmiregistry on the default port. If port already in use exception occurs, use `rmiregistry 3000 &`
- `make runserver`  to start the server
- connect to another remote server and `make run client` to run the client
- after performing operations, `make clean` to clean up the generated files.
