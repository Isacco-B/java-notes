package com.isaccobertoli;

import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.Objects;

import com.isaccobertoli.controllers.AuthController;
import com.isaccobertoli.controllers.NoteControler;
import com.isaccobertoli.controllers.UserController;
import com.isaccobertoli.database.MongoConnection;
import com.isaccobertoli.repository.AuthRepository;
import com.isaccobertoli.repository.NoteRepository;
import com.isaccobertoli.repository.UserRepository;
import com.isaccobertoli.routes.AuthRoutes;
import com.isaccobertoli.routes.NoteRoutes;
import com.isaccobertoli.routes.UserRoutes;
import com.isaccobertoli.services.AuthService;
import com.isaccobertoli.services.NoteService;
import com.isaccobertoli.services.UserService;
import com.isaccobertoli.utils.EnvUtil;
import com.isaccobertoli.utils.ExceptionHandlersUtil;
import com.mongodb.client.MongoDatabase;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        String serverUrl = Objects.requireNonNullElse(EnvUtil.getEnv("SERVER_URL"), "http://localhost/");
        int serverPort = Objects.requireNonNullElse(Integer.parseInt(EnvUtil.getEnv("SERVER_PORT")), 7000);

        System.out.println("Checking database connection...");
        if (!MongoConnection.testConnection()) {
            System.err.println("Failed to connect to the database. Server will not start.");
            System.exit(1);
        }
        System.out.println("Database connection successful!");

        MongoDatabase database = MongoConnection.getDatabase();

        UserRepository userRepository = new UserRepository(database);
        AuthRepository authRepository = new AuthRepository(database);
        NoteRepository noteRepository = new NoteRepository(database);

        UserService userService = new UserService(userRepository);
        AuthService authService = new AuthService(authRepository, userRepository);
        NoteService noteService = new NoteService(noteRepository);

        UserController userController = new UserController(userService);
        AuthController authController = new AuthController(authService);
        NoteControler noteControler = new NoteControler(noteService);

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add(staticFiles -> {
                staticFiles.directory = "/public";
                staticFiles.location = Location.CLASSPATH;
            });

            config.router.apiBuilder(() -> {
                path("/api", () -> {
                    UserRoutes.configure(userController);
                    AuthRoutes.configure(authController);
                    NoteRoutes.configure(noteControler);
                });
            });

            config.jetty.modifyServer(server -> server.setStopTimeout(5_000));
        });

        ExceptionHandlersUtil.register(app);

        app.start(serverUrl, serverPort);

        System.out.println("Server started at " + serverUrl + ":" + serverPort);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down the server...");
            MongoConnection.closeConnection();
        }));
    }
}
