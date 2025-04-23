package com.JuanAlejoP.biblioteca.ui;

import com.JuanAlejoP.biblioteca.Prestable;
import com.JuanAlejoP.biblioteca.Renovable;
import com.JuanAlejoP.biblioteca.alerts.*;
import com.JuanAlejoP.biblioteca.manager.GestorRecursos;
import com.JuanAlejoP.biblioteca.manager.GestorUsuarios;
import com.JuanAlejoP.biblioteca.model.*;
import com.JuanAlejoP.biblioteca.reports.AsyncReportGenerator;
import com.JuanAlejoP.biblioteca.reports.GeneradorReportes;
import com.JuanAlejoP.biblioteca.service.NotificadorConsola;
import com.JuanAlejoP.biblioteca.service.PreferencesManager;
import com.JuanAlejoP.biblioteca.service.ServicioNotificaciones;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Collection;
import java.util.List;

import java.time.format.DateTimeFormatter;

/**
 * Interfaz de línea de comandos para la biblioteca.
 * <p>
 * Gestiona el flujo principal de la aplicación: usuarios, recursos, reportes
 * y alertas. Se encarga de presentar menús, leer entradas y delegar
 * acciones a gestores y servicios.
 * </p>
 */
public class Consola {

    private final Scanner scanner;
    private final PreferencesManager preferences;
    private final ServicioNotificaciones notifications;
    private final GestorUsuarios userManager;
    private final GestorRecursos resourceManager;
    private final AsyncReportGenerator asyncGenerator;
    private final AlertScheduler alertScheduler;

    /**
     * Construye la consola inicializando gestores, servicios y programador de alertas.
     */
    public Consola() {
        this.scanner = new Scanner(System.in);
        this.preferences = new PreferencesManager();
        this.notifications = new NotificadorConsola(preferences);
        this.userManager = new GestorUsuarios(notifications);
        this.resourceManager = new GestorRecursos();
        this.asyncGenerator = new AsyncReportGenerator(
                new GeneradorReportes(resourceManager, userManager)
        );
        this.alertScheduler = new AlertScheduler(
                new AlertaVencimiento(resourceManager, userManager, notifications),
                new AlertaDisponibilidad(resourceManager, userManager, notifications)
        );
        alertScheduler.start();
    }

    /**
     * Muestra las opciones principales del menú.
     */
    public void showMainMenu() {
        System.out.println("----- Menú Principal -----");
        System.out.println("1. Gestionar usuarios");
        System.out.println("2. Gestionar recursos");
        System.out.println("3. Generar reportes");
        System.out.println("4. Ver alertas");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opción: ");
    }

    /**
     * Bucle principal que procesa la opción seleccionada hasta salir.
     */
    public void init() {
        while (true) {
            showMainMenu();
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1: manageUsers(); break;
                    case 2: manageResources(); break;
                    case 3: asyncGenerator.generarReportesEnSegundoPlano(); break;
                    case 4: viewAlerts(); break;
                    case 0:
                        System.out.println("Saliendo...");
                        alertScheduler.shutdown();
                        return;
                    default: System.out.println("Opción inválida");
                }
            } catch (InputMismatchException | RecursoNoDisponibleException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Gestiona menú de usuarios: agregar, buscar o listar.
     */
    private void manageUsers() {
        while (true) {
            int option = showUsersMenu();
            switch (option) {
                case 1: addUser(); break;
                case 2: searchUser(); break;
                case 3: listUsers(); break;
                case 0: return;
                default: System.out.println("Opción inválida.");
            }
        }
    }

    /**
     * Muestra opciones del submenú de usuarios.
     *
     * @return Opción seleccionada o -1 si la entrada fue inválida.
     */
    private int showUsersMenu() {
        System.out.println("--- Gestión de Usuarios ---");
        System.out.println("1. Agregar nuevo usuario");
        System.out.println("2. Buscar usuario por ID");
        System.out.println("3. Listar todos los usuarios");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine();
            return option;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Entrada inválida. Intente de nuevo.");
            return -1;
        }
    }

    /**
     * Solicita datos y agrega un nuevo usuario.
     */
    private void addUser() {
        System.out.println("Ingrese el nombre del usuario: ");
        String name = scanner.nextLine();
        System.out.println("Ingrese ID del usuario: ");
        String id = scanner.nextLine();
        System.out.println("Ingrese email del usuario: ");
        String email = scanner.nextLine();
        System.out.println("Ingrese telefono del usuario: ");
        String telefono = scanner.nextLine();
        userManager.addNewUser(new Usuario(name, id, email, telefono));
        System.out.println("Usuario agregado exitosamente.");
    }

    /**
     * Solicita ID y muestra datos del usuario.
     */
    private void searchUser() {
        System.out.println("Ingrese ID del usuario a buscar: ");
        String id = scanner.nextLine();
        Usuario usuario = userManager.searchUserById(id);
        System.out.println("--- Datos del Usuario ---");
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("ID: " + usuario.getId());
        System.out.println("Email: " + usuario.getEmail());
    }

    /**
     * Lista todos los usuarios con formato tabular.
     */
    private void listUsers() {
        Collection<Usuario> usuarios = userManager.listAllUsers();
        System.out.println("---- Lista de Usuarios ----");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.printf("%-3s %-15s %-12s %-25s%n", "#", "Nombre", "ID", "Email");
        usuarios.forEach(u -> System.out.printf("%-3d %-15s %-12s %-25s%n",
                usuarios.stream().toList().indexOf(u)+1, u.getNombre(), u.getId(), u.getEmail()));
        System.out.println("Total de usuarios: " + usuarios.size());
    }

    /**
     * Gestiona menú de recursos: añadir, buscar, listar, etc.
     *
     * @throws RecursoNoDisponibleException si la operación de búsqueda falla.
     */
    private void manageResources() throws RecursoNoDisponibleException {
        while (true) {
            int option = showResourcesMenu();
            switch (option) {
                case 1: addResource(); break;
                case 2: searchResource(); break;
                case 3: listResources(); break;
                case 4: searchByTitle(); break;
                case 5: filterByCategoria(); break;
                case 6: showCategoriasDisponibles(); break;
                case 0: return;
                default: System.out.println("Opción inválida.");
            }
        }
    }

    /**
     * Muestra opciones del submenú de recursos.
     *
     * @return Opción seleccionada o -1 si la entrada fue inválida.
     */
    private int showResourcesMenu() {
        System.out.println("--- Gestión de Recursos ---");
        System.out.println("1. Agregar nuevo recurso");
        System.out.println("2. Buscar recurso por ID");
        System.out.println("3. Listar todos los recursos");
        System.out.println("4. Buscar recursos por título");
        System.out.println("5. Filtrar recursos por categoría");
        System.out.println("6. Ver categorías disponibles");
        System.out.println("0. Volver al menú principal");
        System.out.print("Ingrese una opción: ");
        try {
            int option = scanner.nextInt();
            scanner.nextLine();
            return option;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Entrada inválida. Intente de nuevo.");
            return -1;
        }
    }

    /**
     * Solicita datos para crear un nuevo recurso y lo agrega.
     */
    private void addResource() {
        System.out.println("Ingrese ID del recurso: ");
        String id = scanner.nextLine();
        System.out.println("Ingrese el título del recurso: ");
        String title = scanner.nextLine();
        System.out.println("Seleccione el tipo de recurso: ");
        System.out.println("1. Libro");
        System.out.println("2. Revista");
        System.out.println("3. Audiolibro");
        int tipoRecurso = scanner.nextInt();
        scanner.nextLine();

        RecursoDigital recurso;
        switch (tipoRecurso) {
            case 1: recurso = new Libro(id, title, EstadoRecurso.DISPONIBLE); break;
            case 2: recurso = new Revista(id, title, EstadoRecurso.DISPONIBLE); break;
            case 3: recurso = new Audiolibro(id, title, EstadoRecurso.DISPONIBLE); break;
            default:
                System.out.println("Opción inválida, no se creó el recurso.");
                return;
        }

        System.out.println("Seleccione la categoría del recurso: ");
        for (int i = 0; i < CategoriaRecurso.values().length; i++) {
            System.out.printf("%d. %s%n", i+1, CategoriaRecurso.values()[i]);
        }
        int catIndex = scanner.nextInt();
        scanner.nextLine();
        if (catIndex >= 1 && catIndex <= CategoriaRecurso.values().length) {
            recurso.setCategoria(CategoriaRecurso.values()[catIndex - 1]);
        } else {
            System.out.println("Categoría inválida. Se asignará OTRO por defecto.");
            recurso.setCategoria(CategoriaRecurso.OTRO);
        }

        resourceManager.addNewResource(recurso);
        System.out.println("Recurso agregado exitosamente.");
    }

    /**
     * Solicita ID y muestra datos del recurso, luego presenta opciones específicas.
     *
     * @throws RecursoNoDisponibleException si no encuentra el recurso.
     */
    private void searchResource() throws RecursoNoDisponibleException {
        System.out.println("Ingrese ID del recurso a buscar: ");
        String id = scanner.nextLine();
        RecursoDigital recurso = resourceManager.searchResourceById(id);
        System.out.println("--- Datos del Recurso ---");
        System.out.println("ID: " + recurso.getIdentificador());
        System.out.println("Título: " + recurso.getTitulo());
        System.out.println("Status: " + recurso.getEstado());
        showResourceOptions(recurso);
    }

    /**
     * Lista todos los recursos en formato tabular.
     */
    private void listResources() {
        Collection<RecursoDigital> recursos = resourceManager.listAllResources();
        System.out.println("---- Lista de Recursos ----");
        if (recursos.isEmpty()) {
            System.out.println("No hay recursos registrados.");
            return;
        }
        System.out.printf("%-3s %-12s %-15s %-25s%n", "#", "ID", "Título", "Estado");
        int i = 1;
        for (RecursoDigital recurso : recursos) {
            System.out.printf("%-3d %-12s %-15s %-25s%n", i++,
                    recurso.getIdentificador(), recurso.getTitulo(), recurso.getEstado());
        }
        System.out.println("Total de recursos: " + recursos.size());
    }

    /**
     * Busca y muestra recursos cuyo título contiene texto dado.
     */
    private void searchByTitle() {
        System.out.println("Ingrese texto a buscar en el título: ");
        String query = scanner.nextLine();
        List<RecursoDigital> resultados = resourceManager.searchByTitle(query);
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron recursos con ese título.");
        } else {
            resultados.forEach(r -> System.out.println(
                    r.getIdentificador() + ": " + r.getTitulo() + " (" + r.getEstado() + ")"
            ));
        }
    }

    /**
     * Filtra recursos por categoría seleccionada.
     */
    private void filterByCategoria() {
        System.out.println("Seleccione la categoría a filtrar:");
        for (int i = 0; i < CategoriaRecurso.values().length; i++) {
            System.out.printf("%d. %s%n", i+1, CategoriaRecurso.values()[i]);
        }
        int catIndex = scanner.nextInt();
        scanner.nextLine();
        if (catIndex >= 1 && catIndex <= CategoriaRecurso.values().length) {
            var cat = CategoriaRecurso.values()[catIndex - 1];
            var filtrados = resourceManager.filterByCategoria(cat);
            if (filtrados.isEmpty()) {
                System.out.println("No se encontraron recursos en la categoría seleccionada.");
            } else {
                filtrados.forEach(r -> System.out.println(
                        r.getIdentificador() + ": " + r.getTitulo() + " (" + r.getEstado() + ")"
                ));
            }
        } else {
            System.out.println("Categoría inválida.");
        }
    }

    /**
     * Muestra las categorías disponibles.
     */
    private void showCategoriasDisponibles() {
        System.out.println("Categorías disponibles:");
        for (var categoria : CategoriaRecurso.values()) {
            System.out.println("- " + categoria);
        }
    }

    /**
     * Presenta acciones específicas según si el recurso es prestable o renovable.
     *
     * @param recurso Recurso seleccionado.
     */
    private void showResourceOptions(RecursoDigital recurso) {
        while (true) {
            System.out.println("--- Opciones para el recurso ---");
            System.out.println("Estado actual: " + recurso.getEstado());
            if (recurso instanceof Prestable) {
                System.out.println("1. Prestar recurso");
                System.out.println("2. Reservar recurso");
                System.out.println("3. Devolver recurso");
            }
            if (recurso instanceof Renovable) {
                System.out.println("4. Renovar recurso");
            }
            System.out.println("0. Volver");
            System.out.print("Seleccione una opción: ");
            String option = scanner.nextLine();
            try {
                switch (option) {
                    case "1": // Préstamo
                        handlePrestamo(recurso); return;
                    case "2": // Reserva
                        handleReserva(recurso); return;
                    case "3": // Devolución
                        resourceManager.devolverRecurso(recurso.getIdentificador());
                        System.out.println("Recurso devuelto correctamente.");
                        return;
                    case "4": // Renovación
                        handleRenovacion(recurso); return;
                    case "0": return;
                    default: System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (RecursoNoDisponibleException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }

    /**
     * Maneja el préstamo de un recurso solicitando ID de usuario.
     *
     * @param recurso Recurso a prestar.
     * @throws RecursoNoDisponibleException si no está disponible.
     */
    private void handlePrestamo(RecursoDigital recurso) throws RecursoNoDisponibleException {
        System.out.println("Ingrese el ID del usuario que solicita el préstamo:");
        var usuario = userManager.searchUserById(scanner.nextLine());
        resourceManager.prestarRecurso(recurso.getIdentificador(), usuario);
        System.out.println("Préstamo registrado con éxito.");
    }

    /**
     * Maneja la reserva de un recurso solicitando ID de usuario.
     *
     * @param recurso Recurso a reservar.
     * @throws RecursoNoDisponibleException si no se pudo reservar.
     */
    private void handleReserva(RecursoDigital recurso) throws RecursoNoDisponibleException {
        System.out.println("Ingrese el ID del usuario que desea reservar:");
        var usuario = userManager.searchUserById(scanner.nextLine());
        resourceManager.reservarRecurso(recurso.getIdentificador(), usuario);
        System.out.println("Reserva registrada con éxito.");
    }

    /**
     * Maneja la renovación de un recurso solicitando ID de usuario.
     *
     * @param recurso Recurso a renovar.
     * @throws RecursoNoDisponibleException si no es renovable.
     */
    private void handleRenovacion(RecursoDigital recurso) throws RecursoNoDisponibleException {
        System.out.println("Ingrese el ID del usuario que desea renovar:");
        var usuario = userManager.searchUserById(scanner.nextLine());
        resourceManager.renovarRecurso(recurso.getIdentificador(), usuario);
        System.out.println("Recurso renovado exitosamente.");
    }

    /**
     * Menú para mostrar o configurar alertas.
     */
    private void viewAlerts() {
        while (true) {
            System.out.println("--- Menú de Alertas ---");
            System.out.println("1. Mostrar alertas actuales");
            System.out.println("2. Ver historial de alertas");
            System.out.println("3. Configurar preferencias de notificación");
            System.out.println("0. Volver");
            System.out.print("Ingrese una opción: ");
            String opt = scanner.nextLine();
            switch (opt) {
                case "1":
                    new AlertaVencimiento(resourceManager, userManager, notifications).revisarVencimientos();
                    new AlertaDisponibilidad(resourceManager, userManager, notifications).revisarDisponibilidad();
                    break;
                case "2":
                    showAlertHistory();
                    break;
                case "3":
                    configureAlertPreferences();
                    break;
                case "0": return;
                default: System.out.println("Opción inválida.");
            }
        }
    }

    /**
     * Muestra el historial de notificaciones con timestamp.
     */
    private void showAlertHistory() {
        List<NotificacionEntry> history = AlertHistory.getHistory();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        history.forEach(e -> System.out.printf(
                "[%s] %s - %s (Usuario: %s)%n",
                e.getTimestamp().format(fmt), e.getType(), e.getMensaje(), e.getUsuarioId()
        ));
    }

    /**
     * Permite al usuario cambiar sus preferencias de alerta.
     */
    private void configureAlertPreferences() {
        for (AlertType type : AlertType.values()) {
            System.out.printf("¿Recibir %s? (s/n): ", type);
            boolean enable = scanner.nextLine().equalsIgnoreCase("s");
            preferences.setPreference(
                    promptUserId(), type, enable
            );
        }
    }

    /**
     * Solicita al usuario su ID.
     *
     * @return ID ingresado por consola.
     */
    private String promptUserId() {
        System.out.print("Ingrese su ID de usuario: ");
        return scanner.nextLine();
    }
}