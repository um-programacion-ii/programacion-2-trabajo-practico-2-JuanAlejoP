import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Collection;
import java.util.List;

import java.time.format.DateTimeFormatter;

public class Consola {
    private Scanner scanner;
    private PreferencesManager preferences;
    private ServicioNotificaciones notifications;
    private GestorUsuarios userManager;
    private GestorRecursos resourceManager;
    private AsyncReportGenerator asyncGenerator;
    private AlertScheduler alertScheduler;

    public Consola() {
        this.scanner = new Scanner(System.in);
        this.preferences = new PreferencesManager();
        this.notifications = new NotificadorConsola(preferences);
        this.userManager = new GestorUsuarios(notifications);
        this.resourceManager = new GestorRecursos();
        this.asyncGenerator = new AsyncReportGenerator(new GeneradorReportes(resourceManager, userManager));
        this.alertScheduler = new AlertScheduler(
                new AlertaVencimiento(resourceManager, userManager, notifications),
                new AlertaDisponibilidad(resourceManager, userManager, notifications)
        );
        alertScheduler.start();
    }

    public void showMainMenu() {
        System.out.println("----- Menú Principal -----");
        System.out.println("1. Gestionar usuarios");
        System.out.println("2. Gestionar recursos");
        System.out.println("3. Generar reportes");
        System.out.println("4. Ver alertas");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opción: ");
    }

    public void init() {
        while (true) {
            showMainMenu();
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option) {
                    case 1:
                        manageUsers();
                        break;
                    case 2:
                        manageResources();
                        break;
                    case 3:
                        // Usar generación de reportes en segundo plano
                        asyncGenerator.generarReportesEnSegundoPlano();
                        break;
                    case 4:
                        viewAlerts();
                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        alertScheduler.shutdown();
                        return;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (InputMismatchException | RecursoNoDisponibleException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número.");
                scanner.nextLine();
            }
        }
    }

    private void manageUsers() {
        while (true) {
            int option = showUsersMenu();
            switch (option) {
                case 1:
                    addUser();
                    break;
                case 2:
                    searchUser();
                    break;
                case 3:
                    listUsers();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

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

    private void searchUser() {
        System.out.println("Ingrese ID del usuario a buscar: ");
        String id = scanner.nextLine();
        Usuario usuario = userManager.searchUserById(id);
        System.out.println("--- Datos del Usuario ---");
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("ID: " + usuario.getId());
        System.out.println("Email: " + usuario.getEmail());
    }

    private void listUsers() {
        Collection<Usuario> usuarios = userManager.listAllUsers();
        System.out.println("---- Lista de Usuarios ----");
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        System.out.printf("%-3s %-15s %-12s %-25s%n", "#", "Nombre", "ID", "Email");
        System.out.println("--------------------------------------------------------");
        int i = 1;
        for (Usuario usuario : usuarios) {
            System.out.printf("%-3d %-15s %-12s %-25s%n", i++, usuario.getNombre(), usuario.getId(), usuario.getEmail());
        }
        System.out.println("--------------------------------------------------------");
        System.out.println("Total de usuarios: " + usuarios.size());
    }

    private void manageResources() throws RecursoNoDisponibleException {
        while (true) {
            int option = showResourcesMenu();
            switch (option) {
                case 1:
                    addResource();
                    break;
                case 2:
                    searchResource();
                    break;
                case 3:
                    listResources();
                    break;
                case 4:
                    searchByTitle();
                    break;
                case 5:
                    filterByCategoria();
                    break;
                case 6:
                    showCategoriasDisponibles();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

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

        RecursoDigital recurso = null;
        switch (tipoRecurso) {
            case 1:
                recurso = new Libro(id, title, EstadoRecurso.DISPONIBLE);
                break;
            case 2:
                recurso = new Revista(id, title, EstadoRecurso.DISPONIBLE);
                break;
            case 3:
                recurso = new Audiolibro(id, title, EstadoRecurso.DISPONIBLE);
                break;
            default:
                System.out.println("Opción inválida, no se creó el recurso.");
                return;
        }

        System.out.println("Seleccione la categoría del recurso: ");
        for (CategoriaRecurso categoria : CategoriaRecurso.values()) {
            System.out.println(categoria.ordinal() + 1 + ". " + categoria);
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

    private void listResources() {
        Collection<RecursoDigital> recursos = resourceManager.listAllResources();
        System.out.println("---- Lista de Recursos ----");
        if (recursos.isEmpty()) {
            System.out.println("No hay recursos registrados.");
            return;
        }
        System.out.printf("%-3s %-12s %-15s %-25s%n", "#", "ID", "Título", "Estado");
        System.out.println("--------------------------------------------------------");
        int i = 1;
        for (RecursoDigital recurso : recursos) {
            System.out.printf("%-3d %-12s %-15s %-25s%n", i++, recurso.getIdentificador(), recurso.getTitulo(), recurso.getEstado());
        }
        System.out.println("--------------------------------------------------------");
        System.out.println("Total de recursos: " + recursos.size());
    }

    private void searchByTitle() {
        System.out.println("Ingrese texto a buscar en el título: ");
        String query = scanner.nextLine();
        List<RecursoDigital> resultados = resourceManager.searchByTitle(query);
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron recursos con ese título.");
        } else {
            System.out.println("--- Resultados de búsqueda ---");
            for (RecursoDigital recurso : resultados) {
                System.out.println(recurso.getIdentificador() + ": " + recurso.getTitulo() + " (" + recurso.getEstado() + ")");
            }
        }
    }

    private void filterByCategoria() {
        System.out.println("Seleccione la categoría a filtrar:");
        for (CategoriaRecurso categoria : CategoriaRecurso.values()) {
            System.out.println(categoria.ordinal() + 1 + ". " + categoria);
        }
        int catIndex = scanner.nextInt();
        scanner.nextLine();
        if (catIndex >= 1 && catIndex <= CategoriaRecurso.values().length) {
            CategoriaRecurso categoriaSeleccionada = CategoriaRecurso.values()[catIndex - 1];
            List<RecursoDigital> filtrados = resourceManager.filterByCategoria(categoriaSeleccionada);
            if (filtrados.isEmpty()) {
                System.out.println("No se encontraron recursos en la categoría seleccionada.");
            } else {
                System.out.println("--- Recursos en categoría: " + categoriaSeleccionada + " ---");
                for (RecursoDigital recurso : filtrados) {
                    System.out.println(recurso.getIdentificador() + ": " + recurso.getTitulo() + " (" + recurso.getEstado() + ")");
                }
            }
        } else {
            System.out.println("Categoría inválida.");
        }
    }

    private void showCategoriasDisponibles() {
        System.out.println("Categorías disponibles:");
        for (CategoriaRecurso categoria : CategoriaRecurso.values()) {
            System.out.println("- " + categoria);
        }
    }

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
                    case "1":
                        System.out.println("Ingrese el ID del usuario que solicita el préstamo:");
                        String idUsuarioPrestamo = scanner.nextLine();
                        Usuario usuarioPrestamo = userManager.searchUserById(idUsuarioPrestamo);
                        resourceManager.prestarRecurso(recurso.getIdentificador(), usuarioPrestamo);
                        System.out.println("Préstamo registrado con éxito.");
                        return;
                    case "2":
                        System.out.println("Ingrese el ID del usuario que desea reservar:");
                        String idUsuarioReserva = scanner.nextLine();
                        Usuario usuarioReserva = userManager.searchUserById(idUsuarioReserva);
                        resourceManager.reservarRecurso(recurso.getIdentificador(), usuarioReserva);
                        System.out.println("Reserva registrada con éxito.");
                        return;
                    case "3":
                        resourceManager.devolverRecurso(recurso.getIdentificador());
                        System.out.println("Recurso devuelto correctamente.");
                        return;
                    case "4":
                        System.out.println("Ingrese el ID del usuario que desea renovar:");
                        String idUsuarioRenovacion = scanner.nextLine();
                        Usuario usuarioRenovacion = userManager.searchUserById(idUsuarioRenovacion);
                        resourceManager.renovarRecurso(recurso.getIdentificador(), usuarioRenovacion);
                        System.out.println("Recurso renovado exitosamente.");
                        return;
                    case "0":
                        return;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (RecursoNoDisponibleException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }

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
                    List<NotificacionEntry> history = AlertHistory.getHistory();
                    System.out.println("--- Historial de Alertas ---");
                    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    for (NotificacionEntry e : history) {
                        System.out.printf("[%s] %s - %s (Usuario: %s)%n",
                                e.getTimestamp().format(fmt), e.getType(), e.getMensaje(), e.getUsuarioId());
                    }
                    break;
                case "3":
                    for (AlertType type : AlertType.values()) {
                        System.out.printf("¿Recibir %s? (s/n): ", type);
                        boolean en = scanner.nextLine().equalsIgnoreCase("s");
                        preferences.setPreference(userManager.searchUserById(promptUserId()).getId(), type, en);
                    }
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private String promptUserId() {
        System.out.print("Ingrese su ID de usuario: ");
        return scanner.nextLine();
    }
}