import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
class Book {
    private int id;
    private String title;
    private String author;
    private int quantity;
    private int available;
    private Map<Student, Date> borrowTimes;
    private Map<Student, Date> returnTimes;
    public Book(int id, String title, String author, int quantity){
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
        this.available = quantity;
        this.borrowTimes = new HashMap<>();
        this.returnTimes = new HashMap<>();
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
    public void borrow(Student student){
        if (available > 0){
            available--;
            borrowTimes.put(student, new Date());
        }
    }
    public void returnBook(Student student){
        if (available < quantity){
            available++;
            returnTimes.put(student, new Date());
        }
    }
    public Date getBorrowTime(Student student){
        return borrowTimes.get(student);
    }
    public Date getReturnTime(Student student){
        return returnTimes.get(student);
    }
    public String toString(){
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
    public void setExitTime(Date exitTime){
        this.exitTime = exitTime;
    }
}
class Library{
    private List<Book> books;
    private int nextBookId;
    private List<Student> studentLog;
    private Map<Student, List<Book>> borrowedBooks;
    private SimpleDateFormat dateFormat;
    public Library(){
        books = new ArrayList<>();
        studentLog = new ArrayList<>();
        nextBookId = 1;
        borrowedBooks = new HashMap<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public void addBook(String title, String author, int quantity){
        books.add(new Book(nextBookId, title, author, quantity));
        nextBookId++;
    }
    public List<Book> getBooks(){
        return books;
    }
    public void logStudentEntry(String studentName){
        studentLog.add(new Student(studentName, new Date()));
    }
    public void logStudentExit(String studentName){
        for (Student student : studentLog){
            if (student.getName().equals(studentName) && student.getExitTime() == null){
                student.setExitTime(new Date());
                break;
            }
        }
    }
    public void logBookBorrow(Student student, Book book){
        book.borrow(student);
        if (borrowedBooks.containsKey(student)){
            borrowedBooks.get(student).add(book);
        } else{
            List<Book> borrowed = new ArrayList<>();
            borrowed.add(book);
            borrowedBooks.put(student, borrowed);
        }
    }
    public void logBookReturn(Student student, Book book){
        book.returnBook(student);
        if (borrowedBooks.containsKey(student)){
            borrowedBooks.get(student).remove(book);
        }
    }
    public List<Student> getStudentLog(){
        return studentLog;
    }
    public Book findBookById(int id){
        for (Book book : books) {
            if (book.getId() == id){
                return book;
            }
        }
        return null;
    }
    public List<Book> getBorrowedBooks(Student student){
        return borrowedBooks.get(student);
    }
}
public class LibraryManagementGUI{
    private Library library;
    private JFrame frame;
    private DefaultListModel<String> bookListModel;
    private DefaultListModel<String> studentListModel;
    private JTextArea bookTextArea;
    private JTextArea studentTextArea;
    private final String backgroundImagePath = "C:/Users/Srishti/Desktop/library.jpg";
    public LibraryManagementGUI(){
        library = new Library();
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        ImageIcon backgroundIcon = new ImageIcon(backgroundImagePath);
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(1600, 1200, Image.SCALE_SMOOTH); 
        backgroundIcon = new ImageIcon(backgroundImage);
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        frame.setContentPane(new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        });
        bookListModel = new DefaultListModel<>();
        studentListModel = new DefaultListModel<>();
        bookTextArea = new JTextArea();
        bookTextArea.setEditable(false);
        bookTextArea.setOpaque(false);
        bookTextArea.setForeground(Color.WHITE);
        bookTextArea.setFont(new Font("Serif", Font.BOLD, 14));
        JScrollPane bookScrollPane = new JScrollPane(bookTextArea);
        bookScrollPane.setOpaque(false);
        bookScrollPane.getViewport().setOpaque(false);
        studentTextArea = new JTextArea();
        studentTextArea.setEditable(false);
        studentTextArea.setOpaque(false);
        studentTextArea.setForeground(Color.WHITE);
        studentTextArea.setFont(new Font("Serif", Font.BOLD, 14));
        JScrollPane studentScrollPane = new JScrollPane(studentTextArea);
        studentScrollPane.setOpaque(false);
        studentScrollPane.getViewport().setOpaque(false);
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
            public void actionPerformed(ActionEvent e){
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
        buttonPanel.setOpaque(false);
        buttonPanel.add(listBooksButton);
        buttonPanel.add(addBookButton);
        buttonPanel.add(borrowBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(studentEntryButton);
        buttonPanel.add(studentExitButton);
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.setOpaque(false);
        panel.add(bookScrollPane);
        panel.add(studentScrollPane);
        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
    private void listBooks(){
        bookTextArea.setText("");
        for (Book book : library.getBooks()) {
            bookTextArea.append(book.toString() + "\n");
        }
        listStudents();
    }
    private void listStudents(){
        studentTextArea.setText("");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Student student : library.getStudentLog()){
            String exitTime = student.getExitTime() != null ? dateFormat.format(student.getExitTime()) : "N/A";
            StringBuilder studentInfo = new StringBuilder("Student Name: " + student.getName() +
                    ", Entry Time: " + dateFormat.format(student.getEntryTime()) +
                    ", Exit Time: " + exitTime + "\n");
            List<Book> borrowedBooks = library.getBorrowedBooks(student);
            if (borrowedBooks != null && !borrowedBooks.isEmpty()){
                studentInfo.append("Borrowed Books:\n");
                for (Book book : borrowedBooks){
                    String borrowTime = dateFormat.format(book.getBorrowTime(student));
                    String returnTime = book.getReturnTime(student) != null ? dateFormat.format(book.getReturnTime(student)) : "Not returned";
                    studentInfo.append(" - Book ID: ").append(book.getId())
                            .append(", Title: ").append(book.getTitle())
                            .append(", Borrow Time: ").append(borrowTime)
                            .append(", Return Time: ").append(returnTime).append("\n");
                }
            }
            studentTextArea.append(studentInfo.toString() + "\n");
        }
    }
    private void addBook(){
        String title = JOptionPane.showInputDialog("Enter the title of the book:");
        String author = JOptionPane.showInputDialog("Enter the author of the book:");
        int quantity = Integer.parseInt(JOptionPane.showInputDialog("Enter the quantity of the book:"));
        library.addBook(title, author, quantity);
        listBooks();
    }
    private void borrowBook(){
        String studentName = JOptionPane.showInputDialog("Enter the name of the student:");
        int bookId = Integer.parseInt(JOptionPane.showInputDialog("Enter the book ID to borrow:"));
        Book book = library.findBookById(bookId);
        if (book != null && book.getAvailable() > 0){
            Student student = findStudentByName(studentName);
            if (student != null){
                library.logBookBorrow(student, book);
                listBooks();
            } else{
                JOptionPane.showMessageDialog(frame, "Student not found.");
            }
        } else{
            JOptionPane.showMessageDialog(frame, "Book not found or no copies available.");
        }
    }
    private void returnBook(){
        String studentName = JOptionPane.showInputDialog("Enter the name of the student:");
        int bookId = Integer.parseInt(JOptionPane.showInputDialog("Enter the book ID to return:"));
        Book book = library.findBookById(bookId);
        if (book != null){
            Student student = findStudentByName(studentName);
            if (student != null){
                library.logBookReturn(student, book);
                listBooks();
            } else{
                JOptionPane.showMessageDialog(frame, "Student not found.");
            }
        } else{
            JOptionPane.showMessageDialog(frame, "Book not found.");
        }
    }
    private void studentEntry(){
        String studentName = JOptionPane.showInputDialog("Enter the name of the student:");
        library.logStudentEntry(studentName);
        listStudents();
    }
    private void studentExit(){
        String studentName = JOptionPane.showInputDialog("Enter the name of the student:");
        library.logStudentExit(studentName);
        listStudents();
    }
    private Student findStudentByName(String name){
        for (Student student : library.getStudentLog()){
            if (student.getName().equals(name) && student.getExitTime() == null){
                return student;
            }
        }
        return null;
    }
    public static void main(String[] args){
        new LibraryManagementGUI();
    }
}
