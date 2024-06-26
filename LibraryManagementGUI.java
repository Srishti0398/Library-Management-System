import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
class Book{
    private int id;
    private String title;
    private String author;
    private int quantity;
    private int available;
    public Book(int id, String title,String author, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.available = quantity;
    }
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public int getQuantity(){
        return quantity;
    }
    public int getAvailable(){
        return available;
    }
    public void borrow(){
        if (available > 0){
            available--;
        }
    }
    public void returnBook() {
        if (available < quantity) {
            available++;
        }
    }
    public String toString() {
        return "Book ID: " + id + ", Title: " + title + ", Author: " + author + ", Available Copies: " + available + " / " + quantity;
    }
}
class Student{
    private String name;
    private Date entryTime;
    private Date exitTime;
    public Student(String name, Date entryTime){
        this.name = name;
        this.entryTime = entryTime;
        this.exitTime = null;
    }
    public String getName(){
        return name;
    }
    public Date getEntryTime(){
        return entryTime;
    }
    public Date getExitTime(){
        return exitTime;
    }
    public void setExitTime(Date exitTime) {
        this.exitTime = exitTime;
    }
}
class Library {
    private List<Book> books;
    private int nextBookId;
    private List<Student> studentLog;
    private Map<Student, List<Book>> borrowedBooks;
    private SimpleDateFormat dateFormat;
    public Library() {
        books = new ArrayList<>();
        studentLog = new ArrayList<>();
        nextBookId = 1;
        borrowedBooks = new HashMap<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public void addBook(String title, String author, int quantity) {
        books.add(new Book(nextBookId, title, author, quantity));
        nextBookId++;
    }
    public List<Book> getBooks() {
        return books;
    }
    public void logStudentEntry(String studentName){
        studentLog.add(new Student(studentName, new Date()));
    }
    public void logStudentExit(String studentName){
        for (Student student : studentLog){
            if (student.getName().equals(studentName) && student.getExitTime() == null) {
                student.setExitTime(new Date());
                break;
            }
        }
    }
    public void logBookBorrow(Student student, Book book) {
        if (borrowedBooks.containsKey(student)) {
            borrowedBooks.get(student).add(book);
        } else {
            List<Book> borrowed = new ArrayList<>();
            borrowed.add(book);
            borrowedBooks.put(student, borrowed);
        }
    }
    public void logBookReturn(Student student, Book book) {
        if (borrowedBooks.containsKey(student)) {
            borrowedBooks.get(student).remove(book);
        }
    }
    public List<Student> getStudentLog() {
        return studentLog;
    }
    public Book findBookById(int id) {
        for (Book book : books) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }
    public List<Book> getBorrowedBooks(Student student) {
        return borrowedBooks.get(student);
    }
}
public class LibraryManagementGUI {
    private Library library;
    private JFrame frame;
    private DefaultListModel<String> bookListModel;
    private DefaultListModel<String> studentListModel;
    private JList<String> bookList;
    private JList<String> studentList;
    private final String bookImagePath = "C:/Users/Srishti/Desktop/books.jpg";
    private final String libraryImagePath = "C:/Users/Srishti/Desktop/library.jpg";
    public LibraryManagementGUI(){
        library = new Library();
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        ImageIcon bookIcon = new ImageIcon(bookImagePath);
        JLabel bookImageLabel = new JLabel(bookIcon);
        ImageIcon libraryIcon = new ImageIcon(libraryImagePath);
        JLabel libraryImageLabel = new JLabel(libraryIcon);
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        JScrollPane scrollPane = new JScrollPane(bookList);
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        JScrollPane studentScrollPane = new JScrollPane(studentList);
        JButton listBooksButton = new JButton("List Books");
        JButton addBookButton = new JButton("Add Book");
        JButton borrowBookButton = new JButton("Borrow Book");
        JButton returnBookButton = new JButton("Return Book");
        JButton studentEntryButton = new JButton("Student Entry");
        JButton studentExitButton = new JButton("Student Exit");
        listBooksButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                listBooks();
            }
        });
        addBookButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        borrowBookButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                borrowBook();
            }
        });
        returnBookButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                returnBook();
            }
        });
        studentEntryButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                studentEntry();
            }
        });

        studentExitButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                studentExit();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(listBooksButton);
        buttonPanel.add(addBookButton);
        buttonPanel.add(borrowBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(studentEntryButton);
        buttonPanel.add(studentExitButton);
        JPanel imagePanel = new JPanel();
        imagePanel.add(bookImageLabel);
        imagePanel.add(libraryImageLabel);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2));
        panel.add(scrollPane);
        panel.add(studentScrollPane);
        frame.setLayout(new BorderLayout());
        frame.add(imagePanel, BorderLayout.NORTH); 
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private void listBooks(){
        bookListModel.clear();
        for (Book book : library.getBooks()){
            bookListModel.addElement(book.toString());
        }
        listStudents();
    }
    private void listStudents(){
        studentListModel.clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Student student : library.getStudentLog()) {
            String exitTime = student.getExitTime() != null ? dateFormat.format(student.getExitTime()) : "N/A";
            StringBuilder studentInfo = new StringBuilder("Student Name: " + student.getName() +
                ", Entry Time: " + dateFormat.format(student.getEntryTime()) +
                ", Exit Time: " + exitTime + "\n");
            List<Book> borrowedBooks = library.getBorrowedBooks(student);
            if (borrowedBooks != null && !borrowedBooks.isEmpty()){
                studentInfo.append("Borrowed Books:\n");
                for (Book book : borrowedBooks) {
                    studentInfo.append("  - ").append(book.getTitle()).append("\n");
                }
            }
            studentListModel.addElement(studentInfo.toString());
        }
    }
    private void addBook() {
        String title = JOptionPane.showInputDialog("Enter the title of the book:");
        String author = JOptionPane.showInputDialog("Enter the author of the book:");
        String quantityInput = JOptionPane.showInputDialog("Enter the quantity of copies:");
        if (title != null && author != null && quantityInput != null){
            try {
                int quantity = Integer.parseInt(quantityInput);
                library.addBook(title, author, quantity);
                listBooks();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input for quantity. Please enter a valid number.");
            }
        }
    }
    private void borrowBook(){
        String studentName = JOptionPane.showInputDialog("Enter student's name:");
        String bookIdInput = JOptionPane.showInputDialog("Enter the book ID to borrow:");
        if (studentName != null && bookIdInput != null){
            try {
                int borrowId = Integer.parseInt(bookIdInput);
                Book borrowBook = library.findBookById(borrowId);
                if (borrowBook != null && borrowBook.getAvailable() > 0){
                    Student student = library.getStudentLog().stream()
                            .filter(s -> s.getName().equals(studentName))
                            .findFirst()
                            .orElse(null);
                    if (student != null) {
                        borrowBook.borrow();
                        library.logBookBorrow(student, borrowBook);
                        listBooks();
                    } else {
                        JOptionPane.showMessageDialog(null, "Student not found.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "All copies of the book are currently borrowed.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid book ID.");
            }
        }
    }
    private void returnBook() {
        String studentName = JOptionPane.showInputDialog("Enter student's name:");
        String bookIdInput = JOptionPane.showInputDialog("Enter the book ID to return:");
        if (studentName != null && bookIdInput != null) {
            try {
                int returnId = Integer.parseInt(bookIdInput);
                Book returnBook = library.findBookById(returnId);
                if (returnBook != null && returnBook.getAvailable() < returnBook.getQuantity()) {
                    Student student = library.getStudentLog().stream()
                            .filter(s -> s.getName().equals(studentName))
                            .findFirst()
                            .orElse(null);
                    if (student != null) {
                        returnBook.returnBook();
                        library.logBookReturn(student, returnBook);
                        listBooks();
                    } else {
                        JOptionPane.showMessageDialog(null, "Student not found.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No copies of the book are currently borrowed.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid book ID.");
            }
        }
    }
    private void studentEntry() {
        String studentName = JOptionPane.showInputDialog("Enter student's name for entry:");
        if (studentName != null && !studentName.isEmpty()) {
            library.logStudentEntry(studentName);
            listStudents();
        }
    }
    private void studentExit() {
        String studentName = JOptionPane.showInputDialog("Enter student's name for exit:");
        if (studentName != null && !studentName.isEmpty()) {
            library.logStudentExit(studentName);
            listStudents();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LibraryManagementGUI();
            }
        });
    }
}
