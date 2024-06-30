import java.io.*;
import java.util.*;

public class Library implements Serializable {

    public class Book implements Serializable {
        private String isbn;
        private String title;
        private String author;
        private int published_Year;
        private boolean isBorrowed;

        public Book(String isbn, String title, String author, int published_Year) {
            this.isbn = isbn;
            this.title = title;
            this.author = author;
            this.published_Year = published_Year;
            this.isBorrowed = false; // Initialize to false
        }

        public String getIsbn() {
            return isbn;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public int getPublished_Year() {
            return published_Year;
        }

        public boolean isBorrowed() {
            return isBorrowed;
        }

        public void borrowBook() {
            this.isBorrowed = true;
        }

        public void returnBook() {
            this.isBorrowed = false;
        }

        public String getDetails() {
            return "[" + isbn + "] " + title + "\nAuthor: " + author + "\nPublished: " + published_Year;
        }
    }

    public class User implements Serializable {
        private String user_id;
        private String name;
        private ArrayList<Book> borrowedBooks;

        public User(String user_id, String name) {
            this.user_id = user_id;
            this.name = name;
            this.borrowedBooks = new ArrayList<>();
        }

        public String getUserId() {
            return user_id;
        }

        public void borrowBook(Book book) {
            if (!book.isBorrowed()) {
                borrowedBooks.add(book);
                book.borrowBook();
                System.out.println("Book borrowed: " + book.getTitle());
            } else {
                System.out.println("Book is already borrowed.");
            }
        }

        public void returnBook(Book book) {
            if (borrowedBooks.contains(book)) {
                borrowedBooks.remove(book);
                book.returnBook();
                System.out.println("Book returned: " + book.getTitle());
            } else {
                System.out.println("You have not borrowed this book.");
            }
        }

        public void checkBookStatus(Book book) {
            if (borrowedBooks.contains(book)) {
                System.out.println(book.getTitle() + " is borrowed by you.");
            } else {
                System.out.println(book.getTitle() + " is not borrowed by you.");
            }
        }
    }

    public class Admin implements Serializable {
        private Map<String, Book> books;
        private Map<String, User> users;
        private static final String FILE_ADMIN = "admin.dat";

        public Admin() {
            books = new HashMap<>();
            users = new HashMap<>();
            loadAdmin();
        }

        public void addBook(String isbn, String title, String author, int published_Year) {
            if (isbn != null && title != null && !isbn.trim().isEmpty() && !title.trim().isEmpty()) {
                Book newBook = new Book(isbn, title, author, published_Year);
                books.put(isbn, newBook);
                saveAdmin();
                System.out.println("Book added: " + title);
            } else {
                System.out.println("Error: Book ISBN and Title cannot be null or empty.");
            }
        }

        public void removeBook(String isbn) {
            if (isbn != null && !isbn.trim().isEmpty()) {
                if (books.containsKey(isbn)) {
                    books.remove(isbn);
                    saveAdmin();
                    System.out.println("Book with ISBN: " + isbn + " has been removed.");
                } else {
                    System.out.println("Error: Book with ISBN: " + isbn + " not found.");
                }
            } else {
                System.out.println("Error: ISBN cannot be null or empty.");
            }
        }

        public void viewAllBooks() {
            if (books.isEmpty()) {
                System.out.println("\nNo Book available.");
            } else {
                System.out.println("\n------- BOOK --------");
                for (Book book : books.values()) {
                    System.out.println(book.getDetails());
                    System.out.println("----------------------");
                }
            }
        }

        public void registerUser(String userId, String name) {
            if (!userId.trim().isEmpty() && !name.trim().isEmpty()) {
                User newUser = new User(userId, name);
                users.put(userId, newUser);
                saveAdmin();
                System.out.println("User registered: " + name);
            } else {
                System.out.println("Error: User ID and Name cannot be null or empty.");
            }
        }

        public User getUser(String userId) {
            return users.get(userId);
        }

        public void removeUser(String userId) {
            if (users.containsKey(userId)) {
                users.remove(userId);
                saveAdmin();
                System.out.println("User with ID: " + userId + " has been removed.");
            } else {
                System.out.println("Error: User with ID: " + userId + " not found.");
            }
        }

        public void saveAdmin() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_ADMIN))) {
                oos.writeObject(this);
            } catch (Exception e) {
                System.out.println("Error saving: " + e.getMessage());
            }
        }

        public void loadAdmin() {
            File file = new File(FILE_ADMIN);
            if (file.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_ADMIN))) {
                    Admin loadedAdmin = (Admin) ois.readObject();
                    this.books = loadedAdmin.books;
                    this.users = loadedAdmin.users;
                } catch (Exception e) {
                    System.out.println("Error loading: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        Library library = new Library();
        Admin admin = library.new Admin();
        User currentUser = null;

        int choice;
        int admin_password = 1234;

        System.out.println("------- MENU -------");
        System.out.println("  1. Admin\n  2. User");
        System.out.println("--------------------");

        System.out.print("Please press the menu you want to access: ");
        int menu = keyboard.nextInt();
        keyboard.nextLine();

        switch (menu) {
            case 1:
                System.out.print("\nPlease enter the admin password: ");
                int pass = keyboard.nextInt();
                keyboard.nextLine();

                if (pass == admin_password) {
                    do {
                        System.out.println("\n1. Add Book");
                        System.out.println("2. Remove Book");
                        System.out.println("3. View All Books");
                        System.out.println("4. Register User");
                        System.out.println("5. Remove User");
                        System.out.println("6. Exit");
                        System.out.print("Enter your choice: ");
                        choice = keyboard.nextInt();
                        keyboard.nextLine();

                        switch (choice) {
                            case 1:
                                System.out.print("Enter ISBN: ");
                                String newISBN = keyboard.nextLine();
                                System.out.print("Enter Title: ");
                                String newTitle = keyboard.nextLine();
                                System.out.print("Enter Author: ");
                                String newAuthor = keyboard.nextLine();
                                System.out.print("Enter Published Year: ");
                                int newPublished_Year = keyboard.nextInt();
                                keyboard.nextLine();

                                admin.addBook(newISBN, newTitle, newAuthor, newPublished_Year);
                                break;

                            case 2:
                                System.out.print("Enter ISBN to remove: ");
                                String removeId = keyboard.nextLine();
                                admin.removeBook(removeId);
                                break;

                            case 3:
                                admin.viewAllBooks();
                                break;

                            case 4:
                                System.out.print("Enter User ID: ");
                                String newUserId = keyboard.nextLine();
                                System.out.print("Enter User Name: ");
                                String newName = keyboard.nextLine();

                                admin.registerUser(newUserId, newName);
                                break;

                            case 5:
                                System.out.print("Enter User ID to remove: ");
                                String userId = keyboard.nextLine();

                                admin.removeUser(userId);
                                break;

                            case 6:
                                System.out.println("Exiting...");
                                break;

                            default:
                                System.out.println("Invalid choice. Please try again.");
                        }

                    } while (choice != 6);
                } else {
                    System.out.println("You entered the wrong code.");
                }
                break;

            case 2:
                System.out.print("Enter your User ID: ");
                String userId = keyboard.nextLine();
                currentUser = admin.getUser(userId);

                if (currentUser == null) {
                    System.out.println("User not found. Please register first.");
                    break;
                }

                do {
                    System.out.println("\n1. Borrow Book");
                    System.out.println("2. Return Book");
                    System.out.println("3. Check Book Status");
                    System.out.println("4. Exit");
                    System.out.print("Enter your choice: ");
                    choice = keyboard.nextInt();
                    keyboard.nextLine();

                    switch (choice) {
                        case 1:
                            System.out.print("Enter ISBN of the book you want to borrow: ");
                            String borrowISBN = keyboard.nextLine();

                            Book borrowBook = admin.books.get(borrowISBN);
                            if (borrowBook != null) {
                                currentUser.borrowBook(borrowBook);
                            } else {
                                System.out.println("Book not found.");
                            }
                            break;

                        case 2:
                            System.out.print("Enter ISBN of the book you want to return: ");
                            String returnISBN = keyboard.nextLine();

                            Book returnBook = admin.books.get(returnISBN);
                            if (returnBook != null) {
                                currentUser.returnBook(returnBook);
                            } else {
                                System.out.println("Book not found.");
                            }
                            break;

                        case 3:
                            System.out.print("Enter ISBN of the book you want to check: ");
                            String checkISBN = keyboard.nextLine();

                            Book checkBook = admin.books.get(checkISBN);
                            if (checkBook != null) {
                                currentUser.checkBookStatus(checkBook);
                            } else {
                                System.out.println("Book not found.");
                            }
                            break;

                        case 4:
                            System.out.println("Exiting...");
                            break;

                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } while (choice != 4);
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
                break;

        }
        keyboard.close();
    }
}
