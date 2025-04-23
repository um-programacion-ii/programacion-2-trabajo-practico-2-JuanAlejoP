import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Collection;

public class Consola {
    private Scanner scanner;
    private GestorUsuarios userManager;
    private GestorRecursos resourceManager;
    private ServicioNotificaciones notifications;

    public Consola(ServicioNotificaciones notifications) {
        this.scanner = new Scanner(System.in);
        this.notifications = notifications;
        this.userManager = new GestorUsuarios(notifications);
        this.resourceManager = new GestorRecursos();
    }


    public void showMainMenu() {
        System.out.println("----- Menú Principal -----");
        System.out.println("1. Gestionar usuarios");
        System.out.println("2. Gestionar recursos");
        System.out.println("0. Salir");
        System.out.print("Ingrese una opción: ");

    }

    public void init() {
        while (true) {
            showMainMenu();
            try {
                int option = scanner.nextInt();
                scanner.nextLine();
                if (option == 1) {
                    manageUsers();
                } else if (option == 2) {
                    manageResources();
                } else if (option == 0) {
                    System.out.println("Saliendo...");
                    break;
                } else {
                    System.out.println("Opción inválida");
                }
            } catch (InputMismatchException e) {
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

    private void manageResources() {
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

        resourceManager.addNewResource(recurso);
        System.out.println("Recurso agregado exitosamente.");
    }

    private void searchResource() {
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
            System.out.printf("%-3d %-12s %-15s %-25s%n", i++,
                    recurso.getIdentificador(), recurso.getTitulo(), recurso.getEstado());
        }
        System.out.println("--------------------------------------------------------");
        System.out.println("Total de recursos: " + recursos.size());
    }

    private void showResourceOptions(RecursoDigital recurso) {
        System.out.println("--- Opciones para el recurso ---");
        if (recurso instanceof Prestable) {
            System.out.println("P. Prestar recurso");
        }

        if (recurso instanceof Renovable) {
            System.out.println("R. Renovar recurso");
        }
        if (!(recurso instanceof Prestable) && !(recurso instanceof Renovable)) {
            System.out.println("!. Este recurso no puede ser prestado ni renovado.");
        }
    }
}