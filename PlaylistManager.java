// COMPILED BY HARSH ADITYA

import java.io.*;
import java.util.*;

class Node {
    String song;
    Node next;
    Node prev;

    public Node(String song) {
        this.song = song;
        this.next = null;
        this.prev = null;
    }
}

public class PlaylistManager {
    private Node top;
    private Node start;

    public PlaylistManager() {
        top = null;
        start = null;
    }

    private void writeToFile(String song) {
        try (FileWriter fw = new FileWriter("playlist.txt", true)) {
            fw.write(song + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private void deleteFromFile(String song) {
        File inputFile = new File("playlist.txt");
        File tempFile = new File("temp.txt");
        boolean songDeleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().equals(song)) {
                    writer.write(line + "\n");
                } else {
                    songDeleted = true;
                }
            }

        } catch (IOException e) {
            System.out.println("Error processing file: " + e.getMessage());
        }

        if (songDeleted) {
            if (inputFile.delete() && tempFile.renameTo(inputFile)) {
                System.out.println("Song deleted.");
            } else {
                System.out.println("Error updating file.");
            }
        } else {
            System.out.println("Song not found in the playlist.");
        }
    }

    public void addSong(String songName) {
        Node newNode = new Node(songName);
        if (start == null) {
            start = newNode;
        } else {
            Node temp = start;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.prev = temp;
        }
        writeToFile(songName);
    }

    public void deleteSong(String songName) {
        if (start == null) {
            System.out.println("Playlist is empty.");
            return;
        }

        Node temp = start;
        while (temp != null) {
            if (temp.song.equals(songName)) {
                if (temp.prev != null) {
                    temp.prev.next = temp.next;
                } else {
                    start = temp.next;
                }

                if (temp.next != null) {
                    temp.next.prev = temp.prev;
                }

                deleteFromFile(songName);
                System.out.println("Song deleted: " + songName);
                return;
            }
            temp = temp.next;
        }
        System.out.println("Song not found.");
    }

    public void displayPlaylist() {
        if (start == null) {
            System.out.println("Playlist is empty.");
            return;
        }

        Node temp = start;
        System.out.println("Playlist:");
        while (temp != null) {
            System.out.println(temp.song);
            temp = temp.next;
        }
    }

    public void playSong(String songName) {
        Node temp = start;
        while (temp != null) {
            if (temp.song.equals(songName)) {
                System.out.println("Now playing: " + songName);
                pushToRecent(songName);
                return;
            }
            temp = temp.next;
        }
        System.out.println("Song not found in playlist.");
    }

    private void pushToRecent(String song) {
        Node newNode = new Node(song);
        newNode.next = top;
        top = newNode;
    }

    public void displayRecent() {
        if (top == null) {
            System.out.println("No recently played tracks.");
            return;
        }

        System.out.println("Recently played tracks:");
        Node temp = top;
        while (temp != null) {
            System.out.println(temp.song);
            temp = temp.next;
        }
    }

    public void loadPlaylistFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("playlist.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addSong(line);
            }
            System.out.println("Playlist loaded from file.");
        } catch (IOException e) {
            System.out.println("Error loading playlist: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        PlaylistManager playlist = new PlaylistManager();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("**WELCOME**\n**Please use '_' for spaces.**");

        while (true) {
            System.out.println("\nMenu:\n1. Add Song\n2. Delete Song\n3. Display Playlist\n4. Play Song\n5. Recently Played\n6. Load Playlist from File\n7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter song name: ");
                    String song = scanner.nextLine();
                    playlist.addSong(song);
                    break;
                case 2:
                    System.out.print("Enter song name to delete: ");
                    song = scanner.nextLine();
                    playlist.deleteSong(song);
                    break;
                case 3:
                    playlist.displayPlaylist();
                    break;
                case 4:
                    System.out.print("Enter song name to play: ");
                    song = scanner.nextLine();
                    playlist.playSong(song);
                    break;
                case 5:
                    playlist.displayRecent();
                    break;
                case 6:
                    playlist.loadPlaylistFromFile();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}