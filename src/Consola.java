import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Collection;

public class Consola {
    private Scanner scanner;
    private GestorUsuarios userManager;
    private GestorRecursos resourceManager;

    public Consola() {
        this.scanner = new Scanner(System.in);
        this.userManager = new GestorUsuarios();
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
        System.out.println("Gestión de usuarios aún no implementada.");
    }

    private void manageResources() {
        while (true) {
            int option = showResourcesMenu();
            switch (option) {
                case 1:
                    addResource();
                    break;
                case 2:
                    // Falta implementar
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
}